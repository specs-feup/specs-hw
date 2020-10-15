package pt.up.fe.specs.binarytranslation.stream.replicator;

public interface ObjectProducer<T> extends AutoCloseable {

    /*
     * 
     */
    default T getPoison() {
        return null;
    };
}
