package pt.up.fe.specs.binarytranslation.graphs;

import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;

public class GraphInput {

    private GraphInputType type;
    private String value;
    // bitwidth?

    public GraphInput(GraphInputType tp, String value) {
        this.type = tp;
        this.value = value;
    }

    public GraphInput(Operand op) {

        // TODO Compelte this conversion to account for datawidths

        var type = op.getProperties().getMainType();

        if (type == OperandType.REGISTER) {
            this.type = GraphInputType.livein;
        }

        // if immediate
        else {
            this.type = GraphInputType.immediate;
        }

        this.value = op.getStringValue();
    }

    public GraphInputType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setInputAs(GraphInputType type, String value) {
        this.type = type;
        this.value = value;
    }
}
