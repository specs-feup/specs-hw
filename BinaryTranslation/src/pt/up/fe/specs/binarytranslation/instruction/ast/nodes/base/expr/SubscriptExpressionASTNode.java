package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;

public abstract class SubscriptExpressionASTNode extends ExpressionASTNode {

    protected SubscriptExpressionASTNode(InstructionASTNodeType type) {
        super(type);
    }

    protected SubscriptExpressionASTNode(InstructionASTNodeType type, OperandASTNode operand) {
        super(type);
        this.addChild(operand);
    }

    public OperandASTNode getOperand() {
        return (OperandASTNode) this.getChild(0);
    }
}
