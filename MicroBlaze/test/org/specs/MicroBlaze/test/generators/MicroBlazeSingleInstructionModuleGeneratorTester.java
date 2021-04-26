package org.specs.MicroBlaze.test.generators;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.singleinstructionmodule.SingleInstructionModuleGenerator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.util.treenode.utils.DottyGenerator;

public class MicroBlazeSingleInstructionModuleGeneratorTester {

    @Test
    public void testAddiUnit() {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");
        var singleUnitBuilder = new SingleInstructionModuleGenerator();
        var unit = singleUnitBuilder.generateHarware(addi);
        // unit.emit(System.out);

        // new dotty generation test
        var dottygen = new DottyGenerator<HardwareNode>();
        dottygen.generateDotty(unit.getTree().getRoot());
    }
}
