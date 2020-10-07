package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.ExpressionASTNode;

public class IfStatementASTNode extends StatementASTNode {

    public IfStatementASTNode(ExpressionASTNode condition, List<StatementASTNode> statements) {
        super();
        this.type = InstructionASTNodeType.IfStatementNode;
        this.addChild(condition);
        for (var stat : statements)
            this.addChild(stat);
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

    public ExpressionASTNode getCondition() {
        return (ExpressionASTNode) this.getChild(0);
    }

    public List<StatementASTNode> getStatements() {
        return this.getChildrenOf(StatementASTNode.class);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new IfStatementASTNode(this.getCondition(), this.getStatements());
    }
}
