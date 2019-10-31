package pt.up.fe.specs.binarytranslation.test;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.expression.ExpressionOperator;

public class ExpressionOperatorTest {

    @Test
    public void test() {
        System.out.print(ExpressionOperator.equals.apply("ra", "rb"));
    }

}
