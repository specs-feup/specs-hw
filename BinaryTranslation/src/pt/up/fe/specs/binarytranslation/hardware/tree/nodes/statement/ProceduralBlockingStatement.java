package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;

public class ProceduralBlockingStatement extends SingleStatement {

    private ProceduralBlockingStatement() {
        super(HardwareNodeType.ProceduralBlocking);
    }

    public ProceduralBlockingStatement(VariableReference target, HardwareExpression expression) {
        super(HardwareNodeType.ProceduralBlocking, target, expression);
    }

    @Override
    public String getAsString() {
        return this.getTarget().getAsString() + " = " + this.getExpression().getAsString() + ";";
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new ProceduralBlockingStatement();
    }
}
