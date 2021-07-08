package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class ParenthesisExpression extends HardwareExpression {

    private ParenthesisExpression() {
        super();
        this.type = HardwareNodeType.ParenthesisExpression;
    }

    public ParenthesisExpression(HardwareExpression inner) {
        this();
        this.addChild(inner);

    }

    @Override
    public String toContentString() {
        return "( )";
    }

    @Override
    public String getAsString() {
        return "( " + this.getInner().getAsString() + " )";
    }

    public HardwareExpression getInner() {
        return (HardwareExpression) this.getChild(0);
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new ParenthesisExpression();
    }
}
