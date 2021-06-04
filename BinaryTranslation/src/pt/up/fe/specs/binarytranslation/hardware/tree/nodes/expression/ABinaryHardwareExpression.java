package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public abstract class ABinaryHardwareExpression extends HardwareExpression {

    protected String expressionOperator;

    protected ABinaryHardwareExpression(String operator, HardwareNodeType type) {
        super();
        this.expressionOperator = operator;
        this.type = type;
    }

    protected ABinaryHardwareExpression(String operator,
            HardwareNodeType type, HardwareExpression varA, HardwareExpression varB) {
        this(operator, type);
        this.addChild(varA);
        this.addChild(varB);
    }

    protected HardwareExpression getLeft() {
        return (HardwareExpression) this.getChild(0);
    }

    protected HardwareExpression getRight() {
        return (HardwareExpression) this.getChild(1);
    }

    @Override
    public String toContentString() {
        return this.expressionOperator;
    }

    @Override
    public String getAsString() {
        return this.getLeft().getAsString() + " " + this.toContentString() + " " + this.getRight().getAsString();
    }
}
