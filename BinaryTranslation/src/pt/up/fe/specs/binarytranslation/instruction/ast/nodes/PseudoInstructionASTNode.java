package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import java.util.List;

public class PseudoInstructionASTNode extends AInstructionASTNode {

    private List<StatementASTNode> statements;

    public PseudoInstructionASTNode(List<StatementASTNode> statements) {
        super();
        this.statements = statements;
        this.type = InstructionASTNodeType.PseudoInstructionNode;
        this.children.addAll(this.statements);
    }

    @Override
    public String getAsString() {
        String ret = "";
        for (var s : this.statements) {
            ret += s.getAsString() + "\n";
        }
        ret += "\n";
        return ret;
    }

    public List<StatementASTNode> getStatements() {
        return statements;
    }
}
