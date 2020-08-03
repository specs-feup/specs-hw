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

package org.specs.MicroBlazeSimulator.microcode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.simulator.microcodemachine.MicroCodeDecoder;
import pt.up.fe.specs.simulator.microcodemachine.MicroCodeInstruction;
import pt.up.fe.specs.simulator.microcodemachine.MicroCodeMachine;
import pt.up.fe.specs.util.SpecsCheck;

public class ExpandedMbMicroCodeDecoder implements MicroCodeDecoder {

    private static final Map<String, BiConsumer<Instruction, MicroCodeMachine>> MICROCODE;

    static {
        MICROCODE = new HashMap<>();

        MICROCODE.put("imm", ExpandedMbMicroCodeDecoder::imm);
        MICROCODE.put("brai", ExpandedMbMicroCodeDecoder::brai);
        MICROCODE.put("bri", ExpandedMbMicroCodeDecoder::bri);
    }

    @Override
    public MicroCodeInstruction decode(Instruction instruction, MicroCodeMachine machine) {

        var microCode = MICROCODE.get(instruction.getName());
        SpecsCheck.checkNotNull(microCode,
                () -> "No micro code for instruction " + instruction.getName() + " (" + instruction.getString()
                        + ")");

        var simInst = new MicroCodeInstruction(machine, instruction,
                mcInst -> microCode.accept(mcInst.getOriginalInstruction(), mcInst.getMachine()));

        // Add instruction to the machine
        machine.addInstruction(simInst);

        return simInst;
    }

    public static void imm(Instruction inst, MicroCodeMachine machine) {

        var immValue = inst.getFieldData().getFieldAsBinaryInteger("imm");

        // Write as temporary value
        machine.getTemp().write(MbId.IMM, immValue << 16);
    }

    public static void brai(Instruction inst, MicroCodeMachine machine) {

        Number immValue = inst.getFieldData().getFieldAsBinaryInteger("imm");

        // If there is a IMM value set, 'OR' it with given imm
        if (machine.getTemp().has(MbId.IMM)) {
            var upperImm = machine.getTemp().readAndClear(MbId.IMM);
            immValue = machine.getAlu().or(upperImm, immValue);
        } else {
            immValue = machine.getAlu().signExtend(immValue, 15);
        }

        // Calculate next address
        var nextAddr = machine.toAddr(immValue);

        // Set address
        machine.setJump(nextAddr);
    }

    public static void bri(Instruction inst, MicroCodeMachine machine) {

        Number immValue = inst.getFieldData().getFieldAsBinaryInteger("imm");

        // If there is a IMM value set, 'OR' it with given imm
        if (machine.getTemp().has(MbId.IMM)) {
            var upperImm = machine.getTemp().readAndClear(MbId.IMM);
            immValue = machine.getAlu().or(upperImm, immValue);
        } else {
            immValue = machine.getAlu().signExtend(immValue, 15);
        }

        // Calculate next address
        var nextAddr = machine.toAddr(inst.getAddress()).add(immValue);

        // Set address
        machine.setJump(nextAddr);
    }

}
