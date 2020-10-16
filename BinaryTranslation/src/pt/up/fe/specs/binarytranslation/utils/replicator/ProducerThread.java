package pt.up.fe.specs.binarytranslation.utils.replicator;

import java.util.ArrayList;
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
public class ProducerThread<T, K extends ObjectProducer<T>> implements Runnable {

    /*
     * Source producer function
     */
    private final K producer;

    /*
     * 
     */
    private final Function<K, T> produceFunction;

    /*
     * "Constructor" for stream objects to feed to consumers
     */
    private final Function<ChannelConsumer<T>, ObjectStream<T>> cons;

    /*
     * Variable number of channels to feed consumers
     */
    private List<ChannelProducer<T>> producers;

    protected ProducerThread(K producer, Function<K, T> produceFunction) {
        this(producer, produceFunction,
                cc -> new GenericObjectStream<T>(cc, producer.getPoison()));
    }

    protected ProducerThread(K producer, Function<K, T> produceFunction,
            Function<ChannelConsumer<T>, ObjectStream<T>> cons) {
        this.producer = producer;
        this.produceFunction = produceFunction;
        this.cons = cons;
        this.producers = new ArrayList<ChannelProducer<T>>();
    }

    /*
     * creates a new channel into which this runnable object will pump data, with depth 1
     */
    protected ObjectStream<T> newChannel() {
        return this.newChannel(1);
    }

    /*
     * creates a new channel into which this runnable object will pump data
     */
    protected ObjectStream<T> newChannel(int depth) {

        /*
         * Need new channel
         */
        var channel = new ConcurrentChannel<T>(depth);
        this.producers.add(channel.createProducer());

        /*
         * Give channel consumer object to consumer?
         */
        return this.cons.apply(channel.createConsumer());
    }

    /*
     * ChannelProducer returns false immediately if fail to insert, so repeat
     */
    private void insertToken(ChannelProducer<T> prod, T inst) {
        while (!prod.offer(inst))
            ;
    }

    /*
     * Thread workload (putting objects into blocking channels)
     */
    @Override
    public void run() {

        /*
         * Warning: "null" cannot be inserted into a ChannelProducer / ConcurrentChannel
         */
        T nextproduct = null;
        while ((nextproduct = this.produceFunction.apply(this.producer)) != null) {
            for (var producer : this.producers) {
                this.insertToken(producer, nextproduct);
            }
        }

        // insert poison terminator to all channels
        for (var producer : this.producers) {
            this.insertToken(producer, this.producer.getPoison());
        }
    }
}
