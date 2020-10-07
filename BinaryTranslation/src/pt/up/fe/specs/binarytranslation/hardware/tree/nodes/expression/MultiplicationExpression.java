package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class MultiplicationExpression extends ABinaryHardwareExpression {

    private MultiplicationExpression() {
        super("*", HardwareNodeType.MultiplicationExpression);
    }

    public MultiplicationExpression(HardwareExpression varA, HardwareExpression varB) {
        super("*", HardwareNodeType.MultiplicationExpression, varA, varB);
    }

    /*
     * Shallow copy
     */
    @Override
    protected HardwareNode copyPrivate() {
        return new MultiplicationExpression();
    }
}
