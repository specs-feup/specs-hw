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

package pt.up.fe.specs.elfsimulator;

import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;

public class ElfSimulator{
    public ElfSimulator(AStaticInstructionStream stream) {
        Map<Number, Instruction> program = streamToMap(stream);
        System.out.print(program);
        runProgram(program);
    }

    private Map<Number, Instruction> streamToMap(AStaticInstructionStream stream){
        Map<Number, Instruction> map = new HashMap<>();
        while(stream.hasNext()) {
            Instruction instruction = stream.nextInstruction();
            System.out.print(instruction.getAddress() + "  " + instruction.getRepresentation() + "\n");
            map.put(instruction.getAddress(), instruction);
        }
        return map;
    }
    
    private int runProgram(Map<Number, Instruction> program) {
        ProgramState programState = new ProgramState(); 
        int pc = 0;
        while (pc > 0) {
            InstructionSimulator instructionSim = new InstructionSimulator(
                    program.get(programState.getProgramCounter()), programState);
        }
        return 0;
    }
}
