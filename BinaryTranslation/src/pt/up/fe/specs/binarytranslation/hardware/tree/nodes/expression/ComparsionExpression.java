package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class ComparsionExpression extends ABinaryHardwareExpression {

    public ComparsionExpression(HardwareExpression varA, HardwareExpression varB) {
        super(varA, varB);
        this.expressionOperator = "==";
        this.type = HardwareNodeType.ComparsionExpression;
    }
}
