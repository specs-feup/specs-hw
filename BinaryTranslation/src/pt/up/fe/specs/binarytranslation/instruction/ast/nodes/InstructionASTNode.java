package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import pt.up.fe.specs.binarytranslation.utils.ATreeNode;

public abstract class InstructionASTNode extends ATreeNode<InstructionASTNode> {

    protected InstructionASTNodeType type;

    public InstructionASTNode() {
        super();
    }

    public InstructionASTNodeType getType() {
        return type;
    }

    public abstract String getAsString();

    @Override
    public InstructionASTNode getThis() {
        return this;
    }
}
