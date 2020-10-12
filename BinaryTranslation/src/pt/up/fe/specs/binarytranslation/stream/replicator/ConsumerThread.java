package pt.up.fe.specs.binarytranslation.stream.replicator;

import java.util.function.Function;

public class ConsumerThread<T, K extends AutoCloseable> implements Runnable {

    public ConsumerThread(Function<K, T> consumeFunction) {
        // this.channel = producer.newChannel();
        this.consumeFunction = consumeFunction;
    }
}
