package pt.up.fe.specs.binarytranslation.stream.multistream;

import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsCheck;

/**
 * Utility class that receives a function capable of consuming input from a stream producer, and runs that function on a
 * thread
 * 
 * @author nuno
 *
 */
public class InstructionStreamConsumer<K> implements Runnable {

    private K consumeResult;
    private InstructionStream istream;

    private final Function<InstructionStream, K> consumeFunction;

    public InstructionStreamConsumer(Function<InstructionStream, K> consumeFunction) {
        this.consumeFunction = consumeFunction;
    }

    public void provide(InstructionStream istream) {
        this.istream = istream;
    }

    /*
     * Threaded workload
     */
    @Override
    public void run() {
        SpecsCheck.checkNotNull(this.istream, () -> "Channel for this consumer object has not been provided!");
        this.consumeResult = this.consumeFunction.apply(this.istream);
    }

    public K getConsumeResult() {
        return consumeResult;
    }
}
