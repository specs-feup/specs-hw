package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.VariableReference;

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
    @Override
    public void setResultName(String resultName) {
        this.operandName = resultName;
    }

    @Override
    public VariableReference getResultName() {
        return new VariableReference(this.operandName);
    }

}
