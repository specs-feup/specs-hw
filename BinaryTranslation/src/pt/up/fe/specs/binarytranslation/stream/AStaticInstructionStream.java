package pt.up.fe.specs.binarytranslation.stream;

import java.io.File;
import java.util.Arrays;

public abstract class AStaticInstructionStream extends AInstructionStream {

    protected AStaticInstructionStream(File elfname, String objdumpexe) {
        super(new ProcessBuilder(Arrays.asList(objdumpexe, "-d", elfname.getAbsolutePath())));
    }

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.STATIC_ELF;
    }
}
