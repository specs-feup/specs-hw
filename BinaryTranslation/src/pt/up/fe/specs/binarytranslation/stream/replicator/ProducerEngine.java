package pt.up.fe.specs.binarytranslation.stream.replicator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

/**
 * 
 * @author nuno
 *
 * @param <T>
 *            Type of produced object
 * @param <K>
 *            Type of producer object
 */
public class ProducerEngine<T, K extends ObjectProducer<T>> {

    /*
     * Original producer (should implement runnable)
     */
    private final ProducerThread<T, K> producer;

    /*
     * Subscribed consumers (should implement runnable)
     */
    private final List<ConsumerThread<T, ?>> consumers;

    public ProducerEngine(K producer, Function<K, T> produceFunction) {
        this(new ProducerThread<T, K>(producer, produceFunction));
    }

    public ProducerEngine(K producer, Function<K, T> produceFunction,
            Function<ChannelConsumer<T>, ObjectStream<T>> cons) {
        this(new ProducerThread<T, K>(producer, produceFunction, cons));
    }

    private ProducerEngine(ProducerThread<T, K> producer) {
        this.producer = producer;
        this.consumers = new ArrayList<ConsumerThread<T, ?>>();
    }

    /*
     * 
     */
    public ConsumerThread<T, ?> subscribe(Function<ObjectStream<T>, ?> consumeFunction) {
        var thread = new ConsumerThread<>(consumeFunction);
        this.subscribe(thread);
        return thread;
    }

    /*
     * 
     */
    private void subscribe(ConsumerThread<T, ?> consumer) {
        this.consumers.add(consumer);
        consumer.provide(this.producer.newChannel());
    }

    /**
     * Launches all threads
     */
    public void launch() {

        /*
         * Thread list
         */
        var<Thread> threads = new ArrayList<Thread>();

        /*
         * One produce thread
         */
        var produceThread = new Thread(this.producer);
        threads.add(produceThread);
        produceThread.start();

        /*
         * N consumers (profilers, detectors, etc)
         */
        for (var consumer : this.consumers) {
            var consumeThread = new Thread(consumer);
            threads.add(consumeThread);
            consumeThread.start();
        }

        /*
         * Wait for all
         */
        for (var thread : threads)
            try {
                thread.join();

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}
