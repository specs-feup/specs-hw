package pt.up.fe.specs.binarytranslation.stream;

public interface TraceInstructionStream extends InstructionStream {

    /**
     * Advance the stream to a given address, returning true if the underlying @InstructionProducer supports this
     * feature
     */
    default boolean advanceTo(long addr) {
        return false;
    }

    @Override
    default InstructionStreamType getType() {
        return InstructionStreamType.TRACE;
    }
}
