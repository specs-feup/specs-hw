package pt.up.fe.specs.binarytranslation.stream.replicator;

// TODO: promote to class <T>?

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
