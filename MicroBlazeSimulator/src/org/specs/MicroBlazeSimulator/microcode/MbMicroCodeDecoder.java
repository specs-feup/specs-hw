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
import java.util.function.Consumer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.simulator.microcodemachine.MicroCodeDecoder;
import pt.up.fe.specs.simulator.microcodemachine.MicroCodeInstruction;
import pt.up.fe.specs.simulator.microcodemachine.MicroCodeMachine;
import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

public class MbMicroCodeDecoder implements MicroCodeDecoder {

    private static final Map<String, Consumer<MicroCodeInstruction>> MICROCODE;

    static {
        MICROCODE = new HashMap<>();

        MICROCODE.put("imm", MbMicroCodeDecoder::imm);
        MICROCODE.put("brai", inst -> MbMicroCodeDecoder.bri(inst, true, false, 0));
        MICROCODE.put("bri", inst -> MbMicroCodeDecoder.bri(inst, false, false, 0));
    }

    // private final MicroCodeMachine machine;

    public MbMicroCodeDecoder() {
        // this.machine = machine;
    }

    @Override
    public MicroCodeInstruction decode(Instruction instruction, MicroCodeMachine machine) {

        var microCode = MICROCODE.get(instruction.getName());
        SpecsCheck.checkNotNull(microCode,
                () -> "No micro code for instruction " + instruction.getName() + " (" + instruction.getString()
                        + ")");

        var simInst = new MicroCodeInstruction(machine, instruction, microCode);

        // Add instruction to the machine
        machine.addInstruction(simInst);

        return simInst;
    }

    public static void imm(MicroCodeInstruction microCode) {
        var mbInst = microCode.getOriginalInstruction();

        var immString = mbInst.getFieldData().get(AsmFieldData.FIELDS).get("imm");
        var immValue = Integer.parseInt(immString, 2);

        // Write as temporary value
        microCode.getMachine().getTemp().write(MbId.IMM, immValue << 16);
    }

    public static void bri(MicroCodeInstruction microCode, boolean isAbsolute, boolean hasLinkRegister, int delaySlot) {
        var machine = microCode.getMachine();

        var mbInst = microCode.getOriginalInstruction();

        var immString = mbInst.getFieldData().get(AsmFieldData.FIELDS).get("imm");
        var immValue = Integer.parseInt(immString, 2);

        Number extendedImm = MbMicroCodeUtils.signExtendedImm32(immValue, microCode.getMachine());

        // Store current address in the register
        if (hasLinkRegister) {
            // TODO: get register from fields
            // machine.getRegisters().write(linkRegister, microCode.getAddress().toNumber());
            throw new NotImplementedException(MbMicroCodeDecoder.class);
        }

        // Calculate next address
        var nextAddr = isAbsolute ? machine.toAddr(extendedImm) : microCode.getAddress().add(extendedImm);

        // Set address
        machine.setJump(nextAddr);

        // Set delay slot
        microCode.getMachine().setDelay(delaySlot);
    }

}
