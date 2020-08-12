package org.specs.Arm.asm.generators;

import org.junit.Test;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.singleinstructionmodule.SingleInstructionModuleGenerator;

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
}
