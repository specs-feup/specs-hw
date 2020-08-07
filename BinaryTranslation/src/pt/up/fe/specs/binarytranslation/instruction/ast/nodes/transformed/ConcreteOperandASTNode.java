package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public abstract class ConcreteOperandASTNode extends OperandASTNode {

    /**
     * The {@link Operand} reference from the {@link Instruction} class instance to which the AST of this node is
     * connected to.
     * 
     * This node type is meant to replace an BareOperandASTNode (which has bare information)
     */
    protected Operand op;
    protected InstructionASTNodeType type;

    public ConcreteOperandASTNode(Operand op) {
        super();
        this.op = op;
    }

    @Override
    public String getAsString() {
        return op.getRepresentation();
    }

    public int getWidth() {
        return this.op.getProperties().getWidth();
    }
}
