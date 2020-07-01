package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import java.util.List;

public interface InstructionASTNode {

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
    public List<InstructionASTNode> getChildren();

    /*
     * 
     */
    public void replaceChild(InstructionASTNode oldChild, InstructionASTNode newChild);

    /*
     * 
     */
    public InstructionASTNode getParent();
}
