package pt.up.fe.specs.binarytranslation.operations;

/**
 * Primitive operations that can be used to construct a stack of operations which, when resolved, implement particular
 * instructions of the target ISA
 * 
 * @author nuno
 *
 */
public enum Operation {

    assign(2, 1, false, "="),
    add(2, 2, true, "+");

    private int numoperands;
    private int numresults;
    private String operator;
    private Boolean permutable;

    private Operation(int numoperands, int numresults, Boolean permutable, String operator) {
        this.numoperands = numoperands;
        this.numresults = numresults;
        this.operator = operator;
        this.permutable = permutable;
    }

}
