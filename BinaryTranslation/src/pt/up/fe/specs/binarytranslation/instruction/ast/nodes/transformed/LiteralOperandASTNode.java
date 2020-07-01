package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class LiteralOperandASTNode extends InstructionOperandASTNode {

    /*
     * This class represents ONLY operands which are literal values (i.e., numbers)
     */
    public LiteralOperandASTNode(Operand op) {
        super(op);
        this.type = InstructionASTNodeType.LiteralNode;
    }

    public Number getValue() {
        return this.op.getValue();
    }
}
