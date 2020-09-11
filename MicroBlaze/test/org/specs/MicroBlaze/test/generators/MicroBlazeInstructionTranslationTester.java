package org.specs.MicroBlaze.test.generators;

import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.instruction.MicroBlazeInstructionProperties;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.singleinstructionmodule.SingleInstructionModuleGenerator;

/**
 * Test the generation of SingleInstructionUnit modules for the entire MicroBlaze ISA.
 * 
 * @author nuno
 *
 */
public class MicroBlazeInstructionTranslationTester {

    @Test
    public void testTypeB() {

        var singleUnitBuilder = new SingleInstructionModuleGenerator();

        // for (var props : MicroBlazeInstructionProperties.values()) {
        for (var props : Arrays.asList(MicroBlazeInstructionProperties.addic)) {
            if (props.getCodeType() == MicroBlazeAsmFieldType.TYPE_B) {
                var inst = MicroBlazeInstruction.newInstance("0", Integer.toHexString(props.getOpCode()));
                var unit = singleUnitBuilder.generateHarware(inst);
                unit.emit(System.out);
            }
        }
    }
}
