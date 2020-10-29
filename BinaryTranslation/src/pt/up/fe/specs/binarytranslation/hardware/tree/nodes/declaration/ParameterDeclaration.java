package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class ParameterDeclaration extends VariableDeclaration {

    private final String parameterName;
    private final int numBits;
    private final String initializer;

    public ParameterDeclaration(String parameterName, int numBits, String initializer) {
        super();
        this.parameterName = parameterName;
        this.numBits = numBits;
        this.initializer = initializer;
        this.type = HardwareNodeType.ParameterDeclaration;
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

    @Override
    protected HardwareNode copyPrivate() {
        return new ParameterDeclaration(this.parameterName, this.numBits, this.initializer);
    }
}
