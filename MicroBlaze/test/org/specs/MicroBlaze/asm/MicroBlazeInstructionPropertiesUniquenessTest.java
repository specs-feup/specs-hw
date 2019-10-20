/**
 *  Copyright 2019 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.specs.MicroBlaze.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.MicroBlaze.isa.MicroBlazeInstructionProperties;

public class MicroBlazeInstructionPropertiesUniquenessTest {

    /*
     * Test if all opcodes in all the instructionsproperties for microblaze are unique
     */
    @Test
    public void test() {
        int matchcount = 0;
        for (MicroBlazeInstructionProperties first : MicroBlazeInstructionProperties.values()) {
            matchcount = 0;
            for (MicroBlazeInstructionProperties second : MicroBlazeInstructionProperties.values()) {
                if(first.getOpCode() == second.getOpCode())
                    matchcount++;
            }
            System.out.print("props: " + first.getName() + " matches = " + matchcount + "\n");
            assertEquals(matchcount, 1);
        }
    }    
}
