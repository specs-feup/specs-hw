package pt.up.fe.specs.binarytranslation.graphs;

public class GraphOperation {

    // add(32, true, false, false, false),
    // sub(32, true, false, false, false);

    GraphOperationType type;
    // private int numinputs;
    // private int numoutputs;
    // private int bitwidth;

    // TODO add a bifunction that expresses the operation?

    /*private GraphOperation(int bitwidth, Boolean arithmetic, Boolean logical, Boolean floating, Boolean special) {
        this.bitwidth = bitwidth;
        this.arithmetic = arithmetic;
        this.logical = logical;
        this.floating = floating;
        this.special = special;
    }*/

    public GraphOperation(GraphOperationType type) {
        this.type = type;
    }

    public GraphOperationType getType() {
        return type;
    }
}
