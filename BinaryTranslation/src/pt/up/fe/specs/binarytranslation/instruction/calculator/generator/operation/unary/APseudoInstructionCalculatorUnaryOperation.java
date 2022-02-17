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

import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.PseudoInstructionCalculatorRegister;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.APseudoInstructionCalculatorOperation;

public abstract class APseudoInstructionCalculatorUnaryOperation extends APseudoInstructionCalculatorOperation implements Function<PseudoInstructionCalculatorRegister, Number>{

    protected boolean integerOperand(PseudoInstructionCalculatorRegister operand) {
        return (operand.getValue() instanceof Integer);
    }
    
    protected boolean floatOperand(PseudoInstructionCalculatorRegister operand) {
        return (operand.getValue() instanceof Float);
    }
    
}
