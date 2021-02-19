package org.specs.Riscv.test.instruction;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.specs.Riscv.instruction.RiscvInstructionProperties;

import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.test.instruction.InstructionTestUtils;

public class RiscvInstructionPropertiesTest {

    /*
     * Test checks if the manually initialized data in the RiscvInstructionProperties enum
     * matches with what the parsers decode when initializing the enum private fields
     */
    @Test
    public void validitytest() {
        List<InstructionProperties> list = Arrays.asList(RiscvInstructionProperties.values());
        InstructionTestUtils.validityTest(list);
    }

    /*
     * Test if all opcodes in all the instruction properties for riscv are unique
     */
    @Test
    public void uniquenesstest() {
        List<InstructionProperties> list = Arrays.asList(RiscvInstructionProperties.values());
        InstructionTestUtils.uniquenessTest(list);
    }

    /*
     * Tests if decoding of instructions happens correctly 
     * (i.e. expected decode matches inst name given its encoding)
     */
    /*@Test
    public void testMicroBlazeInstructionInstantiate() {
        List<InstructionEncoding> list = Arrays.asList(MicroBlazeInstructionEncoding.values());
        InstructionTestUtils.instructionInstantiate(list, MicroBlazeInstruction.class);
    }*/
}
