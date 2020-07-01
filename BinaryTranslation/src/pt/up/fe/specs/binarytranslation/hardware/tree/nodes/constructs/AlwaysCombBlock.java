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
            builder.append("\nalways_comb : " + this.blockName + "\n");
        else
            builder.append("\nalways_comb\n");

        for (var child : this.getChildren()) {
            builder.append("\t" + child.getAsString());
        }

        builder.append("end //" + this.blockName + "\n");
        return builder.toString();
    }
}
