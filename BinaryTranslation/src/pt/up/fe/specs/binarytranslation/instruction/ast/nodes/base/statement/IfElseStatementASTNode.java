package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;

public class IfElseStatementASTNode extends IfStatementASTNode {

    public IfElseStatementASTNode(ExpressionASTNode condition, List<StatementASTNode> statements,
            List<StatementASTNode> elsestatements) {
        super(condition, statements);
        this.type = InstructionASTNodeType.IfElseStatementNode;
        for (var estat : elsestatements)
            this.addChild(estat);
    }

    @Override
    public String getAsString() {
        String ret = "if (" + this.getChild(0).getAsString() + ") ";
        if (this.getChildren().size() > 1)
            ret += "{\n";

        for (int i = 1; i < this.getChildren().size(); i++) {
            var child = this.getChild(i);
            ret += child.getAsString();
        }

        if (this.getChildren().size() > 1)
            ret += "}\n";

        return ret;
    }

    @Override
    public ExpressionASTNode getCondition() {
        return (ExpressionASTNode) this.getChild(0);
    }
}
