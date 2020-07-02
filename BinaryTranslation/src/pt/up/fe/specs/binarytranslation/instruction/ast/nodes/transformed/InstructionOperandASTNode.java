package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public abstract class InstructionOperandASTNode extends OperandASTNode {

    /**
     * The {@link Operand} reference from the {@link Instruction} class instance to which the AST of this node is
     * connected to.
     * 
     * This node type is meant to replace an OperandASTNode (which has bare information)
     */
    protected Operand op;
    protected InstructionASTNodeType type;

    public InstructionOperandASTNode(Operand op) {
        super(op.getStringValue());
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
