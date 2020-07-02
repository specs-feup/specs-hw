package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.*;

public abstract class HardwareStatement extends HardwareNode {

    // are these the correct fields??..
    // No, should be VariableReference!
    // and the HardwareStatement is an expression, which could be complex, or a VariableReference itself
    // therefore VariableReference extends/implements HardwareStatement

    // TODO: classes like AdditionStatement, etc?

    protected HardwareStatement(VariableReference target, HardwareExpression expression) {
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
