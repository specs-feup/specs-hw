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

import java.io.File;

import pt.up.fe.specs.util.SpecsIo;

/**
 * Methods for generating a CPP Simulator of an ELF File Execution from
 * a Instruction Stream [1] of the file and a description of the Instruction Set
 * 
 * [1](pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream)
 * 
 * @author j-vm
 *
 */
public interface SimulatorGenerationOutput {
    
    public default String getOutputFolderName() {
        return this.getClass().getSimpleName() + this.hashCode();
    }

    public default File getOutputFolder() {
        return SpecsIo.mkdir("./output/" + this.getOutputFolderName());
    }
    
    /*
     * Generates an Instruction Set Specific .h file describing
     * each instruction
     */
    public default void generateISLib() {
        generateISLib(this.getOutputFolder());
    }
    
    public default void generateISLib(File parentfolder) {
        SpecsIo.mkdir(parentfolder);
        
    }
    
    /*
     * Generates the cpp file for the ELF file Execution Simulation
     * listing the program asm instructions 
     */
    public default void generateELFSim() {
        generateELFSim(this.getOutputFolder());
    }   
    
    public default void generateELFSim(File parentfolder) {
        
    }
    
    /*
     * Generates both the InstructionSetLibrary and the ELFSimulator files
     */
    public default void generateISS() {
        generateISS(this.getOutputFolder());
    }
    
    public default void generateISS(File parentfolder) {
        generateISLib(parentfolder);
        generateELFSim(parentfolder);
    }
    
    
}
