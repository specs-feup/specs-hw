package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public abstract class OperandASTNode extends ExpressionASTNode {

    public OperandASTNode() {
        this.type = InstructionASTNodeType.OperandNode;
    }
}
