package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import java.util.ArrayList;
import java.util.List;

public abstract class AInstructionASTNode implements InstructionASTNode {

    protected InstructionASTNodeType type;
    protected InstructionASTNode parent;
    protected List<InstructionASTNode> children;

    public AInstructionASTNode() {
        this.children = new ArrayList<InstructionASTNode>();
    }

    @Override
    public InstructionASTNodeType getType() {
        return type;
    }

    @Override
    public List<InstructionASTNode> getChildren() {
        return this.children;
    }

    @Override
    public void replaceChild(InstructionASTNode oldChild, InstructionASTNode newChild) {
        for (var c : this.getChildren()) {
            if (c == oldChild) {
                c = newChild;
                return;
            }
        }
    }

    @Override
    public InstructionASTNode getParent() {
        return parent;
    }
}
