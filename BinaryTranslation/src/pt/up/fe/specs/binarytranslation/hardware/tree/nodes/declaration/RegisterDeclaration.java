package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class RegisterDeclaration extends VariableDeclaration {

    private final String regName;
    private final int numBits;

    public RegisterDeclaration(String regName, int numBits) {
        super();
        this.regName = regName;
        this.numBits = numBits;
        this.type = HardwareNodeType.RegisterDeclaration;
    }

    @Override
    public String getVariableName() {
        return this.regName;
    }

    @Override
    public String getAsString() {
        return "reg [ (" + this.numBits + " - 1) : 0] " + this.regName + ";\n";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new RegisterDeclaration(this.regName, this.numBits);
    }
}
