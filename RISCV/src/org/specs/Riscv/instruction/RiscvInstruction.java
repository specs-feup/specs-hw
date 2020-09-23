/**
 * Copyright 2020 SPeCS.
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

package org.specs.Riscv.instruction;

import java.util.Arrays;

import org.specs.Riscv.parsing.RiscvAsmFieldData;
import org.specs.Riscv.parsing.RiscvAsmFieldType;
import org.specs.Riscv.parsing.RiscvIsaParser;

import pt.up.fe.specs.binarytranslation.instruction.AInstruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.InstructionSet;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;

public class RiscvInstruction extends AInstruction {

    // raw field data
    private final RiscvAsmFieldData fieldData;

    // shared by all instructions, so they can go parse themselves
    private static RiscvIsaParser parser;

    /*
     * Parser and "decoder" Shared by all
     */
    static {
        parser = new RiscvIsaParser();
        instSet = new InstructionSet(Arrays.asList(RiscvInstructionProperties.values()),
                Arrays.asList(RiscvAsmFieldType.values()));
    }

    /*
     * Static "constructor"
     */
    public static RiscvInstruction newInstance(String address, String instruction) {
        var fieldData = parser.parse(address, instruction);
        var props = instSet.process(fieldData);
        var idata = new RiscvInstructionData(props, fieldData);
        return new RiscvInstruction(address, instruction, idata, fieldData, props);
    }

    /*
     * Create the instruction
     */
    private RiscvInstruction(String address, String instruction, InstructionData idata,
            RiscvAsmFieldData fieldData, InstructionProperties props) {
        super(Long.parseLong(address, 16), instruction, idata, props);
        this.fieldData = fieldData;
    }

    /*
     * Copy "constructor"
     */
    @Override
    public RiscvInstruction copy() {

        String copyaddr = new String(Integer.toHexString(this.getAddress().intValue()));
        String copyinst = new String(this.getInstruction());
        RiscvInstructionData copyData = this.getData().copy();
        RiscvAsmFieldData copyFieldData = this.getFieldData().copy();
        return new RiscvInstruction(copyaddr, copyinst, copyData, copyFieldData, this.getProperties());
    }

    @Override
    public RiscvInstructionData getData() {
        // idata is guaranteed to be an (RiscvInstructionData)
        return (RiscvInstructionData) super.getData();
    }

    @Override
    public Number getBranchTarget() {
        if (this.isJump()) {
            long branchTarget = (this.getData().getBranchTarget()).longValue();

            if (this.getData().getGenericTypes().contains(InstructionType.G_RJUMP)) {
                var v = this.getAddress().longValue();
                return branchTarget = v + branchTarget;
            }

            else if (this.getData().getGenericTypes().contains(InstructionType.G_AJUMP))
                return branchTarget;
        }
        return null;
    }

    @Override
    public RiscvAsmFieldData getFieldData() {
        return this.fieldData;
    }

    @Override
    public InstructionPseudocode getPseudocode() {
        // TODO Auto-generated method stub
        return null;
    }
}
