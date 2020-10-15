package pt.up.fe.specs.binarytranslation.stream.replicator;

public abstract class AObjectStream<T> implements ObjectStream<T> {

    private boolean inited = false;
    private boolean isClosed = false;
    private T currentT, nextT;
    private T poison;

    public AObjectStream(T poison) {
        this.currentT = null;
        this.nextT = null;
        this.poison = poison;
    }

    /*
     * MUST be implemented by children (e.g., may come from a ConcurrentChannel, or Linestream, etc
     */
    protected abstract T consumeFromProvider();

    private T getNext() {

        if (this.isClosed())
            return null;

        T inst = this.consumeFromProvider();

        // convert poison to null
        if (inst == this.poison) {
            this.close();
            inst = null;
        }

        return inst;
    }

    @Override
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

    @Override
    public boolean hasNext() {
        return this.nextT != null;
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }

    @Override
    public void close() {
        this.isClosed = true;
    }
}
