package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;

public class WireDeclaration extends AHardwareNode implements VariableDeclaration {

    private final String wireName;
    private final int numBits;

    public WireDeclaration(String wireName, int numBits) {
        super();
        this.wireName = wireName;
        this.numBits = numBits;
    }

    @Override
    public String getAsString() {
        return "wire [ (" + this.numBits + " - 1) : 0] " + this.wireName + ";\n";
    }
}
