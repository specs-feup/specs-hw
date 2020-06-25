package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public abstract class OperatorASTNode extends AInstructionASTNode {

    private String operator;

    public OperatorASTNode(String operator) {
        this.operator = operator;
        this.type = InstructionASTNodeType.OperatorNode;
    }

    @Override
    public String getAsString() {
        return this.operator;
    }
}
