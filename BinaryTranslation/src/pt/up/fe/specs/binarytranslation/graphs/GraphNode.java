package pt.up.fe.specs.binarytranslation.graphs;

public class GraphNode {

    private int nodenr;
    private int level;
    private int numchildren;
    private int numparents;
    // Instruction inst;
    private GraphOperation op;

    // public GraphNode(Instruction inst, int level, int numchildren, int numparents) {
    public GraphNode(GraphOperation op, int level, int numchildren, int numparents) {
        // this.inst = inst;
        this.op = op;
        this.level = level;
        this.numchildren = numchildren;
        this.numparents = numparents;
    }

    public GraphOperation getOp() {
        return op;
    }

    public int getLevel() {
        return level;
    }

    public int getNumchildren() {
        return numchildren;
    }

    public int getNumparents() {
        return numparents;
    }
}
