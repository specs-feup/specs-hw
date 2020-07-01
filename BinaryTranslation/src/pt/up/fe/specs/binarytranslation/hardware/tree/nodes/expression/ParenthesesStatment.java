package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;

public class ParenthesesStatment extends AHardwareNode implements HardwareExpression {

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
