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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.segment;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.generator.InstructionCDFGGenerator;

public class SegmentCDFG extends InstructionCDFG{

    private List<Instruction> instructions;
    
    public SegmentCDFG(List<Instruction> instructions) {
        super(null);
        this.instructions = instructions;
    }

    public void generate() {

        this.instructions.forEach(instruction -> {
            InstructionCDFGGenerator icdfg_gen = new InstructionCDFGGenerator();
            Graphs.addGraph(this, icdfg_gen.generate(instruction));
        });
        
        this.generateInputs();
        this.generateOutputs();
        
    }
    
    @Override
    public Instruction getInstruction() {
        return this.instructions.get(0);
    }
    
    public List<Instruction> getInstructions() {
        return this.instructions;
    }
}
