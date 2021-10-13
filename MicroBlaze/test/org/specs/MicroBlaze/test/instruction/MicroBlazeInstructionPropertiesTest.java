/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
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
