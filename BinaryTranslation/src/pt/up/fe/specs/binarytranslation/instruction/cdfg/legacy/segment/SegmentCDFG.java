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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.segment;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;

import pt.up.fe.specs.binarytranslation.detection.segments.ABinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.AGenericCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.instruction.InstructionCDFG;

/**
 * @author João Conceição
 */

public class SegmentCDFG extends AGenericCDFG{
    
    private List<InstructionCDFG> instructions_cdfg;
    private ABinarySegment segment;
    private static final String unique_node_str = "s";

    private SegmentCDFG() {
        super(unique_node_str);
    }
    
    public SegmentCDFG(List<InstructionCDFG> instructions_cdfg) {
        
        this();
        
        this.instructions_cdfg = instructions_cdfg;
       
        for(InstructionCDFG icdfg : this.instructions_cdfg) {
            this.mergeGraph(icdfg);
        }
        
    }
    
    public SegmentCDFG(ABinarySegment binary_segment) {
        
        this();
        
        InstructionCDFG icdfg;
        
        this.segment = binary_segment;
        
        this.instructions_cdfg = new ArrayList<InstructionCDFG>();
        
        for(Instruction instruction : binary_segment.getInstructions()) {
            icdfg = new InstructionCDFG(instruction);
            this.instructions_cdfg.add(icdfg);
            Graphs.addGraph(this, icdfg);
        }
        
    }
   

    public List<Instruction> getInstructions(){
        
        List<Instruction> instructions = new ArrayList<Instruction>();
        
        for(InstructionCDFG instruction_cdfg: this.instructions_cdfg) {
            instructions.add(instruction_cdfg.getInstruction());
        }
        
        return instructions;
    }
    
    public List<InstructionCDFG> getInstructionsCDFG(){
        return this.instructions_cdfg;
    }
    
    
}
