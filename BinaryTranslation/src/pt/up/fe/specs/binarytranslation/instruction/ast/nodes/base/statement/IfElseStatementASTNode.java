package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.meta.StatementListASTNode;

public class IfElseStatementASTNode extends IfStatementASTNode {

    private IfElseStatementASTNode() {
        super(InstructionASTNodeType.IfElseStatementNode);
    }

    public IfElseStatementASTNode(ExpressionASTNode condition, List<StatementASTNode> statements,
            List<StatementASTNode> elsestatements) {

        // super
        super(InstructionASTNodeType.IfElseStatementNode, condition, statements);

        // else statements
        this.addChild(new StatementListASTNode(elsestatements));
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("if (" + this.getChild(0).getAsString() + ") ");
        builder.append(this.getStatements().getAsString());
        builder.append(this.getElseStatements().getAsString());
        return builder.toString();
    }

    public StatementListASTNode getElseStatements() {
        return (StatementListASTNode) this.getChild(2);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new IfElseStatementASTNode();
    }
}
