package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;

public class PlainStatementASTNode extends StatementASTNode {

    public PlainStatementASTNode(ExpressionASTNode expr) {
        super();
        this.type = InstructionASTNodeType.PlainStatementNode;
        this.addChild(expr);
    }

    @Override
    public String getAsString() {
        return this.getExpr().getAsString() + ";";
    }

    public ExpressionASTNode getExpr() {
        return (ExpressionASTNode) this.getChild(0);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new PlainStatementASTNode(this.getExpr());
    }
}
