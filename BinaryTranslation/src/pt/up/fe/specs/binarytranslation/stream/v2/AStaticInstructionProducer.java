package pt.up.fe.specs.binarytranslation.stream.v2;

import java.io.File;
import java.util.Arrays;

public abstract class AStaticInstructionProducer extends AInstructionProducer {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    /*
     * Output from GNU based objdump
     */
    protected AStaticInstructionProducer(File elfname, String objdumpexe) {
        super(getProperProcess(elfname, objdumpexe));
    }

    private static ProcessBuilder getProperProcess(File elfname, String objdumpexe) {

        var name = elfname.getName();
        var extension = name.subSequence(name.length() - 3, name.length());

        // Output from GNU based objdump
        if (extension.equals("elf"))
            return new ProcessBuilder(Arrays.asList(objdumpexe, "-d", elfname.getAbsolutePath()));

        // Output from file (previous dump)
        else if (IS_WINDOWS)
            return new ProcessBuilder(Arrays.asList("cmd", "/c", "type", elfname.getAbsolutePath()));

        else // if (IS_LINUX)
            return new ProcessBuilder(Arrays.asList("cat", elfname.getAbsolutePath()));
    }
}
