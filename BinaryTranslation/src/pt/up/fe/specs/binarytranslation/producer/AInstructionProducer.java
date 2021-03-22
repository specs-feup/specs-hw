package pt.up.fe.specs.binarytranslation.producer;

import java.util.function.BiFunction;
import java.util.regex.Pattern;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.processes.StringProcessRun;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.ResourceProvider;

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
    protected final Pattern regex;
    private final BiFunction<String, String, Instruction> produceMethod;

    public AInstructionProducer(Application app, StringProcessRun prun, ResourceProvider regex,
            BiFunction<String, String, Instruction> produceMethod) {
        this.app = app;
        this.prun = prun;
        this.regex = Pattern.compile(regex.getResource());
        this.produceMethod = produceMethod;
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
        while ((line = this.prun.receive()) != null) {
            System.out.print(line + "\n");
        }
    }

    /*
     * Initialized by non-abstract children methods
     */
    protected Instruction newInstance(String address, String instruction) {
        return this.produceMethod.apply(address, instruction);
    }

    /*
     * Initialized by non-abstract children methods
     */
    private Pattern getRegex() {
        return this.regex;
    }

    @Override
    public Instruction nextInstruction() {

        String line = null;
        while (((line = this.prun.receive()) != null) && !SpecsStrings.matches(line, getRegex())) {
            // insts.next();
        }
        if (line == null)
            return null;

        var addressAndInst = SpecsStrings.getRegex(line, getRegex());
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        var newinst = this.newInstance(addr, inst);
        return newinst;
    }

    @Override
    public void close() throws Exception {
        this.prun.close();
    }
}
