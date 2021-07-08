package pt.up.fe.specs.binarytranslation.flow.tree;

import pt.up.fe.specs.util.treenode.ATreeNode;

public abstract class BinaryTranslationStep extends ATreeNode<BinaryTranslationStep> {

    protected BinaryTranslationStepType type;

    public BinaryTranslationStep() {
        super(null);
    }

    public BinaryTranslationStepType getType() {
        return this.type;
    }

    @Override
    public BinaryTranslationStep getThis() {
        return this;
    }

    @Override
    public String toContentString() {
        return this.toString();
    }

    @Override
    protected BinaryTranslationStep copyPrivate() {
        // TODO Auto-generated method stub
        return null;
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
