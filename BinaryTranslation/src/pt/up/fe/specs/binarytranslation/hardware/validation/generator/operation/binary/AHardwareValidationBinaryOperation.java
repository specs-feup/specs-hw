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

import java.util.function.BiFunction;

import pt.up.fe.specs.binarytranslation.hardware.validation.generator.HardwareValidationRegister;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.AHardwareValidationOperation;

public abstract class AHardwareValidationBinaryOperation extends AHardwareValidationOperation implements BiFunction<HardwareValidationRegister, HardwareValidationRegister, Number>{

    protected boolean integerOperands(HardwareValidationRegister operandLeft, HardwareValidationRegister operandRight) {
        return ((operandLeft.getValue() instanceof Integer) && (operandRight.getValue() instanceof Integer));
    }
    
    protected boolean floatOperands(HardwareValidationRegister operandLeft, HardwareValidationRegister operandRight) {
        return ((operandLeft.getValue() instanceof Float) && (operandRight.getValue() instanceof Float));
    }
    
}
