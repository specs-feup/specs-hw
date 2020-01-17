package pt.up.fe.specs.binarytranslation.graphs;

public class GraphInput {

    private final GraphInputType tp;
    private final int value;
    // bitwidth?

    public GraphInput(GraphInputType tp, int value) {
        this.tp = tp;
        this.value = value;
    }
}
