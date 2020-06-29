package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public interface ExpressionASTNode extends InstructionASTNode {

    default public OperatorASTNode getOperator() {
        return null;
    }

    default public ExpressionASTNode getLeft() {
        return null;
    }

    default public ExpressionASTNode getRight() {
        return null;
    }
}
