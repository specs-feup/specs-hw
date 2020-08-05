package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

public class ParameterDeclaration extends VariableDeclaration {

    private final String parameterName;
    private final int numBits;
    private final String initializer;

    public ParameterDeclaration(String parameterName, int numBits, String initializer) {
        super();
        this.parameterName = parameterName;
        this.numBits = numBits;
        this.initializer = initializer;
    }

    @Override
    public String getVariableName() {
        return this.parameterName;
    }

    @Override
    public String getAsString() {
        return "parameter [ (" + this.numBits + " - 1) : 0] "
                + this.parameterName + "= {" + this.initializer + "};\n";
    }
}
