package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class AssignmentStatementASTNode extends StatementASTNode {

    public AssignmentStatementASTNode(OperandASTNode target, ExpressionASTNode expr) {
        super();
        this.type = InstructionASTNodeType.AssignmentStatementNode;
        this.addChild(target);
        this.addChild(expr);
    }

    @Override
    public String getAsString() {
        return this.getTarget().getAsString() + " = " + this.getExpr().getAsString() + ";";
    }

    public OperandASTNode getTarget() {
        return (OperandASTNode) this.getChild(0);
    }

    public ExpressionASTNode getExpr() {
        return (ExpressionASTNode) this.getChild(1);
    }
}
