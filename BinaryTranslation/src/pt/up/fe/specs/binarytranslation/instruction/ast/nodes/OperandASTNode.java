package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public class OperandASTNode extends AInstructionASTNode implements ExpressionASTNode {

    private String operandName;

    public OperandASTNode(String operandName) {
        super();
        this.operandName = operandName;
        this.type = InstructionASTNodeType.OperandNode;
    }

    @Override
    public String getAsString() {
        return this.operandName;
    }

    public String getOperandName() {
        return operandName;
    }

    /*
     * Used to replace the ASM field name with a specific register/value string
     */
    public void setOperandName(String operandName) {
        this.operandName = operandName;
    }
}
