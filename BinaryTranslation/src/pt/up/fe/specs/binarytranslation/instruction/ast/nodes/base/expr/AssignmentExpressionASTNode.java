package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;

public class AssignmentExpressionASTNode extends ExpressionASTNode {

    public AssignmentExpressionASTNode(OperandASTNode target, ExpressionASTNode expr) {
        super();
        this.type = InstructionASTNodeType.AssignmentExpressionNode;
        this.addChild(target);
        this.addChild(expr);
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
}
