package pt.up.fe.specs.binarytranslation.flow.tree;

public class BinaryTranslationAnchorNode extends BinaryTranslationStep {

    public BinaryTranslationAnchorNode() {
        super();
    }

    @Override
    protected BinaryTranslationStep copyPrivate() {
        return new BinaryTranslationAnchorNode();
    }
}
