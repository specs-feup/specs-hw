package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

public class AdditionExpression extends ABinaryHardwareExpression {

    public AdditionExpression(HardwareExpression varA, HardwareExpression varB) {
        super(varA, varB);
        this.expressionOperator = "+";
    }
}
