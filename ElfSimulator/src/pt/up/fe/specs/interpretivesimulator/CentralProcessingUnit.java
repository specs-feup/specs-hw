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


import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;


public class CentralProcessingUnit {
    
    ControlUnit cu;
    ArithmeticLogicUnit alu;
    Registers registers;
    Ram ram;
    
    public CentralProcessingUnit(Ram ram) {
        this.ram = ram;
        this.alu = new ArithmeticLogicUnit();
        this.registers = new Registers();
        this.cu = new ControlUnit(this.ram, this.alu, this.registers);
    }
    
    public void runProgram(Program program) {
        while(program.hasNext()) {
            Number jump = this.cu.process(program.next());
            if((int)jump != -1) program.jump(jump);
        }
    }
    
}
