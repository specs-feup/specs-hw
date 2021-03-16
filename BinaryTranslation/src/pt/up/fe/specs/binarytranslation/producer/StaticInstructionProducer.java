package pt.up.fe.specs.binarytranslation.producer;

import java.util.Arrays;
import java.util.function.BiFunction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.providers.ResourceProvider;

public class StaticInstructionProducer extends AInstructionProducer {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    /*
     * Output from GNU based objdump
     */
    public StaticInstructionProducer(Application app, ResourceProvider regex,
            BiFunction<String, String, Instruction> produceMethod) {
        super(app, getProperProcess(app), regex, produceMethod);
    }

    private static ProcessBuilder getProperProcess(Application app) {

        var elfname = app.getElffile();
        var objdumpexe = app.getObjdump();
        var name = elfname.getName();
        var extension = name.subSequence(name.length() - 3, name.length());

        // Output from GNU based objdump
        if (extension.equals("elf"))
            return new ProcessBuilder(Arrays.asList(objdumpexe.getResource(), "-d", elfname.getAbsolutePath()));

        // Output from file (previous dump)
        else if (IS_WINDOWS)
            return new ProcessBuilder(Arrays.asList("cmd", "/c", "type", elfname.getAbsolutePath()));

        else // if (IS_LINUX)
            return new ProcessBuilder(Arrays.asList("cat", elfname.getAbsolutePath()));
    }
}
