package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.AInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

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
