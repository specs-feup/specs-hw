package pt.up.fe.specs.binarytranslation.stream.replicator;

import java.util.List;
import java.util.function.Function;

import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelProducer;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;

/**
 * 
 * @author nuno
 *
 * @param <T>
 *            Type of produced object
 * @param <K>
 *            Type of producer object
 */
public class ProducerThread<T, K extends AutoCloseable> implements Runnable, AutoCloseable {

    /*
     * Source producer function
     */
    private final K producer;
    private final Function<K, T> produceFunction;

    /*
     * Variable number of producers to pump data into
     */
    private List<ChannelProducer<T>> producers;

    public ProducerThread(K producer, Function<K, T> produceFunction) {
        this.producer = producer;
        this.produceFunction = produceFunction;
    }

    /*
     * creates a new channel into which this runnable object will pump data, with depth 1
     */
    public ChannelConsumer<T> newChannel() {
        return this.newChannel(1);
    }

    /*
     * creates a new channel into which this runnable object will pump data
     */
    public ChannelConsumer<T> newChannel(int depth) {

        /*
         * Need new channel
         */
        var channel = new ConcurrentChannel<T>(depth);
        this.producers.add(channel.createProducer());

        /*
         * Give channel consumer object to consumer?
         */
        return channel.createConsumer();
    }

    /*
     * Thread workload (putting objects into blocking channels)
     */
    @Override
    public void run() {

        T nextproduct = null;
        while ((nextproduct = this.produceFunction.apply(this.producer)) != null) {
            for (var producer : this.producers) {
                producer.offer(nextproduct);
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.producer.close();
    }
}
