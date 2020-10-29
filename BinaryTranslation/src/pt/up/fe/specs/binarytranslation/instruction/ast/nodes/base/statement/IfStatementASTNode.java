package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.meta.StatementListASTNode;

public class IfStatementASTNode extends StatementASTNode {

    /*
     * Super call
     */
    protected IfStatementASTNode(InstructionASTNodeType type) {
        super(type);
    }

    /*
     * Normal public constructor
     */
    public IfStatementASTNode(ExpressionASTNode condition,
            List<StatementASTNode> statements) {
        this(InstructionASTNodeType.IfStatementNode, condition, statements);
    }

    /*
     * Supports either IfStatementASTNode or IfElseStatementASTNode construction
     */
    protected IfStatementASTNode(InstructionASTNodeType type, ExpressionASTNode condition,
            List<StatementASTNode> statements) {

        // base constructor
        this(type);

        // condition
        this.addChild(condition);

        // if clauses
        this.addChild(new StatementListASTNode(statements));
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("if (" + this.getChild(0).getAsString() + ") ");
        builder.append(this.getStatements().getAsString());
        return builder.toString();
    }

    public ExpressionASTNode getCondition() {
        return (ExpressionASTNode) this.getChild(0);
    }

    public StatementListASTNode getStatements() {
        return (StatementListASTNode) this.getChild(1);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new IfStatementASTNode(InstructionASTNodeType.IfStatementNode);
    }
}
