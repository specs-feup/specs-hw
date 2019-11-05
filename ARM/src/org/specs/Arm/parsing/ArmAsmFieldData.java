package org.specs.Arm.parsing;

import static org.specs.Arm.parsing.ArmAsmField.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Arm.instruction.ArmInstructionCondition;

import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;

public class ArmAsmFieldData extends AsmFieldData {

    /*
     * remmaping of <string, string> to <asmfield, string>
     */
    private final Map<ArmAsmField, Integer> map = new HashMap<ArmAsmField, Integer>();

    /*
     * Create raw
     */
    public ArmAsmFieldData(AsmFieldType type, Map<String, String> fields) {
        super(type, fields);

        // get int values from fields
        var map1 = this.get(FIELDS);
        for (ArmAsmField field : ArmAsmField.values()) {
            if (map1.containsKey(field.getFieldName())) {
                map.put(field, Integer.parseInt(map1.get(field.getFieldName()), 2));
            }
        }
    }

    /*
     * Create from parent class
     */
    public ArmAsmFieldData(AsmFieldData fieldData) {
        this(fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * 
     */
    public Map<ArmAsmField, Integer> getMap() {
        return map;
    }

    /*
     * Get "sf" field from ARM instruction types and interpret
     */
    public int getBitWidth() {

        ArmAsmFieldType type = (ArmAsmFieldType) this.get(TYPE);
        var map1 = this.get(FIELDS);

        // sf, sfa, and sfb fields are treated differently based on the instruction format
        switch (type) {

        // when "sf" is a single bit
        case DPI_ADDSUBIMM:
        case DPI_ADDSUBIMM_TAGS:
        case LOGICAL:
        case MOVEW:
        case EXTRACT:
        case BITFIELD:
        case DPR_TWOSOURCE:
        case LOGICAL_SHIFT_REG:
        case ADD_SUB_SHIFT_REG:
        case ADD_SUB_EXT_REG:
        case ADD_SUB_CARRY: {
            return (map.get(SF) != 0) ? 64 : 32;
        }

        // when sf is two bits
        case LOAD_REG_LITERAL_FMT1: {
            return ((int) Math.pow(2, map.get(SF))) * 32;
        }

        // when sf is two bits (again)
        case LOAD_STORE_PAIR_NO_ALLOC:
        case LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1: {

            var sf = map.get(SF);
            if (this.isSimd()) {
                sf = ((int) Math.pow(2, sf)) * 32;
            } else {
                sf = (sf != 0) ? 64 : 32;
            }
            return sf;
        }

        // fields "opcodea" and "opcodeb" used for size
        case LOAD_STORE_PAIR_IMM_FMT2: {
            Boolean a, b, c, d;
            a = map1.get("opcodea").substring(0, 1).equals("1");
            b = map1.get("opcodea").substring(1, 2).equals("1");
            c = map1.get("opcodeb").substring(0, 1).equals("1");
            d = map1.get("opcodeb").substring(1, 2).equals("1");
            var sf = ((!a & !d) | (b & !c)) ? 64 : 32;
            return sf;
        }

        // two fields, sfa, and sfb
        case LOAD_STORE_PAIR_IMM_FMT3:
        case LOAD_STORE_IMM_PREPOST_FMT3:
        case LOAD_STORE_REG_OFF_FMT3: {
            var sf = (map.get(SFB) << 2) | map.get(SFA);
            return ((int) Math.pow(2, sf)) * 8;
        }

        default:
            return 32;
        // TODO throw exception here??
        }
    }

    /*
     * Get "simd field"
     */
    public Boolean isSimd() {
        var map1 = this.get(FIELDS);
        if (!map.containsKey(SIMD)) {
            return false; // default length

        } else if (map.get(SIMD) != 0) {
            return true;

        } else {
            return false;
        }
    }

    /*
     * Decode "cond" field if present
     */
    public ArmInstructionCondition getCond() {
        if (!map.containsKey(COND))
            return ArmInstructionCondition.NONE;

        return ArmInstructionCondition.decodeCondition(map.get(COND));
        // TODO throw something
    }

    /*
    * Get target of branch if instruction is branch
    */
    public int getBranchTarget() {

        ArmAsmFieldType type = (ArmAsmFieldType) this.get(TYPE);

        switch (type) {

        // conditional branches have a 19 bit IMM field
        case CONDITIONALBRANCH:
            return signExtend32(map.get(IMM), 9);

        default:
            return 0;
        // TODO throw exception here??
        }
    }

    /*
     * Gets a list of integers which represent the operands in the fields
     * This manner of field parsing, maintains the operand order as parsed
     * in the AsmFields
     */
    public List<Operand> getOperands() {
        // return ArmAsmOperandGetter.getFrom(this);

        ArmAsmFieldType type = (ArmAsmFieldType) this.get(TYPE);

        // assign to Operand objects based on field format
        List<Operand> operands = new ArrayList<Operand>();

        // order of operands MUST be preserved (or should be)
        switch (type) {

        ///////////////////////////////////////////////////////////////////////
        /*case DPI_PCREL: {
            // first operand
            operands.add(newWriteRegister(RD, 64));
        
            // build second operand from "imm" and "imml"
            var imml = map.get(IMML);
            var imm = map.get(IMM);
            var shift = (map.get(OPCODEA) == 0) ? 0 : 12;
            Integer fullimm = (((imm << 2) | (imml)) << shift) * 4096;
            operands.add(ArmOperand.newImmediateLabel(IMM, fullimm, 64));
            break;
        }*/

        ///////////////////////////////////////////////////////////////////////
        /*case DPI_ADDSUBIMM: {
            // first and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
            operands.add(newReadRegister(RN, wd));
        
            // third operand
            var imm = map.get(IMM);
            Number fullimm = (map.get(OPCODEB) == 1) ? imm << 12 : imm; // OPCODEB = "sh"
            operands.add(ArmOperand.newImmediate(IMM, fullimm, wd));
            break;
        }*/

        /* TODO: depends on architectural configuration (and is confusing...)
        case DPI_ADDSUBIMM_TAGS: {
            // first and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, map.get(RD), wd));
            operands.add(newWriteRegister(RN, map.get(RN), wd));
            
            
            break;
        }
        */
        /*
        case LOGICAL: {
            // first and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
            operands.add(newReadRegister(RN, wd));
        
            // third operand
            var immr = map.get(IMMR);
            var imms = map.get(IMMS);
            var Nval = map.get(N);
            Number fullimm = DecodeBitMasks(Nval, imms, immr, true);
            operands.add(ArmOperand.newImmediate(IMM, fullimm, wd));
            break;
        }
        */
        /*
        case MOVEW: {
            // first operand
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
        
            var imm = map.get(IMM);
            var hw = map.get(OPCODEB); // OPCODEB = "hw"
            Number fullimm = imm << hw;
            operands.add(ArmOperand.newImmediate(IMM, fullimm, wd));
            break;
        }
        */
        /*
        case BITFIELD: {
            // first and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
            operands.add(newReadRegister(RN, wd));
        
            // 3rd and 4th
            operands.add(newImmediate(IMMR, 8)); // actually 6 bits
            operands.add(newImmediate(IMMS, 8)); // actually 6 bits
            break;
        }
        */
        /*
        case EXTRACT: {
            // first, second and third operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
            operands.add(newReadRegister(RN, wd));
            operands.add(newReadRegister(RM, wd));
        
            // fourth operand
            operands.add(newImmediate(IMMS, 8)); // actually 6 bits
            break;
        }
        */
        /*
        case CONDITIONALBRANCH: {
            // first operand
            Number fullimm = map.get(IMM) << 2;
            operands.add(ArmOperand.newImmediateLabel(IMM, fullimm, 64));
            break;
        }
        */
        /*
        case EXCEPTION: {
            // first operand
            Number fullimm = map.get(IMM);
            operands.add(ArmOperand.newImmediateLabel(IMM, fullimm, 16));
            break;
        }
        */
        /*
        case UCONDITIONALBRANCH_REG: {
            // first operand
            operands.add(newReadRegister(RN, 64));
        
            // for brab and braa
            var opa = map.get(OPCODEA); // contains bit "Z"
            if ((opa & 0x0001) != 0) {
                operands.add(ArmOperand.newReadRegister(RM, map.get(OPCODED), 64));
            }
        
            break;
        }
        */
        /*
        case UCONDITIONALBRANCH_IMM: {
            // first operand
            Number fullimm = map.get(IMM) << 2;
            operands.add(ArmOperand.newImmediateLabel(IMM, fullimm, 64));
            break;
        }
        */
        /*
        case COMPARE_AND_BRANCH_IMM: {
            // first operand
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RT, wd));
        
            // second operand
            Number fullimm = map.get(IMM) << 2;
            operands.add(ArmOperand.newImmediateLabel(IMM, fullimm, 64));
            break;
        }
        */
        /*
        case TEST_AND_BRANCH: {
            // first operand
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newReadRegister(RT, wd));
        
            // second operand
            var b5 = map.get(SF);
            var b40 = map.get(RM);
            operands.add(ArmOperand.newImmediate(RM, ((b5 << 5) | b40), 8));
        
            // third operand
            Number label = map.get(IMM) << 2;
            operands.add(ArmOperand.newImmediateLabel(IMM, label, 64));
            break;
        }
        */
        /*
        // ldr literal loads, scalar and simd
        case LOAD_REG_LITERAL_FMT1: {
        
            var sf = map.get(SF);
            var simd = map.get(SIMD);
            int wd = 32 * (int) Math.pow(2, sf);
        
            // first operand
            var key = simd << 1;
            operands.add(OPERANDPROVIDE.get(key).apply(RT, map.get(RT), wd));
        
            // TODO need addr of current instruction...
        
            // second operand
            var imm = map.get(IMM) << 2;
            Number label = (imm << (64 - 19)) >> (64 - 19);
            operands.add(ArmOperand.newImmediateLabel(IMM, label, 64));
            break;
        }
        */
        /* 
        // should only be for "prfm" and "ldrsw_reg"
        case LOAD_REG_LITERAL_FMT2: {
        
            // first operand
            var wd = (map.get(OPCODEA) != 0) ? 64 : 32;
            operands.add(newWriteRegister(RT, wd));
        
            // second operand
            Number label = map.get(IMM) << 2;
            operands.add(ArmOperand.newImmediate(IMM, label, 32));
            break;
        }
        */
        /*
        // stnp and ldnp (scalar and simd)
        case LOAD_STORE_PAIR_NO_ALLOC:
        case LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1: {
        
            // bitwidth
            var sf = map.get(SF);
            var simd = map.get(SIMD);
            if (simd == 0)
                sf = sf >> 1;
        
            int wd = 32 * (int) Math.pow(2, sf);
        
            // load or store?
            var isload = (map.get(OPCODEA));
            var key = simd << 1 | isload;
        
            // first and second operands
            operands.add(newRegister(key, RT, wd));
            operands.add(newRegister(key, RM, wd));
        
            // third operand
            operands.add(newReadRegister(RN, wd));
        
            // fourth (optional) operand
            var imm = map.get(IMM) * (wd / 8);
            Number fullimm = (imm << (64 - 7)) >> (64 - 7);
        
            if (type == ArmAsmFieldType.LOAD_STORE_PAIR_NO_ALLOC)
                wd = (wd == 32) ? 8 : 16;
            else // if LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1
                wd = 16;
        
            operands.add(ArmOperand.newImmediate(IMM, fullimm, wd));
            break;
        }
        */
        /*   
        // stgp and ldpsw
        case LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2: {
            // first, second, and third operands
            operands.add(newReadRegister(RT, 64));
            operands.add(newReadRegister(RM, 64));
            operands.add(newReadRegister(RN, 64));
        
            // fourth operand
            operands.add(newImmediate(IMM, 16));
            break;
        }
        */
        /*
        // sturb, ldurb, sturh, ldurh, ldursw, prfum
        // ldursb, ldursh, stur, ldur
        case LOAD_STORE_PAIR_IMM_FMT1:
        case LOAD_STORE_PAIR_IMM_FMT2: {
            // first, and second operands
            var wd = (map.get(OPCODEA) != 0) ? 64 : 32;
            operands.add(newReadRegister(RT, wd));
            operands.add(newReadRegister(RN, 64));
        
            // third operand
            var imm = map.get(IMM);
            Number fullimm = (imm << (64 - 9)) >> (64 - 9);
            operands.add(ArmOperand.newImmediate(IMM, fullimm, 64));
            break;
        }
        */
        /*
        // stur (simd), ldur (simd)
        case LOAD_STORE_PAIR_IMM_FMT3:
        
            // Load/store register (immediate pre-indexed) - C4-286
            // Load/store register (immediate post-indexed) - C4-284
        case LOAD_STORE_IMM_PREPOST_FMT1:
        case LOAD_STORE_IMM_PREPOST_FMT2:
        case LOAD_STORE_IMM_PREPOST_FMT3: {
        
            int wd = 0;
            if (map.containsKey(SIMD)) {
                var sfa = map.get(SFA);
                var sfb = map.get(SFB);
                var sf = (sfb << 2) | sfa;
                wd = 32 * (int) Math.pow(2, sf);
        
            } else {
                wd = 32;
            }
        
            // first, and second operands
            operands.add(newSIMDReadRegister(RT, map.get(RT), wd));
            operands.add(newReadRegister(RN, map.get(RN), 64));
        
            // third operand
            var imm = map.get(IMM);
            Number fullimm = (imm << (16 - 9)) >> (16 - 9);
            operands.add(newImmediate(IMM, fullimm, 16));
            break;
        }
        */
        /*
        case LOAD_STORE_REG_OFF_FMT1:
        case LOAD_STORE_REG_OFF_FMT2:
        case LOAD_STORE_REG_OFF_FMT3: {
        
            // first operand
            var isload = (map.get(OPCODEB) != 0);
            if (isload)
                operands.add(newWriteRegister(RT, 32));
            else
                operands.add(newReadRegister(RT, 32));
        
            // second operand
            operands.add(newReadRegister(RN, 64));
        
            // third operand
            var wd = ((map.get(OPTION) & 0b001) == 0) ? 32 : 64;
            operands.add(newReadRegister(RM, wd));
        
            // fourth operand
            var option = map.get(OPTION);
            if (option == 0b011) {
                var shift = ArmInstructionShift.decodeShift(map.get(SHIFT).intValue());
                operands.add(newSubOperation(SHIFT, shift.toString(), 8));
        
            } else {
                var ext = ArmInstructionExtend.decodeExtend(map.get(OPTION).intValue());
                operands.add(newSubOperation(OPTION, ext.toString(), 8));
            }
        
            // fifth operand
            operands.add(newImmediate(S, 16));
            break;
        }
        */
        /*
        case LOAD_STORE_REG_UIMM_FMT1:
        case LOAD_STORE_REG_UIMM_FMT2:
        case LOAD_STORE_REG_UIMM_FMT3: {
        
            break;
        }
        */
        /*
        case DPR_TWOSOURCE:
        case ADD_SUB_CARRY: {
            // first, second, and third operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
            operands.add(newReadRegister(RN, wd));
            operands.add(newReadRegister(RM, wd));
            break;
        }
        */
        /*
        case DPR_ONESOURCE: {
            // first, and second operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
            operands.add(newReadRegister(RN, wd));
            break;
        }
        */
        /*
        case LOGICAL_SHIFT_REG:
        case ADD_SUB_SHIFT_REG:
        case ADD_SUB_EXT_REG: {
            // first, second, and third operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
            operands.add(newReadRegister(RN, wd));
            operands.add(newReadRegister(RM, wd));
        
            // fourth operand
            // NOTE
            // there is a separate suboperation associated
            // with this operand.... see page C6-777 of ARMv8 ISA manual
            if (type != ArmAsmFieldType.ADD_SUB_EXT_REG) {
                var shift = ArmInstructionShift.decodeShift(map.get(SHIFT).intValue());
                operands.add(newSubOperation(SHIFT, shift.toString(), 8));
        
            } else {
                var ext = ArmInstructionExtend.decodeExtend(map.get(OPTION).intValue());
                operands.add(newSubOperation(OPTION, ext.toString(), 8));
            }
        
            // fifth operand (first suboperation operand)
            operands.add(newImmediate(IMM, 8));
            break;
        }
        */
        /*
        case CONDITIONAL_CMP_REG:
        case CONDITIONAL_CMP_IMM: {
            // first operand
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newReadRegister(RN, wd));
        
            // second operand
            if (type == ArmAsmFieldType.CONDITIONAL_CMP_REG)
                operands.add(newReadRegister(RM, wd));
            else
                operands.add(newImmediate(IMM, 8));
        
            // third operand
            operands.add(newImmediate(NZCV, 8));
        
            // fourth operand
            var cond = map.get(COND);
            var conds = ArmInstructionCondition.decodeCondition(cond).getShorthandle();
            operands.add(newSubOperation(COND, conds, 8));
            break;
        }
        */
        /* 
        case CONDITIONAL_SELECT: {
            // first, second, and third operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
            operands.add(newReadRegister(RN, wd));
            operands.add(newReadRegister(RM, wd));
        
            // fourth operand
            var cond = map.get(COND);
            var conds = ArmInstructionCondition.decodeCondition(cond).getShorthandle();
            operands.add(newSubOperation(COND, conds, 8));
            break;
        }
        */
        /*
        case DPR_THREESOURCE: {
            // first, second, third, and fourth operands
            var wd = (map.get(SF) == 1) ? 64 : 32;
            operands.add(newWriteRegister(RD, wd));
            operands.add(newReadRegister(RN, wd));
            operands.add(newReadRegister(RM, wd));
            operands.add(newReadRegister(RA, wd));
            break;
        }
        */
        default:
            break;
        }

        return operands;
    }
}
