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

package pt.up.fe.specs.elfsimulator.test;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.specs.MicroBlaze.stream.*;
import org.specs.Riscv.stream.RiscvElfStream;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.elfsimulator.InstructionSetLibGenerator;

public class TestSimulator {

    private File openMBFile() {

        // static
        // File fd = SpecsIo.resourceCopy("org/specs/elfsimulator/microblaze/matmul.txt");
        // File fd = SpecsIo.resourceCopy("org/specs/elfsimulator/microblaze/innerprod.txt");
        File fd = SpecsIo.resourceCopy("org/specs/elfsimulator/microblaze/cholesky.txt");
        // File fd = SpecsIo.resourceCopy("org/specs/elfsimulator/microblaze/innerprod.txt");

        System.out.print(fd.exists() + "\n");
        fd.deleteOnExit();
        return fd;
    }
    private File openRISCVFile() {

        // static

        File fd = SpecsIo.resourceCopy("org/specs/elfsimulator/riscv/test64.txt");

        System.out.print(fd.exists() + "\n");
        fd.deleteOnExit();
        return fd;
    }
    
    @Test
    void loadMicroBlazeElfTest() {
       MicroBlazeElfStream stream = new MicroBlazeElfStream(openMBFile());
       var newinst = stream.nextInstruction();
       while (newinst != null) {

           //Print Address
           System.out.print(newinst.getAddress() + " >\t");
           
           //Print Plain Name
           InstructionData instData = newinst.getData();
           String plainName = instData.getPlainName();
           System.out.print( plainName + " >\t");
           
           //Print Operands
           List<Operand> operands = instData.getOperands();
           operands.forEach((operand)-> System.out.print(operand.getRepresentation() + "|"));
           
           //Print Generic Types
           List<InstructionType> genTypes = instData.getGenericTypes();
           System.out.print( "\t" + genTypes + "\n");
           System.out.print(newinst.getPseudocode().toString() +"\n"); 
           
           newinst = stream.nextInstruction();
       }
    }
    
    @Test
    void loadRiscvElfTest() {
        RiscvElfStream stream = new RiscvElfStream(openRISCVFile());
        var newinst = stream.nextInstruction();
        while (newinst != null) {
            
            //Print Address
            System.out.print(newinst.getAddress() + " >\t");
            
            //Print Plain Name
            InstructionData instData = newinst.getData();
            String plainName = instData.getPlainName();
            //System.out.print( plainName + " >\t");
            
            System.out.print(newinst.getPseudocode().toString() +"\n"); 
            
            //Print Operands
            List<Operand> operands = instData.getOperands();
            //operands.forEach((operand)-> System.out.print(operand.getRepresentation() + "|"));
            
            //Print Generic Types
            List<InstructionType> genTypes = instData.getGenericTypes();
            //System.out.print( "\t" + genTypes + "\n");

            newinst = stream.nextInstruction();
        }
    }
    
    @Test
    void InstructionSetLibGeneratorTest() {
        MicroBlazeElfStream stream = new MicroBlazeElfStream(openMBFile());
        //RiscvElfStream stream = new RiscvElfStream(openRISCVFile());
        InstructionSetLibGenerator islg = new InstructionSetLibGenerator(stream);
    }

}
