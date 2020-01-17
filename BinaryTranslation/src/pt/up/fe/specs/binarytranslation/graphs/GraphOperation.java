package pt.up.fe.specs.binarytranslation.graphs;

public enum GraphOperation {

    add(32, true, false, false, false),
    sub(32, true, false, false, false);

    private int bitwidth;
    private Boolean arithmetic, logical, floating, special;

    // TODO add a bifunction that expresses the operation?

    private GraphOperation(int bitwidth, Boolean arithmetic, Boolean logical, Boolean floating, Boolean special) {
        this.bitwidth = bitwidth;
        this.arithmetic = arithmetic;
        this.logical = logical;
        this.floating = floating;
        this.special = special;
    }
}
