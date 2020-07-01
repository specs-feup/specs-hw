package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

public class ParenthesesStatment extends HardwareExpression {

    private HardwareExpression inner;

    public ParenthesesStatment(HardwareExpression inner) {
        super();
        this.inner = inner;
        this.addChild(inner);
    }

    @Override
    public String getAsString() {
        return "( " + this.inner.getAsString() + " )";
    }
}
