package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;

public abstract class HardwareExpression extends HardwareNode {

    @Override
    public String toContentString() {
        return this.getAsString();
    }
}
