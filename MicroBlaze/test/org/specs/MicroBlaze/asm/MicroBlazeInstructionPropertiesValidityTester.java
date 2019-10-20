package org.specs.MicroBlaze.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.MicroBlaze.isa.MicroBlazeInstructionProperties;

public class MicroBlazeInstructionPropertiesValidityTester {

    /*
     * Test checks if the manually initialized data in the MicroBlazeInstructionProperties enum
     * matches with what the parsers decode when initializing the enum private fields
     */
    @Test
    public void test() {
        for (MicroBlazeInstructionProperties props : MicroBlazeInstructionProperties.values()) {
            var v1 = props.getCodeType().toString();
            var v2 = props.getFieldData().getType().toString();
            System.out.print(props.name() + ":\t\t" + v1 + "\tvs.\t" + v2 + "\n");
            assertEquals(v1, v2);
        }
    }
}
