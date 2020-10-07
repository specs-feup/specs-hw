package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;

public class SingleStatement extends HardwareStatement {

    protected SingleStatement(VariableReference target, HardwareExpression expression) {
        super();
        this.addChild(target);
        this.addChild(expression);
    }

    protected VariableReference getTarget() {
        return (VariableReference) this.getChild(0);
    }

    protected HardwareExpression getExpression() {
        return (HardwareExpression) this.getChild(1);
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new SingleStatement(this.getTarget(), this.getExpression());
    }
}
