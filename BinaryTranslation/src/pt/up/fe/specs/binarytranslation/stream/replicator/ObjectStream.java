package pt.up.fe.specs.binarytranslation.stream.replicator;

public interface ObjectStream<T> extends AutoCloseable {

    /**
     * 
     * @return the next instruction of the stream, or null if there are no more instructions in the stream
     */
    T next();

    /**
     * 
     * @return True if stream has another line
     */
    boolean hasNext();
}
