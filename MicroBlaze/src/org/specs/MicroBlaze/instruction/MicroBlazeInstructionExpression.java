package org.specs.MicroBlaze.instruction;

import static org.specs.MicroBlaze.parsing.MicroBlazeAsmFields.RA;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmFields.RB;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmFields.RD;
import static pt.up.fe.specs.binarytranslation.expression.ExpressionOperator.equals;
import static pt.up.fe.specs.binarytranslation.expression.ExpressionOperator.sum;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFields;

import pt.up.fe.specs.binarytranslation.expression.ExpressionOperator;
import pt.up.fe.specs.binarytranslation.expression.ExpressionSymbol;
import pt.up.fe.specs.binarytranslation.instruction.InstructionExpression;
import pt.up.fe.specs.binarytranslation.instruction.Operand;

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
}
