package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;

public class PlainStatementASTNode extends StatementASTNode {

    private PlainStatementASTNode() {
        super(InstructionASTNodeType.PlainStatementNode);
    }

    public PlainStatementASTNode(ExpressionASTNode expr) {
        this();
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
        return new PlainStatementASTNode();
    }
}
