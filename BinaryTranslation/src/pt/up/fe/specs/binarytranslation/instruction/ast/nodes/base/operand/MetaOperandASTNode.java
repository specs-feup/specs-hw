package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class MetaOperandASTNode extends OperandASTNode {

    /*
     * Name of the implicit field in the processor 
     * architecture that this node represents
     * e.g. the PC, or the PSTATE registers
     */
    private String operandValue;

    public MetaOperandASTNode(String operandValue) {
        super();
        this.operandValue = operandValue;
        this.type = InstructionASTNodeType.MetaFieldNode;
    }

    @Override
    public String getAsString() {
        return this.operandValue;
    }
}
