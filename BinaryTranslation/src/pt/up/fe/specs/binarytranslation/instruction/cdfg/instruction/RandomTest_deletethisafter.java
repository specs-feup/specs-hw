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

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.fullflow.InstructionCDFGFullFlow;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;

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
        
        public void printParseTree() {
            PseudoInstructionContext parse = this.getParseTree();
            var walker = new ParseTreeWalker();
            walker.walk(new TreeDumper(), parse);
        }
        
    }
    
    private FakeInstruction instruction = new FakeInstruction("addTest",  
            "RD = RA + RB;"
            //"if(RA == 0) {RD = RA + RB;}RD = RA + RB;if(RA == 0) {RD = RA + RB;}"
            //"if(RA == 0) {RD = RA + RB; if(RA == 0){RD = RA + RB;}else{RD = RA + RB;}}"
            //"if(RA == 0) {RD = RA - RB; RA =  RG + 3;}"
            );
    
    private final String systemPath = "/home/soiboi/Desktop/" + "btf_";
    private final String wslPath = "/home/soiboi/Desktop/" + "btf_";
    private final int moduleTestbenchSamples = 1;
    
    @Test
    public void testFullFlow() throws IOException {

        InstructionCDFGFullFlow fullFlow = new InstructionCDFGFullFlow(this.instruction, this.moduleTestbenchSamples, this.systemPath, this.wslPath);
        
        fullFlow.runAll();
        
    }
    
}
