package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.VariableReference;

public abstract class ABinaryHardwareExpression extends AHardwareNode implements HardwareExpression {

    /*
     * 
     */
    protected String expressionOperator;
    protected final VariableReference varA, varB;

    public ABinaryHardwareExpression(VariableReference varA, VariableReference varB) {
        super();
        this.varA = varA;
        this.varB = varB;
    }

    @Override
    public String getAsString() {
        return this.varA.getAsString() + " " + this.expressionOperator + " " + this.varB.getAsString();
    }
}
