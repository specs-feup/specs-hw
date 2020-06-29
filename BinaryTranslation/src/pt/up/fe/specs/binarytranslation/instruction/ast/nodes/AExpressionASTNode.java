package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.VariableReference;

public abstract class AExpressionASTNode extends AInstructionASTNode implements ExpressionASTNode {

    protected VariableReference resultName;
    protected InstructionASTNode right;
    protected OperatorASTNode operator;

    public AExpressionASTNode(OperatorASTNode operator, InstructionASTNode right) {
        super();
        this.right = right;
        this.operator = operator;
    }

    @Override
    public OperatorASTNode getOperator() {
        return operator;
    }

    @Override
    public InstructionASTNode getRight() {
        return right;
    }

    @Override
    public InstructionASTNode getLeft() {
        return null;
    }

    @Override
    public void setResultName(String resultName) {
        this.resultName = new VariableReference(resultName);
    }

    @Override
    public VariableReference getResultName() {
        return resultName;
    }
}
