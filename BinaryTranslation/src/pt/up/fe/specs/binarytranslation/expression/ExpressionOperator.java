package pt.up.fe.specs.binarytranslation.expression;

import java.util.function.BinaryOperator;

/**
 * Primitive operations that can be used to construct a stack of operations which, when resolved, implement particular
 * instructions of the target ISA
 * 
 * @author nuno
 *
 */
public enum ExpressionOperator implements ExpressionSymbol {

    equals((b, a) -> a + " = " + b),
    sum((b, a) -> "(" + a + " + " + b + ")"),
    sub((b, a) -> "(" + a + " - " + b + ")"),
    shiftleft((b, a) -> "(" + a + " << " + b + ")"),
    shiftright((b, a) -> "(" + a + " >> " + b + ")");

    private final BinaryOperator<String> operator;

    private ExpressionOperator(final BinaryOperator<String> operator) {
        this.operator = operator;
    }

    public String apply(String arg0, String arg1) {
        return this.operator.apply(arg0, arg1);
    }
}

// LOOSE IDEAS

/*
each instruction represents a stack of operands and operations

example: "add" from microblaze is "add rd, ra, rb"

which as a stack of generic operands and operators is

[<field>, assignement, <field>, <field>, addition]
    --> these fields should be the allowed AsmFields!


--------------

1. interface of operators

2. enums implement that interface, where each enum is a target language (e.g. verilog, java, c)

3. when resolving the stack, construct the string based on specirfic language operators
    e.g. in verilog, assimgment has a pre and post string "assign <rd> =" 
*/

/*

 other idea:
 Instead of have this stack operation structure, express each step of an instructions operation as some kind of apply function
 then resolve the expression by calling apply to the input strings for each step of the operation
 
 e.g.
 
 list operationsymbols = [assignment("rd", addition("ra", "rb"))]

 for each
     symbol.apply() ---> leads to addition returning "ra + rb",  and then assignemnt returning "rd = ra + rb" 
 
 
 
*/
