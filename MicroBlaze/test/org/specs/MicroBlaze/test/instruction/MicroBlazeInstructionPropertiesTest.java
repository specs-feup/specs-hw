package org.specs.MicroBlaze.test.instruction;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.instruction.MicroBlazeInstructionEncoding;
import org.specs.MicroBlaze.instruction.MicroBlazeInstructionProperties;

import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.test.instruction.InstructionEncoding;
import pt.up.fe.specs.binarytranslation.test.instruction.InstructionTestUtils;

public class MicroBlazeInstructionPropertiesTest {

    /*
     * Test checks if the manually initialized data in the MicroBlazeInstructionProperties enum
     * matches with what the parsers decode when initializing the enum private fields
     */
    @Test
    public void validitytest() {
        List<InstructionProperties> list = Arrays.asList(MicroBlazeInstructionProperties.values());
        InstructionTestUtils.validityTest(list);
    }

    /*
     * Test if all opcodes in all the instruction properties for microblaze are unique
     */
    @Test
    public void uniquenesstest() {
        List<InstructionProperties> list = Arrays.asList(MicroBlazeInstructionProperties.values());
        InstructionTestUtils.uniquenessTest(list);
    }

    /*
     * Tests if decoding of instructions happens correctly 
     * (i.e. expected decode matches inst name given its encoding)
     */
    @Test
    public void testMicroBlazeInstructionInstantiate() {
        List<InstructionEncoding> list = Arrays.asList(MicroBlazeInstructionEncoding.values());
        InstructionTestUtils.instructionInstantiate(list, MicroBlazeInstruction.class);
    }
}
