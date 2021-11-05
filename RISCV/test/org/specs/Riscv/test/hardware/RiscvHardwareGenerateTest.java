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

package org.specs.Riscv.test.hardware;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import org.specs.Riscv.instruction.RiscvPseudocode;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.wip.InstructionCDFGCustomInstructionUnitGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.generator.InstructionCDFGGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.resolve_names.InstructionCDFGNameResolver;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public class RiscvHardwareGenerateTest {

    public PseudoInstructionContext getParseTree(String pseudocode) {
        var parser = new PseudoInstructionParser(new CommonTokenStream(new PseudoInstructionLexer(new ANTLRInputStream(pseudocode))));
        return parser.pseudoInstruction();
    }
    
    @Test
    public void generateInstructionCDFGs() {
        
        
      
        for(var instruction : RiscvPseudocode.values()) {
        
        //var instruction = RiscvPseudocode.srai;
        
            InstructionCDFGGenerator icdfg_generator = new InstructionCDFGGenerator();
            InstructionCDFG icdfg = null;
            
            try {
                icdfg = icdfg_generator.generate(instruction.getParseTree());
            }catch(Exception e){
                System.out.println("ERROR: Could not generate graph of InstructionCDFG of instruction: " + instruction.getName());
                System.out.println();
                continue;
            }
            InstructionCDFGCustomInstructionUnitGenerator hardware_generator = new InstructionCDFGCustomInstructionUnitGenerator();
            
            System.out.println("\n\nProcessing instruction: " + instruction.getName() + "\n\n");

            
            
            try {
                InstructionCDFGNameResolver.resolveNames(icdfg); 
                hardware_generator.generateHardware(icdfg).emit(System.out);
            }catch(Exception e) {
                System.out.println("ERROR: Could not generate HDL of instruction: " + instruction.getName());
                continue;
            }
        }
        
        System.out.println("DONE");
    }
    
    
}
