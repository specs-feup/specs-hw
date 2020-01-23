package pt.up.fe.specs.binarytranslation;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum BinaryTranslationResource implements ResourceProvider {

    DOTTY_BINARY("dot");

    private final String resource;

    private BinaryTranslationResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
