package pt.up.fe.specs.binarytranslation.stream.replicator;

public interface ObjectProducer<T> {

    /*
     * Should return something like an enum implementing T, 
     * which represents a poison value to inject into an ObjectStream
     */
    T getPoison();
}
