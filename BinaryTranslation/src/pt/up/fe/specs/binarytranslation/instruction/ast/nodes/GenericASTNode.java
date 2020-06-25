package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

/**
 * Generic Node class, which simply holds the node content as a string (used for AST generation testing)
 * 
 * @author nuno
 *
 */
public class GenericASTNode extends AInstructionASTNode {

    private String payload;

    public GenericASTNode(String payload, AInstructionASTNode left, AInstructionASTNode right) {
        this.type = InstructionASTNodeType.Generic;
        this.payload = payload;
    }

    public GenericASTNode(String payload, AInstructionASTNode left) {
        this(payload, left, null);
    }

    @Override
    public String getAsString() {
        return this.payload;
    }
}
