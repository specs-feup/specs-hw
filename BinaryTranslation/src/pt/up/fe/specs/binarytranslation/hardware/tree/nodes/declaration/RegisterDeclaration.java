package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;

public class RegisterDeclaration extends AHardwareNode implements VariableDeclaration {

    private final String regName;
    private final int numBits;

    public RegisterDeclaration(String regName, int numBits) {
        super();
        this.regName = regName;
        this.numBits = numBits;
    }

    @Override
    public String getAsString() {
        return "reg [ (" + this.numBits + " - 1) : 0] " + this.regName + ";\n";
    }
}
