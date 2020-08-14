package org.specs.Arm.asm.generators;

import org.junit.Test;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.singleinstructionmodule.SingleInstructionModuleGenerator;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;

public class ArmSingleInstructionModuleGeneratorTester {

    // DPI_ADDSUBIMM (C4-253 to C4-254)

    // From properties
    // add(0x1100_0000, DPI_ADDSUBIMM, G_ADD),

    // From pseudocode
    // add("RD = RN + IMM"),

    @Test
    public void testAddUnit() {
        // 0x12b8: 11000422 add w2, w1, #0x1
        var addi = ArmInstruction.newInstance("12b8", "11000422");
        var singleUnitBuilder = new SingleInstructionModuleGenerator();
        var unit = singleUnitBuilder.generateHarware(addi);
        unit.emit(System.out);
    }

    @Test
    public void testAddUnit_b() {
        // 0x3bc4: 11001084 add w4, w4, #0x4
        var add = ArmInstruction.newInstance("3bc4", "11001084");
        var singleUnitBuilder = new SingleInstructionModuleGenerator();
        var unit = singleUnitBuilder.generateHarware(add);
        unit.emit(System.out);
    }

    @Test
    public void testUbfmUnit() {
        // 0x3bc0: d37ff800 lsl x0, x0, #1 --> alias ubfm
        var ubfm = ArmInstruction.newInstance("3bc0", "d37ff800");
        var singleUnitBuilder = new SingleInstructionModuleGenerator();
        var unit = singleUnitBuilder.generateHarware(ubfm);
        unit.emit(System.out);
    }

    @Test
    public void testAndsUnit() {
        // 0x3bb0: ea02001f ands sp, x2 --> alias tst
        var ands = ArmInstruction.newInstance("3bb0", "ea02001f");
        var singleUnitBuilder = new SingleInstructionModuleGenerator();
        var unit = singleUnitBuilder.generateHarware(ands);
        unit.emit(System.out);
    }

    @Test
    public void testBeqUnit() {
        // 0xcf4: 54ffffa0 b.eq ce8 <_mainCRTStartup+0x70> // b.none
        var beq = ArmInstruction.newInstance("cf4", "54ffffa0");

        var ast = new InstructionAST(beq);
        ast.accept(new ApplyInstructionPass());
        System.out.println(ast.getRootnode().getAsString());

        var singleUnitBuilder = new SingleInstructionModuleGenerator();
        var unit = singleUnitBuilder.generateHarware(beq);
        unit.emit(System.out);
    }

    /*  
    54ffffa0*/
}
