package pt.up.fe.specs.binarytranslation.producer;

import java.util.function.BiFunction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.processes.ObjDump;
import pt.up.fe.specs.binarytranslation.processes.StringProcessRun;
import pt.up.fe.specs.binarytranslation.processes.TxtDump;
import pt.up.fe.specs.util.providers.ResourceProvider;

public class StaticInstructionProducer extends AInstructionProducer {

    /*
     * Output from GNU based objdump
     */
    public StaticInstructionProducer(Application app, ResourceProvider regex,
            BiFunction<String, String, Instruction> produceMethod) {
        super(app, getProperProcess(app), regex, produceMethod);
    }

    private static StringProcessRun getProperProcess(Application app) {

        var name = app.getElffile().getName();
        var extension = name.subSequence(name.length() - 3, name.length());

        // Output from GNU based objdump
        if (extension.equals("elf"))
            return new ObjDump(app);

        // txt dump
        else
            return new TxtDump(app.getElffile());
    }
}
