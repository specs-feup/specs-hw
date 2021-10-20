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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.dot.InstructionCDFGDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.generator.InstructionCDFGGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.segment.SegmentCDFG;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;

public class RandomTest_deletethisafter {

    public PseudoInstructionContext getParseTree(String pseudocode) {
        var parser = new PseudoInstructionParser(new CommonTokenStream(new PseudoInstructionLexer(new ANTLRInputStream(pseudocode))));
        return parser.pseudoInstruction();
    }
    /*
  
    public InstructionCDFG generateICDFG(String instruction) {
        
        PseudoInstructionContext parse = this.getParseTree(instruction);
        
        InstructionCDFG icdfg = new InstructionCDFG();
        icdfg.mergeGraph(((InstructionCDFGGenerator)icdfg.getGenerator()).generate(parse));
        icdfg.toDot(null);
        
        return icdfg;
    }
    */
    public void printParseTree(String instruction) {
        PseudoInstructionContext parse = this.getParseTree(instruction);
        var walker = new ParseTreeWalker();
        walker.walk(new TreeDumper(), parse);
    }
    
    @Test
    public void testParse() {
        
   
        String instruction = "if(RA == IMM) {RB = RC + RD;}else {if(RA < IMM) {RB = RC + RD;}}";
      
        //String instruction = "if(RE + RF -IMM) {RD = RB+RC;$dzo = 1;}";
        
        InstructionCDFGGenerator icdfg_gen = new InstructionCDFGGenerator();
        this.printParseTree(instruction);
        InstructionCDFG icdf = icdfg_gen.generate(this.getParseTree(instruction));
       
        
        
        InstructionCDFGDOTExporter exp = new InstructionCDFGDOTExporter();
        
        Writer writer = new StringWriter();
        
        System.out.println(icdf.getOutputs());
        
        exp.exportGraph((GeneralFlowGraph)icdf, "s", writer);

        
        System.out.println(writer.toString());
        
        List<InstructionCDFG> icdfg_list = new ArrayList<>();
        
        icdfg_list.add(icdf);
        
       SegmentCDFG scdfg = new SegmentCDFG(icdfg_list);

       /*  HardwareCDFG hcdfg = new HardwareCDFG("test",scdfg);
        
        hcdfg.print();
        */
    }
}
