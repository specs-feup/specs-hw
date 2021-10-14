/**
 * Copyright 2021 SPeCS.
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

package org.specs.Riscv.parsing;

import static org.specs.Riscv.parsing.RiscvAsmField.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.specs.Riscv.asm.RiscvRegister;
import org.specs.Riscv.instruction.RiscvRegisterDump;

import pt.up.fe.specs.binarytranslation.instruction.register.ExecutedImmediate;
import pt.up.fe.specs.binarytranslation.instruction.register.ExecutedRegister;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class RiscvRegisterResolver {

    private final RiscvAsmFieldData fielddata;
    private final RiscvRegisterDump registers;

    // 32 register integer bank (TODO: test if actually works?)
    private final static Map<Integer, RiscvRegister> generalRegsToDefs;
    static {
        var amap = new HashMap<Integer, RiscvRegister>();
        var regedfs = Arrays.asList(RiscvRegister.values());
        for (int i = 0; i < 32; i++)
            amap.put(i, regedfs.get(i));
        generalRegsToDefs = amap;
    }

    public RiscvRegisterResolver(
            RiscvAsmFieldData fielddata,
            RiscvRegisterDump registers) {
        this.fielddata = fielddata;
        this.registers = registers;
    }

    public ExecutedRegister resolve(RiscvAsmField fieldName) {
        var fieldIntvalue = fielddata.getMap().get(fieldName);
        var regDef = generalRegsToDefs.get(fieldIntvalue);
        var dataValue = registers.getValue(regDef);
        return new ExecutedRegister(fieldName, regDef, dataValue);
    }

    public ExecutedImmediate resolveIMM12() {
        var imm12 = fielddata.getMap().get(IMMTWELVE);
        imm12 = BinaryTranslationUtils.signExtend32(imm12, 12);
        return new ExecutedImmediate(RiscvAsmField.IMMTWELVE, imm12);
    }

    public ExecutedImmediate resolveIMM7PlusIMM5() {

        var imm5 = fielddata.getMap().get(IMMFIVE);
        var imm7 = fielddata.getMap().get(IMMSEVEN);

        // build full imm field from 2 fields
        var imm12 = (imm7 << 5) | imm5;
        imm12 = BinaryTranslationUtils.signExtend32(imm12, 12);
        return new ExecutedImmediate(RiscvAsmField.IMMTWELVE, imm12);
        // TODO: IMMTWELVE not really correct here... but no other way to represent this now...
    }

    public ExecutedImmediate resolveIMM20Upper() {
        var imm20 = fielddata.getMap().get(IMMTWENTY);
        imm20 = BinaryTranslationUtils.signExtend32(imm20, 20);
        return new ExecutedImmediate(RiscvAsmField.IMMTWENTY, imm20);
    }

    public ExecutedImmediate resolveIMMUJType() {

        var map = fielddata.getMap();

        // build full imm field from 4 fields
        var bit20 = map.get(BIT20);
        var imm10 = map.get(IMMTEN);
        var bit11 = map.get(BIT11);
        var imm8 = map.get(IMMEIGHT);

        // final addr is this instructions own addr + sign extended fulimm
        var target = BinaryTranslationUtils.signExtend32(
                (bit20 << 20) | (imm8 << 12) | (bit11 << 11) | (imm10 << 1), 21);

        // final addr is this instructions own addr + sign extended fulimm
        target = fielddata.getAddr().intValue() + target;

        return new ExecutedImmediate(RiscvAsmField.IMMTWELVE, target);
        // TODO: IMMTWELVE not really correct here... but no other way to represent this now...
    }

    public ExecutedImmediate resolveIMMSBType() {

        var map = fielddata.getMap();

        // build full imm field from 4 fields
        var bit12 = map.get(BIT12);
        var bit11 = map.get(BIT11);
        var imm6 = map.get(IMMSIX);
        var imm4 = map.get(IMMFOUR);
        var target = BinaryTranslationUtils.signExtend32(
                (bit12 << 12) | (bit11 << 11) | (imm6 << 5) | (imm4 << 1), 12);

        return new ExecutedImmediate(RiscvAsmField.IMMTWELVE, target);
        // TODO: IMMTWELVE not really correct here... but no other way to represent this now...
    }
}
