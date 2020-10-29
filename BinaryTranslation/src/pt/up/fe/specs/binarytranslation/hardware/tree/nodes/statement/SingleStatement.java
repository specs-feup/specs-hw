package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;

public abstract class SingleStatement extends HardwareStatement {

    protected SingleStatement(HardwareNodeType type) {
        super();
        this.type = type;
    }

    protected SingleStatement(HardwareNodeType type, VariableReference target, HardwareExpression expression) {
        this(type);
        this.addChild(target);
        this.addChild(expression);
    }

    protected VariableReference getTarget() {
        return (VariableReference) this.getChild(0);
    }

    protected HardwareExpression getExpression() {
        return (HardwareExpression) this.getChild(1);
    }
}
