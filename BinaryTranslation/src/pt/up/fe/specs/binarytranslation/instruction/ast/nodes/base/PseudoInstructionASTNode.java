package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import java.util.*;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.*;

public class PseudoInstructionASTNode extends InstructionASTNode {

    public PseudoInstructionASTNode(List<AssignmentStatementASTNode> statements) {
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

    public List<AssignmentStatementASTNode> getStatements() {
        var list = new ArrayList<AssignmentStatementASTNode>();
        for (var c : this.getChildren())
            list.add((AssignmentStatementASTNode) c);

        return list;
    }
}
