package pt.up.fe.specs.binarytranslation.expression;

public interface ExpressionSymbol {
 
    public boolean isOperand();
    
    public boolean isOperator();

    default String apply(String string, String string2) {
        return "";
    }
}
