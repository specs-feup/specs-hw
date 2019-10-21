package org.specs.MicroBlaze.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.MicroBlaze.isa.MicroBlazeInstructionProperties;

public class MicroBlazeInstructionPropertiesTester {

    /*
     * Test checks if the manually initialized data in the MicroBlazeInstructionProperties enum
     * matches with what the parsers decode when initializing the enum private fields
     */
    @Test
    public void validitytest() {
        for (MicroBlazeInstructionProperties props : MicroBlazeInstructionProperties.values()) {
            var v1 = props.getCodeType().toString();
            var v2 = props.getFieldData().getType().toString();
            System.out.print(props.name() + ":\t\t" + v1 + "\tvs.\t" + v2 + "\n");
            assertEquals(v1, v2);
        }
    }

    /*
     * Test if all opcodes in all the instructionsproperties for microblaze are unique
     */
    @Test
    public void uniquenesstest() {
        int matchcount = 0;
        for (MicroBlazeInstructionProperties first : MicroBlazeInstructionProperties.values()) {
            matchcount = 0;
            for (MicroBlazeInstructionProperties second : MicroBlazeInstructionProperties.values()) {
                if (first.getOpCode() == second.getOpCode())
                    matchcount++;
            }
            System.out.print("props: " + first.getName() + " matches = " + matchcount + "\n");
            assertEquals(matchcount, 1);
        }
    }
}
