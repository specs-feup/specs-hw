package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;

public class ScalarSubscriptExpressionASTNode extends SubscriptExpressionASTNode {

    private int idx;

    public ScalarSubscriptExpressionASTNode(OperandASTNode operand, int idx) {
        super(operand);
        this.type = InstructionASTNodeType.ScalarSubscriptASTNode;
        this.idx = idx;
    }

    @Override
    public String getAsString() {
        return this.getOperand().getAsString() + "[" + this.idx + "]";
    }
}
