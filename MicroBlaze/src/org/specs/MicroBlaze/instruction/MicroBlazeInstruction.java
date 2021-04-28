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

package org.specs.MicroBlaze.instruction;

import java.util.Arrays;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;
import org.specs.MicroBlaze.parsing.MicroBlazeIsaParser;

import com.google.common.base.Enums;

import pt.up.fe.specs.binarytranslation.instruction.AInstruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.InstructionSet;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;

public class MicroBlazeInstruction extends AInstruction {

    // raw field data
    private final MicroBlazeAsmFieldData fieldData;

    // shared by all instructions, so they can go parse themselves
    private static MicroBlazeIsaParser parser;

    /*
     * Parser and "decoder" Shared by all
     */
    static {
        parser = new MicroBlazeIsaParser();
        instSet = new InstructionSet(Arrays.asList(MicroBlazeInstructionProperties.values()),
                Arrays.asList(MicroBlazeAsmFieldType.values()));
    }

    /*
     * Static "constructor"
     */
    public static MicroBlazeInstruction newInstance(String address, String instruction) {
        var fieldData = parser.parse(address, instruction);
        var props = instSet.process(fieldData);
        var idata = new MicroBlazeInstructionData(props, fieldData);
        var inst = new MicroBlazeInstruction(address, instruction, idata, fieldData, props);
        return inst;
    }

    /*
     * Create the instruction
     */
    private MicroBlazeInstruction(String address, String instruction, InstructionData idata,
            MicroBlazeAsmFieldData fieldData, InstructionProperties props) {
        super(Long.parseLong(address, 16), instruction, idata, props);
        this.fieldData = fieldData;
    }

    /*
     * Copy "constructor"
     */
    @Override
    public MicroBlazeInstruction copy() {

        var copyaddr = new String(Long.toHexString(this.getAddress().intValue()));
        var copyinst = new String(this.getInstruction());
        var copyData = this.getData().copy();
        var copyFieldData = this.getFieldData().copy();
        var cpy = new MicroBlazeInstruction(copyaddr, copyinst, copyData, copyFieldData, this.getProperties());

        if (this.getRegisters() != null) {
            var copyDump = new RegisterDump(this.getRegisters());
            cpy.setRegisters(copyDump);
        }
        return cpy;
    }

    @Override
    public MicroBlazeInstructionData getData() {
        // idata is guaranteed to be an (MicroBlazeInstructionData)
        return (MicroBlazeInstructionData) super.getData();
    }

    @Override
    public Number getBranchTarget() {
        if (this.isJump()) {
            var numops = this.getData().getOperands().size();
            var jmpval = this.getData().getOperands().get(numops - 1).getNumberValue();

            long finalvalue = 0;
            if (this.isRelativeJump())
                finalvalue = (this.getAddress().longValue() + jmpval.longValue());
            else
                finalvalue = jmpval.longValue();

            return finalvalue;

            // TODO replace mask with mask built based on elf instruction width
            // or info about instruction set
        }

        return null;
    }

    @Override
    public MicroBlazeAsmFieldData getFieldData() {
        return this.fieldData;
    }

    @Override
    public InstructionPseudocode getPseudocode() {
        var pseudocode = Enums.getIfPresent(MicroBlazePseudocode.class, this.props.getEnumName());
        if (pseudocode.isPresent())
            return pseudocode.get();
        else
            return MicroBlazePseudocode.getDefault();

        // TODO: fix
    }
}
