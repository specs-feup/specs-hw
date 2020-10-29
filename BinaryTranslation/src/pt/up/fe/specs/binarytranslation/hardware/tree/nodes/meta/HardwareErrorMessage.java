package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class HardwareErrorMessage extends HardwareNode {

    final private String errorText;

    public HardwareErrorMessage(String commentText) {
        this.errorText = commentText;
        this.type = HardwareNodeType.ErrorMsg;
    }

    @Override
    public String getAsString() {
        return "$error(\"" + errorText + "\");";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new HardwareErrorMessage(this.errorText);
    }
}
