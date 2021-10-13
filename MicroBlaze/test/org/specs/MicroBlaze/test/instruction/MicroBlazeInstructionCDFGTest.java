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

package org.specs.MicroBlaze.test.instruction;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.instruction.MicroBlazeInstructionProperties;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.SegmentCDFG;

public class MicroBlazeInstructionCDFGTest {

    private static final MicroBlazeInstructionProperties instruction = MicroBlazeInstructionProperties.idiv;
    
   @Test
   public void generateFromInstruction() {
       
       Instruction inst = MicroBlazeInstruction.newInstance("0", Integer.toHexString(instruction.getOpCode()));
       
       System.out.println("Testing generating InstructionCDFG from Instruction...\n\n");
       System.out.println(inst);
       
       
       InstructionCDFG icdfg = new InstructionCDFG(inst);
     
       icdfg.toDot("test"); 
       
       System.out.println("Done\n\n");
   }
   
   @Test
   public void generateFromInstructionAST() {
       
       Instruction inst = MicroBlazeInstruction.newInstance("0", Integer.toHexString(instruction.getOpCode()));
   
       System.out.println("Testing generating InstructionCDFG from InstructionAST...\n\n");
       System.out.println(inst);
       
       InstructionAST iast = new InstructionAST(inst);
       System.out.println(iast);
       //ApplyInstructionPass iast_aip = new ApplyInstructionPass();
       //iast_aip.visit(iast);
       
       InstructionCDFG icdfg = new InstructionCDFG(iast);
       
       

       icdfg.toDot("test");
       System.out.println("Done\n\n");
   }
    
   
   @Test
   public void generateSegmentCDFG() {
       
       Instruction inst = MicroBlazeInstruction.newInstance("0", Integer.toHexString(instruction.getOpCode()));
       
       System.out.println("Testing generating SegmentCDFG from InstructionAST...\n\n");
       System.out.println(inst);
       
       InstructionAST iast = new InstructionAST(inst);
       //ApplyInstructionPass iast_aip = new ApplyInstructionPass();
      // iast_aip.visit(iast);
       
       List<InstructionCDFG> icdfg_list = new ArrayList<InstructionCDFG>();
       
       icdfg_list.add(new InstructionCDFG(iast));
       icdfg_list.add(new InstructionCDFG(iast));
      
       SegmentCDFG scdfg = new SegmentCDFG(icdfg_list);
       scdfg.toDot(null);
   }
   
}
