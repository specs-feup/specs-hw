package pt.up.fe.specs.binarytranslation.profiling;

import pt.up.fe.specs.binarytranslation.profiling.data.InstructionProfileResult;

public interface InstructionStreamProfiler { // extends
                                             // InstructionStreamConsumer {

    /*
     * Run the profiling over the stream
     */
    public InstructionProfileResult profile();
}
