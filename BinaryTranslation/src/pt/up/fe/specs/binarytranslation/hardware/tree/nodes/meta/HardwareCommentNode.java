package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class HardwareCommentNode extends HardwareNode {

    /*
     * Copyright comment info
     */
    final private String commentText;

    public HardwareCommentNode(String commentText) {
        this.commentText = commentText;
        this.type = HardwareNodeType.Comment;
    }

    @Override
    public String getAsString() {
        return "//" + commentText;
    }
}
