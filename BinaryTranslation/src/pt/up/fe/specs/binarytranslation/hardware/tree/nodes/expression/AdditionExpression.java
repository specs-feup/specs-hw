package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class AdditionExpression extends ABinaryHardwareExpression {

    private AdditionExpression() {
        super("+", HardwareNodeType.AdditionExpression);
    }

    public AdditionExpression(HardwareExpression varA, HardwareExpression varB) {
        super("+", HardwareNodeType.AdditionExpression, varA, varB);
    }

    /*
     * Shallow copy
     */
    @Override
    protected HardwareNode copyPrivate() {
        return new AdditionExpression();
    }
}
