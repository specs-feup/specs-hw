package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;

public class HardwareErrorMessage extends HardwareNode {

    final private String errorText;

    public HardwareErrorMessage(String commentText) {
        this.errorText = commentText;
    }

    @Override
    public String getAsString() {
        return "$error(" + errorText + ");";
    }
}
