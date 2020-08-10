package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;

public class ProceduralNonBlockingStatement extends SingleStatement {

    public ProceduralNonBlockingStatement(VariableReference target, HardwareExpression expression) {
        super(target, expression);
        this.type = HardwareNodeType.ProceduralNonBlocking;
    }

    @Override
    public String getAsString() {
        return this.getTarget().getAsString() + " <= " + this.getExpression().getAsString() + ";";
    }
}
