package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.VariableReference;

public interface ExpressionASTNode extends InstructionASTNode {

    default public InstructionASTNode getOperator() {
        return null;
    }

    default public InstructionASTNode getLeft() {
        return null;
    }

    default public InstructionASTNode getRight() {
        return null;
    }

    public void setResultName(String resultName);

    default public VariableReference getResultName() {
        return null;
    }
}
