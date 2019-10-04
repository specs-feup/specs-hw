/**
 * Copyright 2019 SPeCS.
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

package org.specs.MicroBlaze.isa;

import java.math.BigInteger;
import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.generic.AInstruction;

public class MicroBlazeInstruction extends AInstruction {

    /*
     * Parser and "decoder" Shared by all
     */
    static {
        instSet = new InstructionSet(Arrays.asList(MicroBlazeInstructionProperties.values()),
                Arrays.asList(MicroBlazeInstructionType.values()));
        parser = MicroBlazeInstructionParsers.getMicroBlazeIsaParser();
    }

    /*
     * Create the instruction
     */
    public MicroBlazeInstruction(String address, String instruction) {
        super(Long.parseLong(address, 16), instruction);
        this.fieldData = parser.parse(instruction);
        this.idata = instSet.process(fieldData);
        // lookup ISA table for static information
    }

    @Override
    public Number getBranchTarget() {
        if (this.isJump()) {
            int fullopcode = new BigInteger(this.getInstruction(), 16).intValue();
            short jmpval = (short) (fullopcode & 0x0000FFFF);
            return (this.getAddress().longValue() + (long) jmpval);
            // TODO replace mask with mask built based on elf instruction width
            // or info about instruction set
        }

        return null;
    }
}
