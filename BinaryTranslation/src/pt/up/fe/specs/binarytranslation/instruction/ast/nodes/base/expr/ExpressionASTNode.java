package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public abstract class ExpressionASTNode extends InstructionASTNode {

    public ExpressionASTNode(InstructionASTNodeType type) {
        super();
        this.type = type;
    }
}
