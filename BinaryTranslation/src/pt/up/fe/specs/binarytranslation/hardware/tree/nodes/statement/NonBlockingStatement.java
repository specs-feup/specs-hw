package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.*;

public class NonBlockingStatement extends HardwareStatement {

    public NonBlockingStatement(VariableReference target, HardwareExpression expression) {
        super(target, expression);
    }

    @Override
    public String getAsString() {
        return this.getTarget().getAsString() + " = " + this.getExpression().getAsString() + ";";
    }
}
