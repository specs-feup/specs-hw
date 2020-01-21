package pt.up.fe.specs.binarytranslation.graphs;

import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class GraphOutput {

    private Operand op; // the originating operand
    private GraphOutputType type;
    private String value;
    // bitwidth?

    public GraphOutput(Operand op) {

        // TODO Complete this conversion to account for datawidths

        this.value = op.getRepresentation();

        // TODO how to determine is operand is a data or control output? would need instruction, or to add a field to

        // TODO actually, the branch oeprations of the microblaze have NO output register

        // OperandTypes
        // if(op)

        this.type = GraphOutputType.data;
        // default to "data", change to liveout if the owner of
        // this output is the last one to write to this register (?)

        this.op = op;
    }

    public GraphOutputType getType() {
        return type;
    }

    public String getRepresentation() {
        return value;
    }

    public void setOutputAs(GraphOutputType type, String value) {
        this.type = type;
        this.value = value;
    }
}
