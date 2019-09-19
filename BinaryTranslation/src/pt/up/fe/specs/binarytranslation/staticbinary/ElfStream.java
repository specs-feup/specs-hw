package pt.up.fe.specs.binarytranslation.staticbinary;

import pt.up.fe.specs.binarytranslation.InstructionStream;
import pt.up.fe.specs.binarytranslation.InstructionStreamType;

public interface ElfStream extends InstructionStream {

    @Override
    default InstructionStreamType getType() {
        return InstructionStreamType.STATIC_ELF;
    }
}
