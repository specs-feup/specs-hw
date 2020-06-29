package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;

public class RangeSelection extends AHardwareNode implements HardwareExpression {

    private int lower, upper;
    private VariableReference var;

    public RangeSelection(VariableReference var, int lower, int upper) {
        super();
        this.var = var;
        this.children.add(this.var);
        this.lower = lower;
        this.upper = upper;
    }

    public RangeSelection(VariableReference var, int upper) {
        this(var, 0, upper);
    }

    @Override
    public String getAsString() {
        return this.var.getAsString() + "[" + (this.upper - 1) + ":" + this.lower + "]";
    }
}
