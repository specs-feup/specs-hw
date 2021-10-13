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
 
package org.specs.Arm.test.instruction;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.specs.Arm.instruction.ArmInstructionProperties;

import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.test.instruction.InstructionTestUtils;

public class ArmInstructionPropertiesValidityTest {

    /*
     * Test checks if the manually initialized data in the ArmInstructionProperties enum
     * matches with what the parsers decode when initializing the enum private fields
     */
    @Test
    public void validitytest() {
        List<InstructionProperties> list = Arrays.asList(ArmInstructionProperties.values());
        InstructionTestUtils.validityTest(list);
    }

    /*
     * Test if all opcodes in all the instruction properties for microblaze are unique
     * 
     * TODO: This test is failing!
     */
    // @Test
    public void uniquenesstest() {
        List<InstructionProperties> list = Arrays.asList(ArmInstructionProperties.values());
        InstructionTestUtils.uniquenessTest(list);
    }

    /*
    @Test
    public void test() {
        for (ArmInstructionProperties props : ArmInstructionProperties.values()) {
            var v1 = props.getCodeType().toString();
            var v2 = props.getFieldData().getType().toString();
    
            try {
                assertEquals(v1, v2);
            } catch (AssertionError e) {
                System.out.print(props.name() + ":\t\t" + v1 + "\tvs.\t" + v2 + "\n");
            }
        }
    }*/
}
