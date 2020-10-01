package pt.up.fe.specs.binarytranslation.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public abstract class ABundle<T> implements Bundle<T> {

    public ABundle() {
        // TODO Auto-generated constructor stub
    }

    /*    @Override
    public Collection<T> getPayload() {
    
    }*/

    @Override
    public Collection<T> getPayload(Predicate<T> predicate) {

        var list = new ArrayList<T>();
        for (var element : this.getPayload()) {
            if (predicate.test(element))
                list.add(element);
        }
        return list;
    }
}
