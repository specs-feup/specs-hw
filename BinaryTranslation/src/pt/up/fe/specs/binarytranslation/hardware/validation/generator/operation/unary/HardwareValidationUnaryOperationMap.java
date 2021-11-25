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

package pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.AHardwareValidationBinaryOperation;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.bitwise.HardwareValidationBitwiseNot;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.function.cast.HardwareValidationFloatCast;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.function.cast.HardwareValidationSignedCast;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.function.cast.HardwareValidationUnsignedCast;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.function.math.HardwareValidationSignExtend;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.logical.HardwareValidationLogicalNot;

public class HardwareValidationUnaryOperationMap {

    private static final Map<String, Supplier<AHardwareValidationUnaryOperation>> MAP;

    public static AHardwareValidationUnaryOperation get(String operator) {

        Supplier<AHardwareValidationUnaryOperation> operation = MAP.get(operator);
        
        if(operation == null)
            return null;
        
        return operation.get();
    }
    
    static {
        var map = new HashMap<String, Supplier<AHardwareValidationUnaryOperation>>();

        // Bitwise 
        map.put("~", HardwareValidationBitwiseNot::new);
     
        //Logical
        map.put("!", HardwareValidationLogicalNot::new);
        
        //Function
        map.put("signed", HardwareValidationSignedCast::new);
        map.put("unsigned", HardwareValidationUnsignedCast::new);
        map.put("float", HardwareValidationFloatCast::new);
        map.put("sext", HardwareValidationSignExtend::new);
        
        MAP = Collections.unmodifiableMap(map);
    }
    
    
}
