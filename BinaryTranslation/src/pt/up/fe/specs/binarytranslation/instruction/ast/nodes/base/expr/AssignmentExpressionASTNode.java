package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNodeSide;

public class AssignmentExpressionASTNode extends ExpressionASTNode {

    private AssignmentExpressionASTNode() {
        super(InstructionASTNodeType.AssignmentExpressionNode);
    }

    public AssignmentExpressionASTNode(OperandASTNode target, ExpressionASTNode expr) {
        this();
        this.addChild(target);
        this.addChild(expr);
        target.setSide(OperandASTNodeSide.LeftHandSide);
    }

    @Override
    public String getAsString() {
        return this.getTarget().getAsString() + " = " + this.getExpr().getAsString();
    }

    public OperandASTNode getTarget() {
        return (OperandASTNode) this.getChild(0);
    }

    public ExpressionASTNode getExpr() {
        return (ExpressionASTNode) this.getChild(1);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new AssignmentExpressionASTNode();
    }
}
