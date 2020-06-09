package pt.up.fe.specs.binarytranslation.graphs.edge;

import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class GraphInput extends AGraphEdge {

    public GraphInput(Operand op) {
        super(op, GraphEdgeDirection.input, op.getRepresentation());

        if (op.isRegister()) {
            this.etype = GraphEdgeType.livein;
        }

        // if immediate
        else if (op.isImmediate()) {
            this.etype = GraphEdgeType.immediate;
        }
    }

    /*
     * An input is modified during the resolving of the graph, when a BinarySegmentGraph is instantiated
     */
    public void setInputAs(GraphEdgeType type, String value) {
        this.setEdgeAs(type, value);
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
