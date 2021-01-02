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

import java.math.BigInteger;
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
     * Use these to post an imm notification and resolve it later
     */
    // private static Boolean postedImm = false;
    // private static int immValue = 0;

    /*
     * Static "constructor"
     */
    public static MicroBlazeInstruction newInstance(String address, String instruction) {
        var fieldData = parser.parse(address, instruction);
        var props = instSet.process(fieldData);
        var idata = new MicroBlazeInstructionData(props, fieldData);
        var inst = new MicroBlazeInstruction(address, instruction, idata, fieldData, props);

        /*
        // store imm value if instruction is imm
        if (inst.isImmediateValue() && MicroBlazeInstruction.postedImm == false) {
            MicroBlazeInstruction.postedImm = true;
            MicroBlazeInstruction.immValue = inst.getData().getOperands().get(0).getValue().intValue();
        }
        
        // process previous imm value if any
        else if (MicroBlazeInstruction.postedImm == true) {
            inst.completeImm(MicroBlazeInstruction.immValue);
            MicroBlazeInstruction.postedImm = false;
        
        }
        
        // if only lower 16 is given
        else {
            inst.extendImm();
        }
        */
        return inst;
    }

    /*
     * 
    private void extendImm() {
        // get imm value operand
        for (Operand op : this.getData().getOperands())
            if (op.isImmediate()) {
                int lower16 = op.getValue().intValue();
                int fullimm = (lower16 << (16)) >> (16);
                Number num = fullimm;
                op.overrideValue(num);
            }
    
        return;
    }
    
    private void completeImm(int immValue) {
    
        // get imm value operand
        for (Operand op : this.getData().getOperands())
            if (op.isImmediate()) {
                int upper16 = MicroBlazeInstruction.immValue << 16;
                int lower16 = op.getValue().intValue();
                Number fullimm = upper16 | lower16;
                op.overrideValue(fullimm);
            }
    
        return;
    }
    */

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

        String copyaddr = new String(Long.toHexString(this.getAddress().intValue()));
        String copyinst = new String(this.getInstruction());
        MicroBlazeInstructionData copyData = this.getData().copy();
        MicroBlazeAsmFieldData copyFieldData = this.getFieldData().copy();
        return new MicroBlazeInstruction(copyaddr, copyinst, copyData, copyFieldData, this.getProperties());
    }

    @Override
    public MicroBlazeInstructionData getData() {
        // idata is guaranteed to be an (MicroBlazeInstructionData)
        return (MicroBlazeInstructionData) super.getData();
    }

    @Override
    public Number getBranchTarget() {
        if (this.isJump()) {
            int fullopcode = new BigInteger(this.getInstruction(), 16).intValue();
            short jmpval = (short) (fullopcode & 0x0000FFFF);
            return (this.getAddress().longValue() + jmpval);
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
