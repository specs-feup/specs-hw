package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

public class RangeSelection extends HardwareExpression {

    private int lower, upper;
    private VariableReference var;

    public RangeSelection(VariableReference var, int lower, int upper) {
        super();
        this.var = var;
        this.addChild(this.var);
        this.lower = lower;
        this.upper = upper;
    }

    public RangeSelection(VariableReference var, int upper) {
        this(var, 0, upper);
    }

    public String getAsString() {
        return this.var.getAsString() + "[" + (this.upper - 1) + ":" + this.lower + "]";
    }
}
