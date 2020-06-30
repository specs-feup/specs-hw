package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public class OperandASTNode extends AInstructionASTNode implements ExpressionASTNode {

    private Number operandValue;
    private String operandName;

    public OperandASTNode(String operandName) {
        super();
        this.operandName = operandName;
        this.operandValue = null;
        this.type = InstructionASTNodeType.AsmFieldNode;
    }

    /*
     * TODO: this is NEVER CALLED during AST construction...
     */
    public OperandASTNode(Number immediateValue) {
        super();
        this.operandValue = immediateValue;
        this.operandName = immediateValue.toString();
        this.type = InstructionASTNodeType.LiteralNode;
    }

    /*
     * Used to replace the ASM field name with a specific register
     */
    public void setOperandName(String operandName) {
        this.operandName = operandName;
    }

    /*
     * Used to replace the ASM field name with a specific value string
     */
    public void setOperandName(Number immediateValue) {
        this.operandValue = immediateValue;
        this.operandName = immediateValue.toString();
    }

    @Override
    public String getAsString() {
        return this.operandName;
    }

    public String getOperandName() {
        return operandName;
    }

    public Number getOperandValue() {
        return operandValue;
    }
}
