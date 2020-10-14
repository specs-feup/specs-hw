package pt.up.fe.specs.binarytranslation.stream.replicator;

import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class ObjectStream<T> implements AutoCloseable {

    private boolean inited = false;
    private boolean isClosed = false;
    private final ChannelConsumer<T> channel;
    private T currentT, nextT;
    private T poison;

    public ObjectStream(ChannelConsumer<T> channel, T poison) {
        this.channel = channel;
        this.currentT = null;
        this.nextT = null;
    }

    private T getNext() {

        if (this.isClosed())
            return null;

        T inst = null;
        try {
            inst = this.channel.take();

            // convert poison to null
            if (inst == this.poison) {
                this.close();
                inst = null;
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return inst;
    }

    public T next() {

        /*
         * First call of getNext is done here instead of the constructor, since 
         * the channel may block if this ObjectStream is used (as it should) 
         * to read from a ChannelProducer<T> which executes in another thread
         * which may not have yet been launched
         */
        if (this.inited == false) {
            this.nextT = this.getNext();
            this.inited = true;
        }

        if (this.nextT == null) {
            return null;
        }

        this.currentT = this.nextT;
        this.nextT = this.getNext();
        return this.currentT;
    }

    public boolean hasNext() {
        return this.nextT != null;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    @Override
    public void close() {
        this.isClosed = true;
    }
}
