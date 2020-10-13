package pt.up.fe.specs.binarytranslation.stream.replicator;

import java.util.function.Function;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

/**
 * 
 * @author nuno
 *
 * @param <T>
 *            Type of input object from ObjectStream
 * @param <K>
 *            Type of consumption output
 */
public class ConsumerThread<T, K> implements Runnable {

    private K consumeResult;
    private ChannelConsumer<T> ostream;
    private final Function<ChannelConsumer<T>, K> consumeFunction;

    public ConsumerThread(Function<ChannelConsumer<T>, K> consumeFunction) {
        this.consumeFunction = consumeFunction;
    }

    public void provide(ChannelConsumer<T> ostream) {
        this.ostream = ostream;
    }

    /*
     * Threaded workload
     */
    @Override
    public void run() {
        SpecsCheck.checkNotNull(this.ostream, () -> "Channel for this consumer object has not been provided!");
        this.consumeResult = this.consumeFunction.apply(this.ostream);
    }

    public K getConsumeResult() {
        return consumeResult;
    }
}
