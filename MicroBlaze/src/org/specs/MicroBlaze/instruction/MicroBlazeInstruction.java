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
import pt.up.fe.specs.binarytranslation.instruction.AInstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.InstructionSet;

public class MicroBlazeInstruction extends AInstruction {

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
        return MicroBlazeInstruction.newInstance(address, instruction, null);
    }

    /*
     * Static "constructor"
     */
    public static MicroBlazeInstruction newInstance(String address, String instruction, String rawRegisterDump) {
        var fieldData = (MicroBlazeAsmFieldData) parser.parse(address, instruction);
        var props = instSet.process(fieldData);
        var idata = new MicroBlazeInstructionData(props, fieldData, new MicroBlazeRegisterDump(rawRegisterDump));
        var inst = new MicroBlazeInstruction(address, instruction, idata);
        return inst;
    }

    /*
     * Create the instruction
     */
    private MicroBlazeInstruction(String address, String instruction, AInstructionData idata) {
        super(Long.parseLong(address, 16), instruction, idata);
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private MicroBlazeInstruction(MicroBlazeInstruction other) {
        super(other);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public MicroBlazeInstruction copy() {
        return new MicroBlazeInstruction(this);
    }

    @Override
    public MicroBlazeInstructionData getData() {
        // idata is guaranteed to be an (MicroBlazeInstructionData)
        return (MicroBlazeInstructionData) super.getData();
    }

    /*    @Override
    public MicroBlazeAsmFieldData getFieldData() {
        return this.fieldData;
    }*/

    @Override
    public InstructionPseudocode getPseudocode() {
        var pseudocode = Enums.getIfPresent(MicroBlazePseudocode.class, this.getData().getProperties().getEnumName());
        if (pseudocode.isPresent())
            return pseudocode.get();
        else
            return MicroBlazePseudocode.getDefault();

        // TODO: fix
    }
}
