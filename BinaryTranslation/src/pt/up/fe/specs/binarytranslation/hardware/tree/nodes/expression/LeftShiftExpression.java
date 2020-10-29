package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class LeftShiftExpression extends ABinaryHardwareExpression {

    private LeftShiftExpression() {
        super("<<", HardwareNodeType.LeftShiftExpression);
    }

    public LeftShiftExpression(HardwareExpression varA, HardwareExpression varB) {
        super("<<", HardwareNodeType.LeftShiftExpression, varA, varB);
    }

    /*
     * Shallow copy
     */
    @Override
    protected HardwareNode copyPrivate() {
        return new LeftShiftExpression();
    }
}
