package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class ComparsionExpression extends ABinaryHardwareExpression {

    private ComparsionExpression() {
        super("==", HardwareNodeType.ComparsionExpression);
    }

    public ComparsionExpression(HardwareExpression varA, HardwareExpression varB) {
        super("==", HardwareNodeType.ComparsionExpression, varA, varB);
    }

    /*
     * Shallow copy
     */
    @Override
    protected HardwareNode copyPrivate() {
        return new ComparsionExpression();
    }
}
