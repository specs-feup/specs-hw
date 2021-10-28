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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction;

import java.io.StringWriter;
import java.io.Writer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.wip.InstructionCDFGCustomInstructionUnitGenerator;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.dot.InstructionCDFGDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.generator.InstructionCDFGGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.resolve_names.InstructionCDFGNameResolver;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;

public class RandomTest_deletethisafter {

    private class FakeInstruction implements Instruction, InstructionPseudocode{
        
        private String name;
        private String pseudocode;
        
        public FakeInstruction(String name, String pseudocode) {
            this.name = name;
            this.pseudocode = pseudocode;
        }
        
        public PseudoInstructionContext getParseTree() {
            return (new PseudoInstructionParser(new CommonTokenStream(new PseudoInstructionLexer(new ANTLRInputStream(this.pseudocode))))).pseudoInstruction();
        }
        
        public String getName() {
            return this.name;
        }

        @Override
        public String getCode() {
            return this.pseudocode;
        }
        
        @Override
        public InstructionPseudocode getPseudocode() {
            return this;
        }
        
    }
    
 
    public void printParseTree(FakeInstruction instruction) {
        PseudoInstructionContext parse = instruction.getParseTree();
        var walker = new ParseTreeWalker();
        walker.walk(new TreeDumper(), parse);
    }
    
    @Test
    public void testParse() {
        
        FakeInstruction instruction = new FakeInstruction("fakeInstructionTest", 
                //"if(RA == 2){RD = RB + RA;} if(RZ == 3){RF = RE + RJ;} RO = RD + RF;"
                "RZ = RH + 3;if(RA == RE) {RG = 0 + sext(RH);}else{RG = RH + 3;}"
                //"RD = RA + RB;RF = RA + RG;"
                );
        InstructionCDFGGenerator icdfg_gen = new InstructionCDFGGenerator();
       // this.printParseTree(instruction);
        InstructionCDFG icdf = icdfg_gen.generate(instruction);
       
        
        
        InstructionCDFGDOTExporter exp = new InstructionCDFGDOTExporter();
        
        Writer writer = new StringWriter();

        
        exp.exportGraph((GeneralFlowGraph)icdf, "s", writer);

        System.out.println("\n\nPrinting generated InstructionCDFG from instruction: " + instruction + "\n");
        //System.out.println(writer.toString());
        
        System.out.println(InstructionCDFGDOTExporter.generateGraphURL(writer.toString()));
        
        InstructionCDFGCustomInstructionUnitGenerator gen = new InstructionCDFGCustomInstructionUnitGenerator();

        //InstructionCDFGNameResolver.resolveNames(icdf); 
       gen.generateHardware(icdf).emit(System.out);
        /*    
        List<InstructionCDFG> icdfg_list = new ArrayList<>();
        
        icdfg_list.add(icdf);
        
       SegmentCDFG scdfg = new SegmentCDFG(icdfg_list);

       HardwareCDFG hcdfg = new HardwareCDFG("test",scdfg);
        
        hcdfg.print();
        */
    }
}
