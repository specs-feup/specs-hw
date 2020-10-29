package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class SubtractionExpression extends ABinaryHardwareExpression {

    private SubtractionExpression() {
        super("-", HardwareNodeType.SubtractionExpression);
    }

    public SubtractionExpression(HardwareExpression varA, HardwareExpression varB) {
        super("-", HardwareNodeType.SubtractionExpression, varA, varB);
    }

    /*
     * Shallow copy
     */
    @Override
    protected HardwareNode copyPrivate() {
        return new SubtractionExpression();
    }
}
