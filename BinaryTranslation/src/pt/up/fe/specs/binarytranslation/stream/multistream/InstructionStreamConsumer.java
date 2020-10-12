package pt.up.fe.specs.binarytranslation.stream.multistream;

import java.util.function.Function;

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
    private InstructionStreamChannel istream;

    // replaces:
    // private ChannelConsumer<Instruction> channel;

    private final Function<InstructionStreamChannel, K> consumeFunction;

    /*public InstructionStreamConsumer(Consumer<ChannelConsumer<Instruction>> streamConsumer) {
        this.streamConsumer = streamConsumer;
    }*/

    public InstructionStreamConsumer(Function<InstructionStreamChannel, K> consumeFunction) {
        // this.channel = producer.newChannel();
        this.consumeFunction = consumeFunction;
    }

    public void provide(InstructionStreamChannel istream) {
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

    /*
     * Fetches the consumer object;
     * This method is used to fetch the detector/profiler/whatever once all input is consumed
     */
    /*public T getConsumer() {
        return this.streamConsumer;
    }*/
}
