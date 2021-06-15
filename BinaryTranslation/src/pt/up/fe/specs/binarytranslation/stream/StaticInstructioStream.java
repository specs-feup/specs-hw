package pt.up.fe.specs.binarytranslation.stream;

public interface StaticInstructioStream extends InstructionStream {

    @Override
    default InstructionStreamType getType() {
        return InstructionStreamType.STATIC_ELF;
    }
}
