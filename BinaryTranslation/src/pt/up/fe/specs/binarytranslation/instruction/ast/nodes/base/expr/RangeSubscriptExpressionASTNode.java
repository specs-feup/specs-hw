package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;

public class RangeSubscriptExpressionASTNode extends SubscriptExpressionASTNode {

    private int loidx, hiidx;

    public RangeSubscriptExpressionASTNode(OperandASTNode operand, int loidx, int hiidx) {
        super(operand);
        this.type = InstructionASTNodeType.RangeSubscriptASTNode;
        this.loidx = loidx;
        this.hiidx = hiidx;
    }

    @Override
    public String getAsString() {
        return this.getOperand().getAsString() + "[" + this.loidx + ":" + this.hiidx + "]";
    }
}
