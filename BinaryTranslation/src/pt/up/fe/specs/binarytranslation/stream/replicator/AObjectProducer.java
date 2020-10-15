package pt.up.fe.specs.binarytranslation.stream.replicator;

import pt.up.fe.specs.util.collections.concurrentchannel.ChannelProducer;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;

public abstract class AObjectProducer<T> implements ObjectProducer<T> {

    private ConcurrentChannel<T> channel;
    private ChannelProducer<T> producer;

    public AObjectProducer() {
        this(1);
    }

    public AObjectProducer(int capacity) {
        this.channel = new ConcurrentChannel<T>(capacity);
        this.producer = this.channel.createProducer();
    }

    /*
    @Override
    public T getObject() {
        return this.
    }*/

    @Override
    public T getPoison() {
        // TODO Auto-generated method stub
        return null;
    }
}
