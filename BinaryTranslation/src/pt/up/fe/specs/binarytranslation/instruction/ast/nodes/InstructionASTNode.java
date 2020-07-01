package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import pt.up.fe.specs.binarytranslation.utils.TreeNode;

public interface InstructionASTNode extends TreeNode {

    /*
     * 
     */
    public String getAsString();

    /*
     * 
     */
    public InstructionASTNodeType getType();

    /*
     * 
     */
    @Override
    public InstructionASTNode getParent();
}
