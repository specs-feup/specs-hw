package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

public class RegisterDeclaration extends VariableDeclaration {

    private final String regName;
    private final int numBits;

    public RegisterDeclaration(String regName, int numBits) {
        super();
        this.regName = regName;
        this.numBits = numBits;
    }

    @Override
    public String getVariableName() {
        return this.regName;
    }

    @Override
    public String getAsString() {
        return "reg [ (" + this.numBits + " - 1) : 0] " + this.regName + ";\n";
    }
}
