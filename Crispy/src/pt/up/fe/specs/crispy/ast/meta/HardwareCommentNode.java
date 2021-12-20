package pt.up.fe.specs.crispy.ast.meta;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;

public class HardwareCommentNode extends HardwareMetaNode {

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
