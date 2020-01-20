package pt.up.fe.specs.binarytranslation.graphs;

public enum GraphOperationType {

    add,
    sub,
    mul,
    unknown;

    // TODO add expression to operation type? e.g. an add is a "+" or something
    // this can vary by language tho...

    private String name;

    private GraphOperationType() {
        this.name = name();
    }

    public String getName() {
        return name;
    }
}
