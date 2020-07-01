package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import java.util.*;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.*;

public class PseudoInstructionASTNode extends InstructionASTNode {

    public PseudoInstructionASTNode(List<StatementASTNode> statements) {
        super();
        this.type = InstructionASTNodeType.PseudoInstructionNode;
        for (var s : statements)
            this.addChild(s);
    }

    @Override
    public String getAsString() {
        String ret = "";
        for (var s : this.getStatements()) {
            ret += s.getAsString() + "\n";
        }
        ret += "\n";
        return ret;
    }

    public List<StatementASTNode> getStatements() {
        var list = new ArrayList<StatementASTNode>();
        for (var c : this.getChildren())
            list.add((StatementASTNode) c);

        return list;
    }
}
