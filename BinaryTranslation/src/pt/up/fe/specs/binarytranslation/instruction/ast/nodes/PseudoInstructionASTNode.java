package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import java.util.List;

public class PseudoInstructionASTNode extends AInstructionASTNode {

    private List<InstructionASTNode> statements;

    public PseudoInstructionASTNode(List<InstructionASTNode> statements) {
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
}
