package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.*;

public class NonBlockingStatement extends HardwareStatement {

    private final VariableReference target;
    private final HardwareExpression expression;

    // TODO: AHardwareStatement class?

    public NonBlockingStatement(VariableReference target, HardwareExpression expression) {
        this.target = target;
        this.expression = expression;
        target.setParent(this);
        expression.setParent(this);
    }

    @Override
    public String getAsString() {
        return target.getAsString() + " = " + expression.getAsString() + ";\n";
    }
}
