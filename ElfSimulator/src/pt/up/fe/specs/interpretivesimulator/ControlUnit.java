/**
 *  Copyright 2020 SPeCS.
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

package pt.up.fe.specs.interpretivesimulator;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.util.SpecsLogs;

public class ControlUnit {
    
    Registers registers;
    Ram ram;
    ArithmeticLogicUnit alu;
    
    public ControlUnit(Ram ram, ArithmeticLogicUnit alu, Registers registers) {
        super();
        this.registers = registers;
        this.ram = ram;
        this.alu = alu;
    }
    
    public void process(Instruction instruction) {
        System.out.println("\n\n\n"+instruction.getAddress() + "  " + instruction.getName());
        InstructionAST ast = new InstructionAST(instruction);
        try {
            new ResolveAST(registers, ast.getRootnode());
        } catch (Exception e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }
    }
}
