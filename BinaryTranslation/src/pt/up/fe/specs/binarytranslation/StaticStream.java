package pt.up.fe.specs.binarytranslation;

public interface StaticStream extends InstructionStream {

    @Override
    default InstructionStreamType getType() {
        return InstructionStreamType.STATIC_ELF;
    }
}
