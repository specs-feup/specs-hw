package pt.up.fe.specs.binarytranslation.expression;

public interface ExpressionOperand extends ExpressionSymbol {

    @Override
    default boolean isOperator() {
        return false;
    }
    
    @Override
    default boolean isOperand() {
        return true;
    }

    public String getFieldName();
}
