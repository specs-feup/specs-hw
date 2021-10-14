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

package org.specs.Riscv.parsing.getters;

import static org.specs.Riscv.instruction.RiscvOperand.*;
import static org.specs.Riscv.parsing.RiscvAsmField.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Riscv.instruction.RiscvRegisterDump;
import org.specs.Riscv.parsing.RiscvAsmFieldData;
import org.specs.Riscv.parsing.RiscvAsmFieldType;
import org.specs.Riscv.parsing.RiscvRegisterResolver;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class RiscvAsmOperandGetter {

    /*
     * map TYPE to a specific private branch target getter func
     */
    interface RiscvAsmOperandParse {
        List<Operand> apply(RiscvRegisterResolver resolver);
    }

    private static final Map<RiscvAsmFieldType, RiscvAsmOperandParse> TARGETGET;
    static {
        var amap = new HashMap<RiscvAsmFieldType, RiscvAsmOperandParse>();
        amap.put(RiscvAsmFieldType.OP, RiscvAsmOperandGetter::rtype);
        amap.put(RiscvAsmFieldType.AMO, RiscvAsmOperandGetter::rtype);
        amap.put(RiscvAsmFieldType.OPFPa, RiscvAsmOperandGetter::rtype);

        amap.put(RiscvAsmFieldType.OPIMM, RiscvAsmOperandGetter::itype);
        amap.put(RiscvAsmFieldType.LOAD, RiscvAsmOperandGetter::itype);
        amap.put(RiscvAsmFieldType.JALR, RiscvAsmOperandGetter::itype);
        amap.put(RiscvAsmFieldType.LOADFP, RiscvAsmOperandGetter::itype); // TODO: correct type??

        amap.put(RiscvAsmFieldType.STOREFP, RiscvAsmOperandGetter::stype);
        // amap.put(RiscvAsmFieldType.LOADFP, RiscvAsmOperandGetter::stype); // TODO: correct type??
        amap.put(RiscvAsmFieldType.STORE, RiscvAsmOperandGetter::stype);

        amap.put(RiscvAsmFieldType.LUI, RiscvAsmOperandGetter::utype);
        amap.put(RiscvAsmFieldType.AUIPC, RiscvAsmOperandGetter::utype);

        amap.put(RiscvAsmFieldType.JAL, RiscvAsmOperandGetter::ujtype);
        amap.put(RiscvAsmFieldType.BRANCH, RiscvAsmOperandGetter::sbtype);

        TARGETGET = Collections.unmodifiableMap(amap);
    }

    public static List<Operand> getFrom(RiscvAsmFieldData fielddata, RiscvRegisterDump registers) {

        var func = TARGETGET.get(fielddata.get(RiscvAsmFieldData.TYPE));
        if (func == null)
            func = RiscvAsmOperandGetter::undefined;

        var resolver = new RiscvRegisterResolver(fielddata, registers);
        return func.apply(resolver);
    }

    ///////////////////////////////////////////////////////////////////////
    // R types
    // case OP:
    // case AMO:
    // case OPFPa:
    private static List<Operand> rtype(RiscvRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newReadRegister(resolver.resolve(RS1)));
        ops.add(newReadRegister(resolver.resolve(RS2)));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // I types
    // case OPIMM:
    // case LOAD:
    // case JALR:
    // case LOADFP:
    private static List<Operand> itype(RiscvRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        // var map = fielddata.getMap();
        // ops.add(newWriteRegister(RD, map.get(RD)));
        // ops.add(newReadRegister(RS1, map.get(RS1)));
        // ops.add(newImmediate(IMMTWELVE, map.get(IMMTWELVE)));

        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newReadRegister(resolver.resolve(RS1)));
        ops.add(newImmediate(resolver.resolveIMM12()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // S types
    // case STOREFP:
    // case STORE:
    private static List<Operand> stype(RiscvRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();

        // var map = fielddata.getMap();
        // ops.add(newReadRegister(RS1, map.get(RS1)));
        // ops.add(newReadRegister(RS2, map.get(RS2)));
        // build full imm field from 2 fields
        // var fullimm = (map.get(IMMSEVEN) << 5) | map.get(IMMFIVE);
        // ops.add(newImmediate(IMM, fullimm));

        ops.add(newReadRegister(resolver.resolve(RS1)));
        ops.add(newReadRegister(resolver.resolve(RS2)));
        ops.add(newImmediate(resolver.resolveIMM7PlusIMM5()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // U types
    // case LUI:
    // case AUIPC:
    private static List<Operand> utype(RiscvRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        // var map = fielddata.getMap();
        // ops.add(newWriteRegister(RD, map.get(RD)));
        // ops.add(newImmediate(IMMTWENTY, map.get(IMMTWENTY)));

        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newImmediate(resolver.resolveIMM20Upper()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // UJ types
    // case JAL:
    private static List<Operand> ujtype(RiscvRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();

        /*var map = fielddata.getMap();
        
        ops.add(newWriteRegister(RD, map.get(RD)));
        
        // TODO: this code repeats in RiscvAsmBranchTargetGetter
        
        // build full imm field from 4 fields
        var bit20 = map.get(BIT20);
        var imm10 = map.get(IMMTEN);
        var bit11 = map.get(BIT11);
        var imm8 = map.get(IMMEIGHT);
        
        // final addr is this instructions own addr + sign extended fulimm
        var target = BinaryTranslationUtils.signExtend32(
                (bit20 << 20) | (imm8 << 12) | (bit11 << 11) | (imm10 << 1), 21);
        
        // final addr is this instructions own addr + sign extended fulimm
        var fullimm = fielddata.getAddr().intValue() + target;
        ops.add(newImmediate(IMM, fullimm));*/

        ops.add(newWriteRegister(resolver.resolve(RD)));
        ops.add(newImmediate(resolver.resolveIMMUJType()));

        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    // SB-type
    // case BRANCH:
    private static List<Operand> sbtype(RiscvRegisterResolver resolver) {
        var ops = new ArrayList<Operand>();
        /*var map = fielddata.getMap();
        ops.add(newReadRegister(RS1, map.get(RS1)));
        ops.add(newReadRegister(RS2, map.get(RS2)));
        
        // build full imm field from 4 fields
        var bit12 = map.get(BIT12);
        var bit11 = map.get(BIT11);
        var imm6 = map.get(IMMSIX);
        var imm4 = map.get(IMMFOUR);
        var target = BinaryTranslationUtils.signExtend32(
                (bit12 << 12) | (bit11 << 11) | (imm6 << 5) | (imm4 << 1), 12);
        
        ops.add(newImmediate(IMM, target));*/

        ops.add(newReadRegister(resolver.resolve(RS1)));
        ops.add(newReadRegister(resolver.resolve(RS2)));
        ops.add(newImmediate(resolver.resolveIMMSBType()));
        return ops;
    }

    ///////////////////////////////////////////////////////////////////////
    private static List<Operand> undefined(RiscvRegisterResolver resolver) {
        return null;
    }
}
