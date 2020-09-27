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

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;

public class InstructionSetLibGenerator {
    public InstructionSetLibGenerator(AStaticInstructionStream stream) {
            System.out.print(stream.getNumInstructions() + "\n" );
            int unknownCounter = 0;
            while(stream.hasNext()) {
                Instruction instruction = stream.nextInstruction();
                //System.out.print(instruction.getName() + "\n");
                if(instruction.isUnknown()) 
                    unknownCounter++;
                else {
                    System.out.print(instruction.getRepresentation() + "\n");
                    System.out.print("\t" + instruction.getFieldData().getType() + "\n");
                    System.out.print("\t" + instruction.getFieldData().getFields() + "\n\n");
                }
            }
            System.out.print("UNKNOWN INSTRUCTIONS = " + unknownCounter + "\n");
            System.out.print(stream.getNumInstructions() + "\n");
    }
    
   
}
