package org.specs.Arm.asm.instruction;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplySSAPass;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;

public class ArmParseTreeTester {

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
        ast.accept(new ApplyInstructionPass());
        ast.accept(new ApplySSAPass());
        System.out.println(ast.getRootnode().getAsString());
    }

}
