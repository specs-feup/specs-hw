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

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.dot.InstructionCDFGDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.resolve_names.InstructionCDFGNameResolver;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public class TestSegemn {


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
    
    @Test
    public void generate() {
        
        List<Instruction> instructions = List.of(
                new FakeInstruction("fakeInstructionTest", "RD = RA + RC;"),
                new FakeInstruction("fakeInstructionTest", "RZ = RC - RF;")
                );
        
        SegmentCDFG segment = new SegmentCDFG(instructions);
        
        segment.generate();
        
        System.out.println(segment.getDataInputsMap().keySet());
        
        InstructionCDFGDOTExporter exp = new InstructionCDFGDOTExporter();
        
        Writer writer = new StringWriter();
        InstructionCDFGNameResolver.resolveNames(segment);
        exp.exportGraph((GeneralFlowGraph)segment, "s", writer);
        System.out.println(InstructionCDFGDOTExporter.generateGraphURL(writer.toString()));
        
    }
    
}
