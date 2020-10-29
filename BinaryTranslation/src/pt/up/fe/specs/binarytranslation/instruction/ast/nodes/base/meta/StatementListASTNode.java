package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.meta;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.StatementASTNode;

public class StatementListASTNode extends InstructionASTNode {

    private StatementListASTNode() {
        super();
        this.type = InstructionASTNodeType.StatementListNode;
    }

    public StatementListASTNode(List<StatementASTNode> statements) {
        this();
        for (var stat : statements)
            this.addChild(stat);
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();

        if (this.getChildren().size() > 1)
            builder.append("{\n");

        for (int i = 1; i < this.getChildren().size(); i++) {
            builder.append(this.getChild(i).getAsString());
        }

        if (this.getChildren().size() > 1)
            builder.append("}\n");

        return builder.toString();
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new StatementListASTNode();
    }
}
