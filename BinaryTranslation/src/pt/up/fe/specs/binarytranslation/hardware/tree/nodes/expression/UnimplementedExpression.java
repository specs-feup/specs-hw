package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareErrorMessage;

public class UnimplementedExpression extends HardwareExpression {

    public UnimplementedExpression(HardwareExpression varA, HardwareExpression varB, String operator) {
        super();
        this.addChild(new HardwareErrorMessage("Unimplemented expression" +
                " type for expression: \"" + varA.getAsString() + " " + operator + " " + varB.getAsString() + "\""));
        this.type = HardwareNodeType.UnimplementedExpression;
    }

    public UnimplementedExpression(HardwareExpression varA, String operator) {
        super();
        this.addChild(new HardwareErrorMessage("Unimplemented expression" +
                " type for expression: \"" + operator + " " + varA.getAsString() + "\""));
        this.type = HardwareNodeType.UnimplementedExpression;
    }

    @Override
    public String getAsString() {
        return this.getChild(0).getAsString();
    }
}
