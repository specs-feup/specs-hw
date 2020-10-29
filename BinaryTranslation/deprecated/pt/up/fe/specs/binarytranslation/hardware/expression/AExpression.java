/**
 * Copyright 2020 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.expression;

import java.util.*;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;
import pt.up.fe.specs.binarytranslation.expression.ExpressionSymbol;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class AExpression implements Expression {

    /*
     * Symbols should be signal names, and operators (maybe clocking blocks and other things??)
     */
    private Stack<ExpressionSymbol> symbs;

    public AExpression(Stack<ExpressionSymbol> symbs) {
        this.symbs = symbs;
    }

    @Override
    public String solve() {

        String expression = "";
        /*   List<String> stuff = new ArrayList<String>();
        
        while (!symbs.empty()) {
            var s = symbs.pop();
        
            // apply operand
            if (s.isOperator()) {
                expression = s.apply(stuff.get(0), stuff.get(1));
                stuff.clear();
                stuff.add(expression);
            }
        
            // get values (symbolic or absolute, either one)
            else if (s.isOperand()) {
                stuff.add(helper.get(s).getRepresentation());
            }
        }*/
        return expression;
    }

    @Override
    public Stack<ExpressionSymbol> getSymbols() {
        // TODO Auto-generated method stub
        return null;
    }

}
