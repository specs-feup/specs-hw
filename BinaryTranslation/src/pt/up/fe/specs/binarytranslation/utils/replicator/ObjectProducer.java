package pt.up.fe.specs.binarytranslation.utils.replicator;

public interface ObjectProducer<T> extends AutoCloseable {

    /*
     * 
     */
    default T getPoison() {
        return null;
    };
}
