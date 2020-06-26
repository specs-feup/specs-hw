package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public class OperandASTNode extends AInstructionASTNode {

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
}
