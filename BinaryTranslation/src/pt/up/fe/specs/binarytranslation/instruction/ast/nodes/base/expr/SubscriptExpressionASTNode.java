package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;

public abstract class SubscriptExpressionASTNode extends ExpressionASTNode {

    public SubscriptExpressionASTNode(OperandASTNode operand) {
        super();
        this.addChild(operand);
    }

    public OperandASTNode getOperand() {
        return (OperandASTNode) this.getChild(0);
    }
}
