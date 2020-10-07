package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.statement;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public abstract class StatementASTNode extends InstructionASTNode {

    public StatementASTNode(InstructionASTNodeType type) {
        super();
        this.type = type;
    }
}
