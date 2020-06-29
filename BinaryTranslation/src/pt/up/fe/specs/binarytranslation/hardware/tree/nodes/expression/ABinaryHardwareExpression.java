package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;

public abstract class ABinaryHardwareExpression extends AHardwareNode implements HardwareExpression {

    /*
     * 
     */
    protected String expressionOperator;
    protected final HardwareExpression varA, varB;

    public ABinaryHardwareExpression(HardwareExpression varA, HardwareExpression varB) {
        super();
        this.varA = varA;
        this.varB = varB;
    }

    @Override
    public String getAsString() {
        return this.varA.getAsString() + " " + this.expressionOperator + " " + this.varB.getAsString();
    }
}