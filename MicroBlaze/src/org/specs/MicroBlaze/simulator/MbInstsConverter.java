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

package org.specs.MicroBlaze.simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.specs.MicroBlaze.simulator.insts.Bri;
import org.specs.MicroBlaze.simulator.insts.Imm;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.simulator.SimInstruction;
import pt.up.fe.specs.util.SpecsCheck;

public class MbInstsConverter {

    private static final Map<String, BiFunction<MbInstsConverter, Instruction, MbSimInstruction>> CONVERTERS;

    static {
        CONVERTERS = new HashMap<>();

        CONVERTERS.put("brai", (conv, inst) -> MbInstsConverter.bri(conv, inst, true));
        CONVERTERS.put("bri", (conv, inst) -> MbInstsConverter.bri(conv, inst, false));
        CONVERTERS.put("imm", MbInstsConverter::imm);
    }

    private final MicroBlazeMachine machine;

    public MbInstsConverter(MicroBlazeMachine machine) {
        this.machine = machine;
    }

    public SimInstruction convert(Instruction instruction) {

        var converter = CONVERTERS.get(instruction.getName());
        SpecsCheck.checkNotNull(converter, () -> "No converter for instruction " + instruction.getName());

        var simInst = converter.apply(this, instruction);

        // Add instruction to the machine
        machine.addInstruction(simInst);

        return simInst;
    }

    public static MbSimInstruction imm(MbInstsConverter converter, Instruction inst) {
        var immString = inst.getFieldData().get(AsmFieldData.FIELDS).get("imm");
        // System.out.println("IMM FIELDS: " + inst.getFieldData());
        // Convert imm value to int (is it always positive?)
        var immValue = Integer.parseInt(immString, 2);

        return new Imm(converter.machine, inst.getAddress(), immValue);
    }

    // public static MbSimInstruction brai(MbInstsConverter converter, Instruction inst) {
    // var immString = inst.getFieldData().get(AsmFieldData.FIELDS).get("imm");
    //
    // // Convert imm value to int (is it always positive?)
    // var immValue = Integer.parseInt(immString);
    //
    // return Bri.brai(converter.machine, inst.getAddress(), immValue);
    // }

    public static MbSimInstruction bri(MbInstsConverter converter, Instruction inst,
            boolean isAbsolute) {

        var immString = inst.getFieldData().get(AsmFieldData.FIELDS).get("imm");

        // System.out.println("BRI FIELDS: " + inst.getFieldData());

        // Convert imm value to int (is it always positive?)
        var immValue = Integer.parseInt(immString, 2);

        if (isAbsolute) {
            return Bri.brai(converter.machine, inst.getAddress(), immValue);
        }

        return Bri.bri(converter.machine, inst.getAddress(), immValue);
    }
}
