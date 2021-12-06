package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class HardwareCommentNode extends HardwareMetaNode {

    /*
     * Copyright comment info
     */
    final private String commentText;

    public HardwareCommentNode(String commentText) {
        super(HardwareNodeType.Comment);
        this.commentText = commentText.replace("\n", "\n// ");
    }

    @Override
    public String getAsString() {
        return "// " + commentText;
    }

    @Override
    protected HardwareCommentNode copyPrivate() {
        return new HardwareCommentNode(this.commentText);
    }

    @Override
    public HardwareCommentNode copy() {
        return (HardwareCommentNode) super.copy();
    }
}
