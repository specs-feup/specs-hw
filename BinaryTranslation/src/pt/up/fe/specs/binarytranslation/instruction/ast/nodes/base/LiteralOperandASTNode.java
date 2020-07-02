package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class LiteralOperandASTNode extends OperandASTNode {

    private Number value;

    public LiteralOperandASTNode(Number value) {
        super();
        this.value = value;
        this.type = InstructionASTNodeType.LiteralOperandNode;
    }

    @Override
    public String getAsString() {
        // if(this.value instanceof Integer)
        return Integer.toString(this.value.intValue());
        // else if(this.value instanceof )
    }

    public Number getValue() {
        return value;
    }
}
