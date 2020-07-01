package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.utils.ATreeNode;

public abstract class AInstructionASTNode extends ATreeNode implements InstructionASTNode {

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
}
