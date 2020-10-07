package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public abstract class ConcreteOperandASTNode extends OperandASTNode {

    /**
     * The {@Operand} reference from the {@Instruction} class instance to which the AST of this node is connected to.
     * 
     * This node type is meant to replace an BareOperandASTNode (which has bare information)
     */
    protected Operand op;

    protected ConcreteOperandASTNode(InstructionASTNodeType type, Operand op) {
        super(type);
        this.op = op;
        // TODO: do we want this to be a copy??
    }

    @Override
    public String getAsString() {
        return op.getRepresentation();
    }

    public int getWidth() {
        return this.op.getProperties().getWidth();
    }

    public Operand getInstructionOperand() {
        return op;
    }
}
