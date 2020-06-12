package org.specs.MicroBlaze.instruction;

import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.*;
import static pt.up.fe.specs.binarytranslation.expression.ExpressionOperator.*;

import java.util.Stack;

import pt.up.fe.specs.binarytranslation.expression.ExpressionSymbol;
import pt.up.fe.specs.binarytranslation.instruction.InstructionExpression;

public enum MicroBlazeInstructionExpression { // implements InstructionExpression {

    /*
    // stack format
    add(equals, RD, sum, RA, RB),
    addi(equals, RD, sum, RA, IMM);
    
    private final Stack<ExpressionSymbol> symbs;
    
    private MicroBlazeInstructionExpression(ExpressionSymbol... symbs) {
        this.symbs = new Stack<ExpressionSymbol>();
        for (ExpressionSymbol s : symbs) {
            this.symbs.push(s);
        }
    }
    
    // @Override
    public Stack<ExpressionSymbol> getSymbols() {
        return this.symbs;
    }*/
}
