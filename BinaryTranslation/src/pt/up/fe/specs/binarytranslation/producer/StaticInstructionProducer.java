package pt.up.fe.specs.binarytranslation.producer;

import java.util.function.BiFunction;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.processes.ObjDump;
import pt.up.fe.specs.binarytranslation.processes.StringProcessRun;
import pt.up.fe.specs.binarytranslation.processes.TxtDump;

public class StaticInstructionProducer extends AInstructionProducer {

    private final static Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    /*
     * Output from GNU based objdump
     */
    public StaticInstructionProducer(Application app,
            BiFunction<String, String, Instruction> produceMethod) {
        super(app, getProperProcess(app), REGEX, produceMethod);
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
