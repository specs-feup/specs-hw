package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;

public abstract class OperandASTNode extends ExpressionASTNode {

    protected OperandASTNodeSide side;

    public OperandASTNode(InstructionASTNodeType type) {
        super(type);
        this.side = OperandASTNodeSide.RightHandSide;
        // defaults to RHS, unless the operand comes from an AssignmentExpressionASTNode
    }

    public void setSide(OperandASTNodeSide side) {
        this.side = side;
    }

    public OperandASTNodeSide getSide() {
        return side;
    }
}
