package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public abstract class SubscriptOperandASTNode extends OperandASTNode {

    protected SubscriptOperandASTNode(InstructionASTNodeType type) {
        super(type);
    }

    protected SubscriptOperandASTNode(InstructionASTNodeType type, OperandASTNode operand) {
        super(type);
        this.addChild(operand);
    }

    public OperandASTNode getOperand() {
        return (OperandASTNode) this.getChild(0);
    }
}
