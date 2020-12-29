package pt.up.fe.specs.binarytranslation.profiling;

import pt.up.fe.specs.binarytranslation.profiling.data.InstructionProfileResult;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public interface InstructionStreamProfiler { // extends
                                             // InstructionStreamConsumer {

    /*
     * Run the profiling over the stream
     */
    public InstructionProfileResult profile(InstructionStream istream);

    /*
     * 
     */
    public void setStartAddr(Number startAddr);

    /*
     * 
     */
    public void setStopAddr(Number stopAddr);

    /*
     * 
     */
    public Number getProfileTime();
}
