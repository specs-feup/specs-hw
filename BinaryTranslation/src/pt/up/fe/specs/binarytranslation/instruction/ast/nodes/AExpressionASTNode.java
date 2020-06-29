package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public abstract class AExpressionASTNode extends AInstructionASTNode implements ExpressionASTNode {

    protected ExpressionASTNode right;
    protected OperatorASTNode operator;

    public AExpressionASTNode(OperatorASTNode operator, ExpressionASTNode right) {
        super();
        this.right = right;
        this.operator = operator;
    }

    @Override
    public OperatorASTNode getOperator() {
        return operator;
    }

    @Override
    public ExpressionASTNode getRight() {
        return right;
    }

    @Override
    public ExpressionASTNode getLeft() {
        return null;
    }
}
