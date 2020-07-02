package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

public class LiteralOperandASTNode extends OperandASTNode {

    private Number value;

    public LiteralOperandASTNode(Number value) {
        super();
        this.value = value;
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
