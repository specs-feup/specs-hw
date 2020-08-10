package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class ParenthesisExpression extends HardwareExpression {

    private HardwareExpression inner;

    public ParenthesisExpression(HardwareExpression inner) {
        super();
        this.inner = inner;
        this.addChild(inner);
        this.type = HardwareNodeType.ParenthesisExpression;
    }

    @Override
    public String getAsString() {
        return "( " + this.inner.getAsString() + " )";
    }
}
