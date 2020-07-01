package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;

public class HardwareCommentNode extends HardwareNode {

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
