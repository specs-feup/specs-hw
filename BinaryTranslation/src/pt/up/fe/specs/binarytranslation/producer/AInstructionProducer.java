package pt.up.fe.specs.binarytranslation.producer;

import java.util.regex.Pattern;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionInstantiator;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;
import pt.up.fe.specs.binarytranslation.processes.StringProcessRun;
import pt.up.fe.specs.util.SpecsStrings;

public abstract class AInstructionProducer implements InstructionProducer {

    /*
     * Interesting as output, and should be queryable by over-arching BTF chain
     */
    @Expose
    private final Application app;

    /*
     * 
     */
    protected final StringProcessRun prun;

    /*
     * Init by children
     */
    private final Pattern regex;
    private final InstructionInstantiator produceMethod;

    public AInstructionProducer(Application app,
            StringProcessRun prun, Pattern regex,
            InstructionInstantiator produceMethod) {
        this.app = app;
        this.prun = prun;
        this.regex = regex;
        this.produceMethod = produceMethod;
        this.prun.start();
    }

    @Override
    public Application getApp() {
        return app;
    }

    @Override
    public Integer getInstructionWidth() {
        return this.app.getInstructionWidth();
    }

    @Override
    public void rawDump() {
        String line = null;
        while ((line = this.prun.receive(100)) != null) {
            System.out.print(line + "\n");
        }
    }

    /*
     * Initialized by non-abstract children methods
     */
    protected Instruction newInstance(String address, String instruction, RegisterDump registers) {
        return this.produceMethod.apply(address, instruction, registers);
    }

    /*
     * Initialized by non-abstract children methods
     */
    protected Instruction newInstance(String address, String instruction) {
        return this.produceMethod.apply(address, instruction, null);
    }

    @Override
    public Instruction nextInstruction() {
        String line = null;
        while (((line = this.prun.receive(100)) != null)
                && !SpecsStrings.matches(line, this.regex))
            ;
        if (line == null)
            return null;

        var addressAndInst = SpecsStrings.getRegex(line, this.regex);
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        var newinst = this.newInstance(addr, inst);
        return newinst;
    }

    @Override
    public void close() {
        this.prun.close();
    }
}
