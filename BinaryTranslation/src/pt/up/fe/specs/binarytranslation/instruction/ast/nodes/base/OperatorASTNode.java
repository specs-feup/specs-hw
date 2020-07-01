package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class OperatorASTNode extends InstructionASTNode {

    private String operator;

    public OperatorASTNode(String operator) {
        super();
        this.operator = operator;
        this.type = InstructionASTNodeType.OperatorNode;
    }

    @Override
    public String getAsString() {
        return this.operator;
    }
}
