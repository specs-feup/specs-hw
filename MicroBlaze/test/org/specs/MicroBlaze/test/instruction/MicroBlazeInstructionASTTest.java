/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package org.specs.MicroBlaze.test.instruction;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.instruction.MicroBlazeInstructionProperties;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplySSAPass;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;
import pt.up.fe.specs.util.treenode.utils.DottyGenerator;

public class MicroBlazeInstructionASTTest {

    @Test
    public void dumpAddiTree() {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");
        var dumper = new TreeDumper();
        var walker = new ParseTreeWalker();
        walker.walk(dumper, addi.getPseudocode().getParseTree());
    }

    @Test
    public void dumpAddiAST() {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");
        var ast = new InstructionAST(addi);
        System.out.println(ast.toString());
        System.out.println(ast.getRootnode().getAsString());
    }

    @Test
    public void testAddiAST() {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");

        var ast = new InstructionAST(addi);
        var dotty1 = DottyGenerator.buildDotty(ast.getRootnode());
        System.out.println(dotty1);

        ast.accept(new ApplyInstructionPass());
        var dotty2 = DottyGenerator.buildDotty(ast.getRootnode());
        System.out.println(dotty2);

        // System.out.println(ast.toString());
        // System.out.println(ast.getRootnode().getAsString());
    }

    @Test
    public void testAddiSSAPass() {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");
        var ast = new InstructionAST(addi);
        ast.accept(new ApplyInstructionPass());
        ast.accept(new ApplySSAPass());
        System.out.println(ast.toString());
        System.out.println(ast.getRootnode().getAsString());
    }

    private void testInstructionASTGen(Instruction inst) {

        System.out.println("----------------------------");
        System.out.println("--- Tree for: " + inst.getRepresentation());
        var dumper = new TreeDumper();
        var walker = new ParseTreeWalker();
        walker.walk(dumper, inst.getPseudocode().getParseTree());

        var ast = new InstructionAST(inst);
        System.out.println("--- Decode for: " + inst.getRepresentation());
        System.out.println(ast.getRootnode().getAsString());
    }

    private void testASTTYpe(MicroBlazeAsmFieldType type) {
        for (var props : MicroBlazeInstructionProperties.values()) {
            if (props.getCodeType() == type) {
                var inst = MicroBlazeInstruction.newInstance("0", Integer.toHexString(props.getOpCode()));
                testInstructionASTGen(inst);
            }
        }
    }

    @Test
    public void testAddc() {
        var props = MicroBlazeInstructionProperties.addc;
        var inst = MicroBlazeInstruction.newInstance("0", Integer.toHexString(props.getOpCode()));
        testInstructionASTGen(inst);
    }

    @Test
    public void testCmp() {
        var props = MicroBlazeInstructionProperties.cmp;
        var inst = MicroBlazeInstruction.newInstance("0", Integer.toHexString(props.getOpCode()));
        testInstructionASTGen(inst);
    }

    /**
     * Tests if the pseudo-instruction grammar and InstructionAST generator are working properly for all instructions in
     * this ISA (by type)
     */
    @Test
    public void testAllMicroBlazeASTs() {
        for (var type : MicroBlazeAsmFieldType.values()) {
            this.testASTTYpe(type);
        }
    }
}
