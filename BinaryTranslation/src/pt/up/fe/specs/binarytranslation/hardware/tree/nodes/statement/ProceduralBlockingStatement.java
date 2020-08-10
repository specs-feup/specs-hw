package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;

public class ProceduralBlockingStatement extends SingleStatement {

    public ProceduralBlockingStatement(VariableReference target, HardwareExpression expression) {
        super(target, expression);
    }

    @Override
    public String getAsString() {
        return this.getTarget().getAsString() + " = " + this.getExpression().getAsString() + ";";
    }
}