package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;

public class HardwareCommentNode extends AHardwareNode {

    /*
     * Copyright comment info
     */
    final private String commentText;

    public HardwareCommentNode(String commentText) {
        this.commentText = commentText;
    }

    @Override
    public String getAsString() {
        return commentText;
    }
}
