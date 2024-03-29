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
 
package org.specs.Arm.test.instruction;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplySSAPass;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;
import pt.up.fe.specs.util.treenode.utils.DottyGenerator;

public class ArmInstructionASTTest {

    // DPI_ADDSUBIMM (C4-253 to C4-254)

    // From properties
    // add(0x1100_0000, DPI_ADDSUBIMM, G_ADD),

    // From pseudocode
    // add("RD = RN + IMM;"),

    @Test
    public void dumpTree() {
        // 0x12b8: 11000422 add w2, w1, #0x1
        var addi = ArmInstruction.newInstance("12b8", "11000422");
        var dumper = new TreeDumper();
        var walker = new ParseTreeWalker();
        walker.walk(dumper, addi.getPseudocode().getParseTree());
    }

    @Test
    public void testAST() {
        // 0x12b8: 11000422 add w2, w1, #0x1
        var addi = ArmInstruction.newInstance("12b8", "11000422");
        var ast = new InstructionAST(addi);
        ast.accept(new ApplyInstructionPass());
        System.out.println(ast.getRootnode().getAsString());
    }

    @Test
    public void testASTSSA() {
        // 0x12b8: 11000422 add w2, w1, #0x1
        var addi = ArmInstruction.newInstance("12b8", "11000422");
        var ast = new InstructionAST(addi);
        var dotty1 = DottyGenerator.buildDotty(ast.getRootnode());
        System.out.println(dotty1);

        ast.accept(new ApplyInstructionPass());
        var dotty2 = DottyGenerator.buildDotty(ast.getRootnode());
        System.out.println(dotty2);

        ast.accept(new ApplySSAPass());
        var dotty3 = DottyGenerator.buildDotty(ast.getRootnode());
        System.out.println(dotty3);

        // System.out.println(ast.getRootnode().getAsString());
    }

}
