package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.AInstructionASTNode;

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
        return (OperatorASTNode) this.children.get(0);
    }

    @Override
    public ExpressionASTNode getRight() {
        return (ExpressionASTNode) this.children.get(1);
    }

    @Override
    public ExpressionASTNode getLeft() {
        return null;
    }
}
