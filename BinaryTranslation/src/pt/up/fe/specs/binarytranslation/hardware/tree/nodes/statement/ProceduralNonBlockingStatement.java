package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;

public class ProceduralNonBlockingStatement extends SingleStatement {

    private ProceduralNonBlockingStatement() {
        super(HardwareNodeType.ProceduralNonBlocking);
    }

    public ProceduralNonBlockingStatement(VariableReference target, HardwareExpression expression) {
        super(HardwareNodeType.ProceduralNonBlocking, target, expression);
    }

    @Override
    public String getAsString() {
        return this.getTarget().getAsString() + " = " + this.getExpression().getAsString() + ";";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new ProceduralNonBlockingStatement();
    }
}
