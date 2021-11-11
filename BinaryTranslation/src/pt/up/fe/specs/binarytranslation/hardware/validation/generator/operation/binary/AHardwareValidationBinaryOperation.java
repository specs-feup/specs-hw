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

public abstract class AHardwareValidationBinaryOperation implements BiFunction<Number, Number, Number>{

    protected boolean integerOperands(Number operandLeft, Number operandRight) {
        return ((operandLeft instanceof Integer) && (operandRight instanceof Integer));
    }
    
    protected boolean floatOperands(Number operandLeft, Number operandRight) {
        return ((operandLeft instanceof Float) && (operandRight instanceof Float));
    }
    
}
