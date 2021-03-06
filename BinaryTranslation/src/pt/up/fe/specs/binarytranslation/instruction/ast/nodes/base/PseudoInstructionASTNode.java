package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement.StatementASTNode;

public class PseudoInstructionASTNode extends InstructionASTNode {

    private PseudoInstructionASTNode() {
        this.type = InstructionASTNodeType.PseudoInstructionNode;
    }

    public PseudoInstructionASTNode(List<StatementASTNode> statements) {
        this();
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

    @Override
    protected InstructionASTNode copyPrivate() {
        return new PseudoInstructionASTNode();
    }
}
