package pt.up.fe.specs.binarytranslation;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum NullResource implements ResourceProvider {

    nullResource;

    @Override
    public String getResource() {
        return null;
    }
}
