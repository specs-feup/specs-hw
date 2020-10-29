package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;

public class ScalarSubscriptExpressionASTNode extends SubscriptExpressionASTNode {

    private int idx;

    // TODO: make idx a type of node
    private ScalarSubscriptExpressionASTNode(int idx) {
        super(InstructionASTNodeType.ScalarSubscriptASTNode);
        this.idx = idx;
    }

    public ScalarSubscriptExpressionASTNode(OperandASTNode operand, int idx) {
        super(InstructionASTNodeType.ScalarSubscriptASTNode, operand);
        this.idx = idx;
    }

    @Override
    public String getAsString() {
        return this.getOperand().getAsString() + "[" + this.idx + "]";
    }

    @Override
    public OperandASTNode getOperand() {
        return (OperandASTNode) this.getChild(0);
    }

    public int getIdx() {
        return idx;
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new ScalarSubscriptExpressionASTNode(this.getIdx());
    }
}
