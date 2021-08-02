/**
 * Copyright 2020 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.interpretivesimulator;

import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;

public class Program {
    private final Map<Integer, Instruction> instructions = new HashMap<>();
    private final int startingInstructionAddress;
    private final int instructionWidth;

    public Program(AStaticInstructionStream stream) {

        Instruction firstInstruction = stream.nextInstruction();

        this.startingInstructionAddress = firstInstruction.getAddress().intValue();
        this.instructions.put(this.startingInstructionAddress, firstInstruction);

        this.instructionWidth = stream.getInstructionWidth();

        Instruction instruction = null;
        while ((instruction = stream.nextInstruction()) != null) {
            this.instructions.put(instruction.getAddress().intValue(), instruction);
        }
    }

    public Instruction getByAddress(int programCounter) {
        return instructions.get(programCounter);
    }

    public int getInstructionWidth() {
        return instructionWidth;
    }

    public int getStartingInstructionAddress() {
        return startingInstructionAddress;
    }

}

/*public class Program {
    private List<Instruction> instructions;
    private Map<Number, Integer> addressAccess;
    private Number startingInstructionAddress = null;
    
    public Program(AStaticInstructionStream stream) {
        this.instructions = new ArrayList<>();
        addressAccess = new HashMap<>();
        int iterator = 0;
        while(stream.hasNext()) {
            
            Instruction instruction = stream.nextInstruction();
            if(startingInstructionAddress == null) startingInstructionAddress = instruction.getAddress();
            
            addressAccess
            System.out.println(instruction.getAddress() + "  " + instruction.getRepresentation());
            this.instructions.add(instruction);
        }
    }
    
    
    
}*/
