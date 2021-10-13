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

import com.google.common.base.Enums;

import pt.up.fe.specs.binarytranslation.instruction.AInstruction;
import pt.up.fe.specs.binarytranslation.instruction.AInstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.InstructionSet;

public class RiscvInstruction extends AInstruction {

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
        return RiscvInstruction.newInstance(address, instruction, null);
    }

    /*
     * Static "constructor"
     */
    public static RiscvInstruction newInstance(String address, String instruction, String rawRegisterDump) {
        var fieldData = (RiscvAsmFieldData) parser.parse(address, instruction);
        var props = instSet.process(fieldData);
        var idata = new RiscvInstructionData(props, fieldData, new RiscvRegisterDump(rawRegisterDump));
        var inst = new RiscvInstruction(address, instruction, idata);
        return inst;
    }

    /*
     * Create the instruction
     */
    private RiscvInstruction(String address, String instruction, AInstructionData idata) {
        super(Long.parseLong(address, 16), instruction, idata);
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private RiscvInstruction(RiscvInstruction other) {
        super(other);
    }

    /*
     * Copy "constructor"
     */
    @Override
    public RiscvInstruction copy() {
        return new RiscvInstruction(this);
    }

    @Override
    public RiscvInstructionData getData() {
        // idata is guaranteed to be an (RiscvInstructionData)
        return (RiscvInstructionData) super.getData();
    }

    @Override
    public InstructionPseudocode getPseudocode() {
        var pseudocode = Enums.getIfPresent(RiscvPseudocode.class,
                this.getData().getProperties().getEnumName());
        if (pseudocode.isPresent())
            return pseudocode.get();
        else
            return RiscvPseudocode.getDefault();
    }
}
