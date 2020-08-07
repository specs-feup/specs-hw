package org.specs.MicroBlaze.test.instruction;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.test.instruction.InstructionEncoding;
import pt.up.fe.specs.binarytranslation.test.instruction.InstructionTestUtils;

public class MicroBlazeInstructionInstantiationTester {

    @Test
    public void testMicroBlazeInstructionInstantiate() {
        List<InstructionEncoding> list = Arrays.asList(MicroBlazeInstructionEncoding.values());
        InstructionTestUtils.instructionInstantiate(list, MicroBlazeInstruction.class);
    }
}
