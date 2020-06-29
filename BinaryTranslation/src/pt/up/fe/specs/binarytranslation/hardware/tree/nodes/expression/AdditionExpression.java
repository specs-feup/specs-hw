package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.VariableReference;

public class AdditionExpression extends ABinaryHardwareExpression {

    public AdditionExpression(VariableReference varA, VariableReference varB) {
        super(varA, varB);
        this.expressionOperator = "+";
    }
}
