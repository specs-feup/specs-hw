package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class RangeSelection extends HardwareExpression {

    private int lower, upper;
    private VariableReference var;

    public RangeSelection(VariableReference var, int lower, int upper) {
        super();
        this.var = var;
        this.addChild(this.var);
        this.lower = lower;
        this.upper = upper;
        this.type = HardwareNodeType.RangeSelection;
    }

    public RangeSelection(VariableReference var, int upper) {
        this(var, 0, upper);
    }

    @Override
    public String getAsString() {
        return this.var.getAsString() + "[" + (this.upper - 1) + ":" + this.lower + "]";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new RangeSelection(this.var, this.lower, this.upper);
    }
}
