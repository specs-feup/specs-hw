package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs;

public class AlwaysCombBlock extends HardwareConstruct {

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
            builder.append("\t" + child.getAsString() + "\n");
        }

        builder.append("end //" + this.blockName + "\n");
        return builder.toString();
    }
}
