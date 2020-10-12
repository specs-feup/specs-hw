package pt.up.fe.specs.binarytranslation.stream.multistream;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class InstructionStreamEngine {

    /*
     * Original producer (should implement runnable)
     */
    private final InstructionStreamProducer producer;

    /*
     * Subscribed consumers (should implement runnable)
     */
    private final List<InstructionStreamConsumer<?>> consumers;

    public InstructionStreamEngine(InstructionStream istream) {

        /*
         * Producer thread
         */
        this.producer = new InstructionStreamProducer(istream);

        /*
         * Consumer threads
         */
        this.consumers = new ArrayList<InstructionStreamConsumer<?>>();
    }

    /*
     * 
     */
    public void subscribe(InstructionStreamConsumer<?> consumer) {
        this.consumers.add(consumer);
        consumer.provide(this.producer.newChannel());
    }

    /*
     * 
     
    public void subscribe(Function<ChannelConsumer<Instruction>, ?> consumeFunction) {
        this.consumers.add(new InstructionStreamConsumer(this.producer, consumeFunction));
    }*/

    /*
     * 
     */
    public InstructionStreamConsumer<?> getConsumer(int index) {
        return consumers.get(index);
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
