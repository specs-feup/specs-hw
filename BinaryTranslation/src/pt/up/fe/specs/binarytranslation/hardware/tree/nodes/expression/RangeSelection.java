package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class RangeSelection extends HardwareExpression {

    private int lower, upper;

    private RangeSelection(int lower, int upper) {
        super();
        this.lower = lower;
        this.upper = upper;
        this.type = HardwareNodeType.RangeSelection;
    }

    public RangeSelection(VariableReference var, int lower, int upper) {
        this(lower, upper);
        this.addChild(var);
    }

    public RangeSelection(VariableReference var, int upper) {
        this(var, 0, upper);
    }

    public VariableReference getVar() {
        return (VariableReference) this.getChild(0);
    }

    @Override
    public String getAsString() {
        return this.getVar().getAsString() + "[" + (this.upper - 1) + ":" + this.lower + "]";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new RangeSelection(this.lower, this.upper);
    }
}
