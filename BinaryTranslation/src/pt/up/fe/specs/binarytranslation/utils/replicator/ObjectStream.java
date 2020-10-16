package pt.up.fe.specs.binarytranslation.utils.replicator;

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
