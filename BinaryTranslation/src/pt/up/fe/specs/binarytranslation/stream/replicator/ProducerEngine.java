package pt.up.fe.specs.binarytranslation.stream.replicator;

import java.util.ArrayList;
import java.util.List;

public class ProducerEngine<T, K extends ObjectProducer<T>> {

    /*
     * Original producer (should implement runnable)
     */
    private final ProducerThread<T, K> producer;

    /*
     * Subscribed consumers (should implement runnable)
     */
    private final List<ConsumerThread<T, ?>> consumers;

    public ProducerEngine(ProducerThread<T, K> producer) {

        /*
         * Producer thread
         */
        this.producer = producer;

        /*
         * Consumer threads
         */
        this.consumers = new ArrayList<ConsumerThread<T, ?>>();
    }

    // public

    /*
     * 
     */
    public void subscribe(ConsumerThread<T, ?> consumer) {
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
        var threads = new ArrayList<Thread>();

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
