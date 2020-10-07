package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class BitWiseAndExpression extends ABinaryHardwareExpression {

    public BitWiseAndExpression(HardwareExpression varA, HardwareExpression varB) {
        super(varA, varB);
        this.expressionOperator = "&";
        this.type = HardwareNodeType.BitWiseAndExpression;
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new BitWiseAndExpression(this.getLeft(), this.getRight());
    }
}
