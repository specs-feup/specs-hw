package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class RangeSubscriptOperandASTNode extends SubscriptOperandASTNode {

    private int loidx, hiidx;

    // TODO: make idx a type of node
    private RangeSubscriptOperandASTNode(int loidx, int hiidx) {
        super(InstructionASTNodeType.RangeSubscriptASTNode);
        this.loidx = loidx;
        this.hiidx = hiidx;
    }

    public RangeSubscriptOperandASTNode(OperandASTNode operand, int loidx, int hiidx) {
        super(InstructionASTNodeType.RangeSubscriptASTNode, operand);
        this.loidx = loidx;
        this.hiidx = hiidx;
    }

    @Override
    public String getAsString() {
        return this.getOperand().getAsString() + "[" + this.loidx + ":" + this.hiidx + "]";
    }

    @Override
    public OperandASTNode getOperand() {
        return (OperandASTNode) this.getChild(0);
    }

    public int getLoidx() {
        return loidx;
    }

    public int getHiidx() {
        return hiidx;
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new RangeSubscriptOperandASTNode(this.getLoidx(), this.getHiidx());
    }
}
