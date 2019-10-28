package org.specs.MicroBlaze.instruction;

import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.RA;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.RB;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.RD;
import static pt.up.fe.specs.binarytranslation.expression.ExpressionOperator.equals;
import static pt.up.fe.specs.binarytranslation.expression.ExpressionOperator.sum;

import java.util.Stack;

import pt.up.fe.specs.binarytranslation.expression.ExpressionOperand;
import pt.up.fe.specs.binarytranslation.expression.ExpressionSymbol;
import pt.up.fe.specs.binarytranslation.instruction.InstructionExpression;

public enum MicroBlazeInstructionExpression implements InstructionExpression {

    // stack format
    add(equals, RD, sum, RA, RB);

    private Stack<ExpressionSymbol> symbs;
    
    private MicroBlazeInstructionExpression(ExpressionSymbol... symbs) {
        this.symbs = new Stack<ExpressionSymbol>();
        for(ExpressionSymbol s : symbs) {
            this.symbs.push(s);
        }
    }
    
    @Override
    public Stack<ExpressionSymbol> getSymbols() {
        return this.symbs;
    }
    /*
    public String express(List<Operand> operands) {
        String expression = "";
        
        List<String> stuff = new ArrayList<String>();
        while(!this.symbs.empty()) {
            var s = this.symbs.pop();
            
            // apply operand
            if(s.getClass() == ExpressionOperator.class) {
                var sc = ((ExpressionOperator) (s));
                expression = sc.apply(stuff.get(0), stuff.get(1));
                stuff.clear();
                stuff.add(expression);
            }
 
            // get values
            else if(s.getClass() == MicroBlazeAsmFields.class) {
                var sc = ((MicroBlazeAsmFields) (s)).getFieldName();
                for(Operand op : operands) {
                    var opc = (MicroBlazeOperand) op;
                    if(opc.getField() == sc) {
                        stuff.add(op.getRepresentation());
                    }
                }
            }
        } 
        return expression;
    }
    */
}
