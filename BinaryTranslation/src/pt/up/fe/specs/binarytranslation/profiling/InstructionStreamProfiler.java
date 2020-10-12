package pt.up.fe.specs.binarytranslation.profiling;

import java.util.concurrent.Callable;

import pt.up.fe.specs.binarytranslation.profiling.data.InstructionProfileResult;

public interface InstructionStreamProfiler extends Callable<InstructionProfileResult> { // extends
                                                                                        // InstructionStreamConsumer {

    /*
     * Run the profiling over the stream
     */
    public InstructionProfileResult profile();
}
