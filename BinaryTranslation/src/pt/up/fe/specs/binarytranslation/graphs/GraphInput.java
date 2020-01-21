package pt.up.fe.specs.binarytranslation.graphs;

import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class GraphInput {

    private Operand op; // the originating operand
    private GraphInputType type;
    private String value;

    public GraphInput(Operand op) {

        if (op.isRegister()) {
            this.type = GraphInputType.livein;
        }

        // if immediate
        else if (op.isImmediate()) {
            this.type = GraphInputType.immediate;
        }

        this.value = op.getRepresentation();
        this.op = op;
    }

    public GraphInputType getType() {
        return type;
    }

    public String getRepresentation() {
        return value;
    }

    public Boolean isImmediate() {
        return this.type == GraphInputType.immediate;
    }

    public Boolean isLivein() {
        return this.type == GraphInputType.livein;
    }

    /*
     * Input is of this type if its input register (in the original asm list) was written by a preceeding instruction
     */
    public Boolean isInternal() {
        return this.type == GraphInputType.noderesult;
    }

    /*
     * An input is modified during the resolving of the graph, when a BinarySegmentGraph is instantiated
     */
    public void setInputAs(GraphInputType type, String value) {
        this.type = type;
        this.value = value;
    }

    /*
     * 
     */
    public String rawDotty() {
        String ret = "";
        if (this.isLivein())
            ret += "in_";
        ret += this.getRepresentation();
        return ret;
    }
}
