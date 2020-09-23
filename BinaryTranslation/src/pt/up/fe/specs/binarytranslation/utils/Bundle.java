package pt.up.fe.specs.binarytranslation.utils;

import java.util.Collection;
import java.util.function.Predicate;

public interface Bundle<T> {

    /*
     * 
     */
    public Collection<T> getPayload();

    /*
     * Returns playload based on predicate
     */
    public Collection<T> getPayload(Predicate<T> predicate);
}
