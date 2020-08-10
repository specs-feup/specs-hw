package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class SubtractionExpression extends ABinaryHardwareExpression {

    public SubtractionExpression(HardwareExpression varA, HardwareExpression varB) {
        super(varA, varB);
        this.expressionOperator = "-";
        this.type = HardwareNodeType.SubtractionExpression;
    }
}
