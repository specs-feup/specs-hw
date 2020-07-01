package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;

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
