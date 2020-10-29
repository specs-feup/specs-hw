package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class BitWiseAndExpression extends ABinaryHardwareExpression {

    private BitWiseAndExpression() {
        super("&", HardwareNodeType.BitWiseAndExpression);
    }

    public BitWiseAndExpression(HardwareExpression varA, HardwareExpression varB) {
        super("&", HardwareNodeType.BitWiseAndExpression, varA, varB);
    }

    /*
     * Shallow copy
     */
    @Override
    protected HardwareNode copyPrivate() {
        return new BitWiseAndExpression();
    }
}
