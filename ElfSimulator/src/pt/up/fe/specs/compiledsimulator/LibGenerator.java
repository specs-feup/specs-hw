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

package pt.up.fe.specs.compiledsimulator;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class LibGenerator {
    private InstructionStream stream; 
    private InstructionSetSpecifications specifications;
    
    public LibGenerator(InstructionStream stream, InstructionSetSpecifications specifications) {
        this.stream = stream;
        this.specifications = specifications;
    }
    
    public void generateLib() {
        ArrayList<UniqueInstrucion> instructions = getUniqueInstrucitons(stream);
        //TODO: Extract to file Printer with templates
        try { 
            BufferedWriter writerCPP = Files.newBufferedWriter(Paths.get(specifications.getName() + ".cpp"));
            BufferedWriter writerH = Files.newBufferedWriter(Paths.get(specifications.getName() + ".h")); 
            
            writerCPP.write("#include " + specifications.getName() + ".h\n\n");
            writerCPP.write("int pc = -1;\n");
            for(Map.Entry<String, Integer> entry : specifications.getRegisters().entrySet())
                writerCPP.write("int " + entry.getKey() + " = " + entry.getValue() + ";\n");
                
                writerCPP.write("\n\n\n");
            
            for(UniqueInstrucion instruction : instructions) {
                String declaration = "int " + instruction.getName() + "(";
         
                for(Operand operand : instruction.getOperands()) {
                    if(operand != instruction.getOperands().get(0))
                        declaration += ", ";
                    if(operand.isRegister()) declaration += "*";
                    declaration += operand.getAsmField().toString();
                }
                declaration += ")";
                writerH.write(declaration + ";\n\n");
                writerCPP.write( declaration + "{\n\t" + instruction.getCcode() + "}\n\n" );
                
            }
            writerCPP.write("\n /*" + specifications.getName().toUpperCase() + "*/");
            writerCPP.close();
            writerH.close();
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        
    }

    private ArrayList<UniqueInstrucion> getUniqueInstrucitons(InstructionStream stream) {
        ArrayList<UniqueInstrucion> instructions =  new ArrayList<>();
        Set<String> instructionNames = new HashSet<>();
        int unknownCounter = 0;
        
        Instruction instruction = stream.nextInstruction();
        
        while(instruction != null) {
            if(instruction.isUnknown()) 
                unknownCounter++;
            else {
                 if(!instructionNames.contains(instruction.getName())){
                     instructions.add(new UniqueInstrucion(instruction));
                     instructionNames.add(instruction.getName());
                 }
            }
            instruction = stream.nextInstruction();
        }
        System.out.println("Number of Unknown Instructions = " + unknownCounter);
        return instructions;
    }
    
}
