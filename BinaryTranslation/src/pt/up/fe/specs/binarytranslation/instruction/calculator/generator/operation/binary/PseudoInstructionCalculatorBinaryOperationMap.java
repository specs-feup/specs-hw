/**
 *  Copyright 2021 SPeCS.
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

package pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.arithmetic.PseudoInstructionCalculatorAddition;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.arithmetic.PseudoInstructionCalculatorDivision;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.arithmetic.PseudoInstructionCalculatorMultiplication;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.arithmetic.PseudoInstructionCalculatorShiftLeftLogical;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.arithmetic.PseudoInstructionCalculatorShiftRightArithmetic;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.arithmetic.PseudoInstructionCalculatorShiftRightLogical;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.arithmetic.PseudoInstructionCalculatorSubtraction;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.bitwise.PseudoInstructionCalculatorBitwiseAnd;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.bitwise.PseudoInstructionCalculatorBitwiseOr;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.bitwise.PseudoInstructionCalculatorBitwiseXor;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.comparison.PseudoInstructionCalculatorEqualsTo;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.comparison.PseudoInstructionCalculatorGreaterThan;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.comparison.PseudoInstructionCalculatorGreaterThanOrEqualsTo;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.comparison.PseudoInstructionCalculatorLessThan;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.comparison.PseudoInstructionCalculatorLessThanOrEqualsTo;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.comparison.PseudoInstructionCalculatorNotEqualsTo;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.logical.PseudoInstructionCalculatorLogicalAnd;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.logical.PseudoInstructionCalculatorLogicalOr;

public class PseudoInstructionCalculatorBinaryOperationMap {

    private static final Map<String, Supplier<APseudoInstructionCalculatorBinaryOperation>> MAP;

    public static APseudoInstructionCalculatorBinaryOperation get(String operator) {
        
        Supplier<APseudoInstructionCalculatorBinaryOperation> operation = MAP.get(operator);
        
        if(operation == null)
            return null;
        
        return operation.get();
    }
    
    static {
        var map = new HashMap<String, Supplier<APseudoInstructionCalculatorBinaryOperation>>();
  
        // Arithmetic 
        map.put("+", PseudoInstructionCalculatorAddition::new);
        map.put("-", PseudoInstructionCalculatorSubtraction::new);
        map.put("*", PseudoInstructionCalculatorMultiplication::new);
        map.put("/", PseudoInstructionCalculatorDivision::new);
        map.put(">>", PseudoInstructionCalculatorShiftRightLogical::new);
        map.put(">>>", PseudoInstructionCalculatorShiftRightArithmetic::new);
        map.put("<<", PseudoInstructionCalculatorShiftLeftLogical::new);
        
        // Bitwise 
        map.put("&", PseudoInstructionCalculatorBitwiseAnd::new);
        map.put("|", PseudoInstructionCalculatorBitwiseOr::new);
        map.put("^", PseudoInstructionCalculatorBitwiseXor::new);
        
        //Comparison
        map.put("==", PseudoInstructionCalculatorEqualsTo::new);
        map.put("!=", PseudoInstructionCalculatorNotEqualsTo::new);
        map.put("<", PseudoInstructionCalculatorLessThan::new);
        map.put(">", PseudoInstructionCalculatorGreaterThan::new);
        map.put("<=", PseudoInstructionCalculatorLessThanOrEqualsTo::new);
        map.put(">=", PseudoInstructionCalculatorGreaterThanOrEqualsTo::new);
        
        //Logical
        map.put("&&", PseudoInstructionCalculatorLogicalAnd::new);
        map.put("||", PseudoInstructionCalculatorLogicalOr::new);
        
        MAP = Collections.unmodifiableMap(map);
    }
    
}
