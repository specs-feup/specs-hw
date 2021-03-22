package pt.up.fe.specs.binarytranslation.producer;

import java.util.function.BiFunction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;
import pt.up.fe.specs.binarytranslation.processes.StringProcessRun;
import pt.up.fe.specs.binarytranslation.processes.TxtDump;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.providers.ResourceProvider;

public class TraceInstructionProducer extends AInstructionProducer {

    /*
     * Output from QEMU Execution
     */
    protected TraceInstructionProducer(Application app, ResourceProvider regex,
            BiFunction<String, String, Instruction> produceMethod) {
        super(app, TraceInstructionProducer.getProperProcess(app), regex, produceMethod);
    }

    /*
     * Determine process to use based on file extension and OS
     */
    private static StringProcessRun getProperProcess(Application app) {

        var elfname = app.getElffile();
        var name = elfname.getName();
        var extension = name.subSequence(name.length() - 3, name.length());

        // Output from GNU based objdump
        if (extension.equals("elf"))
            return new GDBRun(app, BinaryTranslationUtils.FillGDBScript(app));

        // txt trace dump
        else
            return new TxtDump(app.getElffile());
    }

    @Override
    public void rawDump() {
        if ((this.prun instanceof GDBRun)) {
            var gdb = (GDBRun) this.prun;
            gdb.runToEnd();
        }
        super.rawDump();
    }

    /*
     * 
     */
    @Override
    public RegisterDump getRegisters() {
        if ((this.prun instanceof GDBRun)) {
            var gdb = (GDBRun) this.prun;
            return gdb.getRegisters();

        } else {
            return super.getRegisters();
        }
    }

    // TODO: add an indicator flag for interactive or non interactive mode?
    @Override
    public Instruction nextInstruction() {
        if ((this.prun instanceof GDBRun)) {
            var gdb = (GDBRun) this.prun;
            gdb.stepi();
            var line = gdb.getAddrAndInstruction().split(":");
            return this.newInstance(line[0], line[1]);

        } else {
            return super.nextInstruction();
        }
    }
}
