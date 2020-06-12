/**
 *  Copyright 2019 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import pt.up.fe.specs.binarytranslation.instruction.InstructionExpression;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.parsing.AsmField;

public class ExpressionSolver {

    public static String solve(InstructionExpression expr, Map<AsmField, Operand> helper) {
        String expression = "";
        
        List<String> stuff = new ArrayList<String>();
        
        @SuppressWarnings("unchecked")
        Stack<ExpressionSymbol> symbs = (Stack<ExpressionSymbol>) expr.getSymbols().clone();
                
        while(!symbs.empty()) {
            var s = symbs.pop(); 
            
            // apply operand
            if(s.isOperator()) {
                expression = s.apply(stuff.get(0), stuff.get(1));
                stuff.clear();
                stuff.add(expression);
            }
 
            // get values (symbolic or absolute, either one)
            else if(s.isOperand()) {
                stuff.add(helper.get(s).getRepresentation());
            }
        } 
        return expression;
    }
}
