package pt.up.fe.specs.binarytranslation.graphs;

import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class GraphOutput {

    private final GraphOutputType type;
    private final String value;
    // bitwidth?

    public GraphOutput(GraphOutputType tp, String value) {
        this.type = tp;
        this.value = value;
    }

    public GraphOutput(Operand op) {

        // TODO Complete this conversion to account for datawidths

        this.value = op.getRepresentation();

        // TODO how to determine is operand is a data or control output? would need instruction, or to add a field to

        // TODO actually, the branch oeprations of the microblaze have NO output register

        // OperandTypes
        // if(op)

        this.type = GraphOutputType.data;
    }

    public GraphOutputType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
