package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class BareOperandASTNode extends OperandASTNode {

    /*
     * Name of the ASM field of this operand. This could be anything in the available fields
     * of the instruction encoding, including the immediate value fields such as "IMM"
     */
    private String operandValue;

    public BareOperandASTNode(String operandValue) {
        super();
        this.operandValue = operandValue;
        this.type = InstructionASTNodeType.OperandNode;
    }

    /*
     * Used to replace the ASM field name with a specific register or immediate value (as string)
     */
    public void setOperandName(String operandName) {
        this.operandValue = operandName;
    }

    @Override
    public String getAsString() {
        return this.operandValue;
    }

    public String getOperandValue() {
        return operandValue;
    }
}
