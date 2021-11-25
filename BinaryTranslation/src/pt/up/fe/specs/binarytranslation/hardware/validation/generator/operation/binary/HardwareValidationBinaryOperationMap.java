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

package pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.arithmetic.HardwareValidationAddition;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.arithmetic.HardwareValidationDivision;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.arithmetic.HardwareValidationMultiplication;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.arithmetic.HardwareValidationShiftLeftLogical;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.arithmetic.HardwareValidationShiftRightArithmetic;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.arithmetic.HardwareValidationShiftRightLogical;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.arithmetic.HardwareValidationSubtraction;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.bitwise.HardwareValidationBitwiseAnd;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.bitwise.HardwareValidationBitwiseOr;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.bitwise.HardwareValidationBitwiseXor;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.comparison.HardwareValidationEqualsTo;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.comparison.HardwareValidationGreaterThan;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.comparison.HardwareValidationGreaterThanOrEqualsTo;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.comparison.HardwareValidationLessThan;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.comparison.HardwareValidationLessThanOrEqualsTo;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.comparison.HardwareValidationNotEqualsTo;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.logical.HardwareValidationLogicalAnd;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.logical.HardwareValidationLogicalOr;

public class HardwareValidationBinaryOperationMap {

    private static final Map<String, Supplier<AHardwareValidationBinaryOperation>> MAP;

    public static AHardwareValidationBinaryOperation get(String operator) {
        
        Supplier<AHardwareValidationBinaryOperation> operation = MAP.get(operator);
        
        if(operation == null)
            return null;
        
        return operation.get();
    }
    
    static {
        var map = new HashMap<String, Supplier<AHardwareValidationBinaryOperation>>();
  
        // Arithmetic 
        map.put("+", HardwareValidationAddition::new);
        map.put("-", HardwareValidationSubtraction::new);
        map.put("*", HardwareValidationMultiplication::new);
        map.put("/", HardwareValidationDivision::new);
        map.put(">>", HardwareValidationShiftRightLogical::new);
        map.put(">>>", HardwareValidationShiftRightArithmetic::new);
        map.put("<<", HardwareValidationShiftLeftLogical::new);
        
        // Bitwise 
        map.put("&", HardwareValidationBitwiseAnd::new);
        map.put("|", HardwareValidationBitwiseOr::new);
        map.put("^", HardwareValidationBitwiseXor::new);
        
        //Comparison
        map.put("==", HardwareValidationEqualsTo::new);
        map.put("!=", HardwareValidationNotEqualsTo::new);
        map.put("<", HardwareValidationLessThan::new);
        map.put(">", HardwareValidationGreaterThan::new);
        map.put("<=", HardwareValidationLessThanOrEqualsTo::new);
        map.put(">=", HardwareValidationGreaterThanOrEqualsTo::new);
        
        //Logical
        map.put("&&", HardwareValidationLogicalAnd::new);
        map.put("||", HardwareValidationLogicalOr::new);
        
        MAP = Collections.unmodifiableMap(map);
    }
    
}
