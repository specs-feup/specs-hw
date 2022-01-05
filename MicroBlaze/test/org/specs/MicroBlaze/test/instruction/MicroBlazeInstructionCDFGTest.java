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

import java.io.StringWriter;
import java.io.Writer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazePseudocode;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.dot.InstructionCDFGDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.fullflow.InstructionCDFGFullFlow;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.generator.InstructionCDFGGenerator;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;


public class MicroBlazeInstructionCDFGTest {

    private final String systemPath = "C:\\Users\\joaom\\Desktop\\microblazeTest\\";
    private final String wslPath = "/mnt/c/Users/joaom/Desktop/microblazeTest/";
    private final int moduleTestbenchSamples = 1000;
    
    public PseudoInstructionContext getParseTree(String pseudocode) {
        var parser = new PseudoInstructionParser(new CommonTokenStream(new PseudoInstructionLexer(new ANTLRInputStream(pseudocode))));
        return parser.pseudoInstruction();
    }
    
    @Test
    public void generateInstructionCDFGs() {
        
        System.out.println("Generating InstructionCDFG of currently implemented instructions ...\n\n");
        
        for(var instruction : MicroBlazePseudocode.values()) {
            InstructionCDFGGenerator icdfg_generator = new InstructionCDFGGenerator();
            InstructionCDFG icdfg;
            
            try {
                icdfg = icdfg_generator.generate(instruction.getParseTree());
            }catch(Exception e){
                System.out.println("ERROR: Could not generate graph of InstructionCDFG of instruction: " + instruction.getName());
                System.out.println();
                continue;
            }
            
            
            InstructionCDFGDOTExporter exp = new InstructionCDFGDOTExporter();
            
            Writer writer = new StringWriter();
            
            exp.exportGraph((GeneralFlowGraph)icdfg, instruction.getName().replace(".", "_"), writer);
            
            System.out.println("Graph of InstructionCDFG of instruction: " + instruction.getName());
            System.out.println(InstructionCDFGDOTExporter.generateGraphURL(writer.toString()));
            System.out.println();
        }
    }
    
    @Test
    public void testFullFlow() {
        
        System.out.println("Testing full flow on currently implemented instructions ...\n\n\n");
        
        for(var instruction : MicroBlazePseudocode.values()) {
            InstructionCDFGFullFlow fullFlow = new InstructionCDFGFullFlow(instruction, this.moduleTestbenchSamples, this.systemPath, this.wslPath);
            
            
        }
        
    }
}
