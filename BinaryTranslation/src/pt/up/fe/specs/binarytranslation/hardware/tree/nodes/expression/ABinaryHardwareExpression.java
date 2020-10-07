package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

public abstract class ABinaryHardwareExpression extends HardwareExpression {

    /*
     * 
     */
    protected String expressionOperator;

    public ABinaryHardwareExpression(HardwareExpression varA, HardwareExpression varB) {
        super();
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
    public String getAsString() {
        return this.getLeft().getAsString() + " " + this.expressionOperator + " " + this.getRight().getAsString();
    }
}
