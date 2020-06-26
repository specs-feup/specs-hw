package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;

public class AlwaysCombBlock extends AHardwareNode implements HardwareConstruct {

    private String blockName;

    public AlwaysCombBlock() {
        this.blockName = "";
    }

    public AlwaysCombBlock(String blockname) {
        this.blockName = blockname;
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();

        if (!this.blockName.isBlank())
            builder.append("always_comb : " + this.blockName + "\n");
        else
            builder.append("always_comb\n");

        for (var child : this.getChildren()) {
            builder.append(child.getAsString());
        }
        builder.append("end //" + this.blockName + "\n");
        return builder.toString();
    }
}
