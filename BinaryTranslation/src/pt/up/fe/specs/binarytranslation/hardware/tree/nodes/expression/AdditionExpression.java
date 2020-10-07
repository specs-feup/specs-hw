package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class AdditionExpression extends ABinaryHardwareExpression {

    public AdditionExpression(HardwareExpression varA, HardwareExpression varB) {
        super(varA, varB);
        this.expressionOperator = "+";
        this.type = HardwareNodeType.AdditionExpression;
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new AdditionExpression(this.getLeft(), this.getRight());
    }
}
