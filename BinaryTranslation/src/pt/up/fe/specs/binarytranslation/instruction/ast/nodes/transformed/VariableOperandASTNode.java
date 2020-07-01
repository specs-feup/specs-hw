package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class VariableOperandASTNode extends InstructionOperandASTNode implements ExpressionASTNode {

    /*
     * Nodes of this type represent operands which could be: 
     * "r6" or "imma", i.e., some fields are immediate value fields, but treated as inputs to module
     */
    public VariableOperandASTNode(Operand op) {
        super(op);
        this.type = InstructionASTNodeType.VariableNode;
    }
}
