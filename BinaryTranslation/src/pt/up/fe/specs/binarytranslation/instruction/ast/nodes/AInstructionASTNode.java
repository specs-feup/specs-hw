package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public abstract class AInstructionASTNode implements InstructionASTNode {

    protected Boolean isRoot;
    protected InstructionASTNodeType type;

    /*
     * Root node??
     */
    public AInstructionASTNode() {

    }

    public InstructionASTNodeType getType() {
        return type;
    }

    public Boolean getIsRoot() {
        return isRoot;
    }
}
