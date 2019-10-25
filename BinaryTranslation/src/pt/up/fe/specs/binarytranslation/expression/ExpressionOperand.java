package pt.up.fe.specs.binarytranslation.expression;

import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class ExpressionOperand implements ExpressionSymbol {

    private Operand innerOperand;

    public ExpressionOperand(Operand op) {
        this.innerOperand = op;
    }

    String getRepresentation() {
        return this.innerOperand.getRepresentation();
    }

    Integer getIntegerValue() {
        return this.innerOperand.getIntegerValue();
    }
}
