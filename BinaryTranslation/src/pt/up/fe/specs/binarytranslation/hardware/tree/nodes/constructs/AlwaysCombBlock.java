package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class AlwaysCombBlock extends HardwareNode {

    private String blockName;

    public AlwaysCombBlock() {
        this("");
    }

    public AlwaysCombBlock(String blockname) {
        this.blockName = blockname;
        this.type = HardwareNodeType.AlwaysComb;
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();

        if (!this.blockName.isBlank())
            builder.append("always_comb : " + this.blockName + "\n");
        else
            builder.append("always_comb\n");

        for (var child : this.getChildren()) {
            builder.append("\t" + child.getAsString() + "\n");
        }

        builder.append("end //" + this.blockName + "\n");
        return builder.toString();
    }
}
