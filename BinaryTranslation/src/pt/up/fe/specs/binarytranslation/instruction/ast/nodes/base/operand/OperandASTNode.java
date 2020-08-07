package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;

public abstract class OperandASTNode extends ExpressionASTNode {

    public OperandASTNode() {
        this.type = InstructionASTNodeType.OperandNode;
    }
}
