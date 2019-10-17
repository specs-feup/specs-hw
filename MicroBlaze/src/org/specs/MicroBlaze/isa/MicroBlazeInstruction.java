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

import pt.up.fe.specs.binarytranslation.InstructionData;
import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.generic.AInstruction;

public class MicroBlazeInstruction extends AInstruction {

    // raw field data
    private final MicroBlazeAsmInstructionData fieldData;

    // shared by all instructions, so they can go parse themselves
    private static MicroBlazeIsaParser parser;

    /*
     * Parser and "decoder" Shared by all
     */
    static {
        parser = new MicroBlazeIsaParser();
        instSet = new InstructionSet(Arrays.asList(MicroBlazeInstructionProperties.values()),
                Arrays.asList(MicroBlazeInstructionType.values()));
    }

    /*
     * Static "constructor"
     */
    public static MicroBlazeInstruction newInstance(String address, String instruction) {
        var fieldData = parser.parse(instruction);
        var idata = new MicroBlazeInstructionData(instSet.process(fieldData), fieldData);
        return new MicroBlazeInstruction(address, instruction, idata, fieldData);
    }

    /*
     * Create the instruction
     */
    private MicroBlazeInstruction(String address, String instruction, InstructionData idata,
            MicroBlazeAsmInstructionData fieldData) {
        super(Long.parseLong(address, 16), instruction, idata);
        this.fieldData = fieldData;
    }

    @Override
    public MicroBlazeInstructionData getData() {
        // idata is guaranteed to be an (ArmInstructionData)
        return (MicroBlazeInstructionData) super.getData();
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

    @Override
    public MicroBlazeAsmInstructionData getFieldData() {
        return this.fieldData;
    }
}
