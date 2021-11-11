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

package pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.comparison;

public class HardwareValidationGreaterThanOrEqualsTo extends AHardwareValidationComparisonOperation{

    @Override
    public Number apply(Number operandLeft, Number operandRight) throws IllegalArgumentException{
        
        if(super.integerOperands(operandLeft, operandRight)) {
            return Integer.valueOf(operandLeft.intValue() >= operandRight.intValue() ? 1 : 0);
        }else if(super.floatOperands(operandLeft, operandRight)) {
            return Integer.valueOf(Float.compare(operandLeft.floatValue(), operandRight.floatValue()) >= 0 ? 1 : 0);
        }
        
        throw new IllegalArgumentException();
    }

}