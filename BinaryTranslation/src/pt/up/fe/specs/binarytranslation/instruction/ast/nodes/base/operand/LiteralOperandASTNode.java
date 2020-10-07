package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
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

    @Override
    protected InstructionASTNode copyPrivate() {
        return new LiteralOperandASTNode(this.value);
    }
}
