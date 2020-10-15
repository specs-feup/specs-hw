package pt.up.fe.specs.binarytranslation.stream.replicator;

public interface ObjectStream<T> extends AutoCloseable {

    /*
     * 
     */
    public T next();

    /*
     * 
     */
    public boolean hasNext();

    /*
     * 
     */
    public boolean isClosed();
}
