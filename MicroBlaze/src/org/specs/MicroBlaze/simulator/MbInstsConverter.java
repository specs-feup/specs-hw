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

import org.specs.MicroBlaze.simulator.insts.Imm;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.simulator.SimInstruction;
import pt.up.fe.specs.util.SpecsCheck;

public class MbInstsConverter {

    private final MicroBlazeMachine machine;

    public MbInstsConverter(MicroBlazeMachine machine) {
        this.machine = machine;
    }

    private static final Map<String, BiFunction<MbInstsConverter, Instruction, SimInstruction>> CONVERTERS;
    static {
        CONVERTERS = new HashMap<>();

        CONVERTERS.put("imm", MbInstsConverter::imm);
    }

    public SimInstruction convert(Instruction instruction) {
        var converter = CONVERTERS.get(instruction.getName());
        SpecsCheck.checkNotNull(converter, () -> "No converter for instruction " + instruction.getName());

        return converter.apply(this, instruction);
    }

    public static SimInstruction imm(MbInstsConverter converter, Instruction imm) {
        var immString = imm.getFieldData().get(AsmFieldData.FIELDS).get("imm");

        // Convert imm value to int (is it always positive?)
        var immValue = Integer.parseInt(immString);

        return new Imm(converter.machine, imm.getAddress(), immValue);
    }
}
