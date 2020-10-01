package pt.up.fe.specs.binarytranslation.flow.tree;

import pt.up.fe.specs.binarytranslation.utils.ATreeNode;

public abstract class BinaryTranslationStep extends ATreeNode<BinaryTranslationStep> {

    protected BinaryTranslationStepType type;

    public BinaryTranslationStep() {
        super();
    }

    public BinaryTranslationStepType getType() {
        return this.type;
    }

    @Override
    public BinaryTranslationStep getThis() {
        return this;
    }

    /*
     * Execute this step (Default visits kids)
     */
    public void execute() {
        for (var c : this.getChildren()) {
            c.execute();
        }
    };
}
