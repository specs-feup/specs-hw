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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;

public class Program {
    private final List<Instruction> instructions = new ArrayList<>();;
    private final int startingInstructionAddress;
    private final int instructionSize;
    
    private int currentIndex = 0;
    
    public Program(AStaticInstructionStream stream) {
        Instruction firstInstruction = stream.nextInstruction();
        Instruction secondInstruction = stream.nextInstruction();
        this.startingInstructionAddress = (int) firstInstruction.getAddress();
        this.instructionSize = this.startingInstructionAddress - (int) secondInstruction.getAddress();
        this.instructions.add(firstInstruction);
        this.instructions.add(secondInstruction);
        while(stream.hasNext()) {
            this.instructions.add(stream.nextInstruction());
        }
    }
    
    public Instruction next() {
        return instructions.get(currentIndex++);
    }
    
    public boolean hasNext()  {
        return currentIndex < instructions.size();
    }
    
    public void jump(Number address) {
        currentIndex = ((int) address - startingInstructionAddress) / instructionSize;
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
