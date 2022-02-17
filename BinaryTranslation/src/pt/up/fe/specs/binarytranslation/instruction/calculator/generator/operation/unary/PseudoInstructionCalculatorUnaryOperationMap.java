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

package pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.unary;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.APseudoInstructionCalculatorBinaryOperation;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.unary.bitwise.PseudoInstructionCalculatorBitwiseNot;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.unary.function.cast.PseudoInstructionCalculatorFloatCast;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.unary.function.cast.PseudoInstructionCalculatorSignedCast;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.unary.function.cast.PseudoInstructionCalculatorUnsignedCast;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.unary.function.math.PseudoInstructionCalculatorSignExtend;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.unary.logical.PseudoInstructionCalculatorLogicalNot;

public class PseudoInstructionCalculatorUnaryOperationMap {

    private static final Map<String, Supplier<APseudoInstructionCalculatorUnaryOperation>> MAP;

    public static APseudoInstructionCalculatorUnaryOperation get(String operator) {

        Supplier<APseudoInstructionCalculatorUnaryOperation> operation = MAP.get(operator);
        
        if(operation == null)
            return null;
        
        return operation.get();
    }
    
    static {
        var map = new HashMap<String, Supplier<APseudoInstructionCalculatorUnaryOperation>>();

        // Bitwise 
        map.put("~", PseudoInstructionCalculatorBitwiseNot::new);
     
        //Logical
        map.put("!", PseudoInstructionCalculatorLogicalNot::new);
        
        //Function
        map.put("signed", PseudoInstructionCalculatorSignedCast::new);
        map.put("unsigned", PseudoInstructionCalculatorUnsignedCast::new);
        map.put("float", PseudoInstructionCalculatorFloatCast::new);
        map.put("sext", PseudoInstructionCalculatorSignExtend::new);
        
        MAP = Collections.unmodifiableMap(map);
    }
    
    
}
