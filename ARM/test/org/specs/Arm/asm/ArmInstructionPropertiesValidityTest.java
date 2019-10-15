package org.specs.Arm.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.Arm.isa.ArmInstructionProperties;

public class ArmInstructionPropertiesValidityTest {

    /*
     * Test checks if the manually initialized data in the ArmInstructionProperties enum
     * matches with what the parsers decode when initializing the enum private fields
     */
    @Test
    public void test() {
        for (ArmInstructionProperties props : ArmInstructionProperties.values()) {
            var v1 = props.getCodeType().toString();
            var v2 = props.getFieldData().getType().toString();
            System.out.print(props.name() + ":\t\t" + v1 + "\tvs.\t" + v2 + "\n");
            assertEquals(v1, v2);
        }
    }
}
