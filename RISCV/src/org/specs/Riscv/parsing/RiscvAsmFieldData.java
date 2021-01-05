package org.specs.Riscv.parsing;

import static org.specs.Riscv.instruction.RiscvOperand.*;
import static org.specs.Riscv.parsing.RiscvAsmField.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class RiscvAsmFieldData extends AsmFieldData {

    /*
     * remmaping of <string, string> to <asmfield, string>
     */
    private final Map<RiscvAsmField, Integer> map = new HashMap<RiscvAsmField, Integer>();

    /*
     * Create raw
     */
    public RiscvAsmFieldData(Number addr, AsmFieldType type, Map<String, String> fields) {
        super(addr, type, fields);

        // get int values from fields
        for (RiscvAsmField field : RiscvAsmField.values()) {
            if (fields.containsKey(field.getFieldName())) {
                map.put(field, Integer.parseInt(fields.get(field.getFieldName()), 2));
            }
        }
    }

    /*
     * Create from parent class
     */
    public RiscvAsmFieldData(AsmFieldData fieldData) {
        this(fieldData.get(ADDR), fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * Copy "constructor"
     */
    public RiscvAsmFieldData copy() {
        return new RiscvAsmFieldData(
                this.get(ADDR), this.getType(),
                new HashMap<String, String>(this.getFields()));
    }

    /*
     * 
     */
    public Map<RiscvAsmField, Integer> getMap() {
        return map;
    }

    /*
     * Gets a list of integers which represent the operands in the fields
     * This manner of field parsing, maintains the operand order as parsed
     * in the AsmFields
     */
    public List<Operand> getOperands() {

        RiscvAsmFieldType type = (RiscvAsmFieldType) this.get(TYPE);
        var map1 = this.get(FIELDS);

        // get int values from fields
        Map<RiscvAsmField, Integer> operandmap = new HashMap<RiscvAsmField, Integer>();
        for (RiscvAsmField field : RiscvAsmField.values()) {
            if (map1.containsKey(field.getFieldName())) {
                operandmap.put(field, Integer.parseInt(map1.get(field.getFieldName()), 2));
            }
        }

        int fullimm = 0, bit20 = 0, bit11 = 0, bit12 = 0, imm6, imm4, imm8, imm10;

        // assign to Operand objects based on field format
        List<Operand> operands = new ArrayList<Operand>();

        // order of operands MUST be preserved (or should be)
        switch (type) {

        ///////////////////////////////////////////////////////////////////////
        // R types
        case OP:
        case AMO:
        case OPFPa:
            // case OPFPb:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RS1, operandmap.get(RS1)));
            operands.add(newReadRegister(RS2, operandmap.get(RS2)));
            break;

        ///////////////////////////////////////////////////////////////////////
        // I types
        case OPIMM:
        case LOAD:
        case JALR:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RS1, operandmap.get(RS1)));
            operands.add(newImmediate(IMMTWELVE, operandmap.get(IMMTWELVE)));
            break;

        ///////////////////////////////////////////////////////////////////////
        // S types
        case STOREFP:
        case LOADFP:
        case STORE:
            operands.add(newReadRegister(RS1, operandmap.get(RS1)));
            operands.add(newReadRegister(RS2, operandmap.get(RS2)));

            // build full imm field from 2 fields
            var imm7 = Integer.valueOf(operandmap.get(IMMSEVEN));
            var imm5 = Integer.valueOf(operandmap.get(IMMFIVE));
            fullimm = (imm7 << 5) | imm5;
            operands.add(newImmediate(IMM, fullimm));
            break;

        ///////////////////////////////////////////////////////////////////////
        // U types
        case LUI:
        case AUIPC:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newImmediate(IMMTWENTY, operandmap.get(IMMTWENTY)));
            break;

        ///////////////////////////////////////////////////////////////////////
        // UJ types
        case JAL:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));

            // build full imm field from 4 fields
            bit20 = Integer.valueOf(operandmap.get(BIT20));
            imm10 = Integer.valueOf(operandmap.get(IMMTEN));
            bit11 = Integer.valueOf(operandmap.get(BIT11));
            imm8 = Integer.valueOf(operandmap.get(IMMEIGHT));
            fullimm = (bit20 << 20) | (imm8 << 12) | (bit11 << 11) | (imm10 << 1);
            operands.add(newImmediate(IMM, fullimm));
            break;

        ///////////////////////////////////////////////////////////////////////
        // SB-type
        case BRANCH:
            operands.add(newReadRegister(RS1, operandmap.get(RS1)));
            operands.add(newReadRegister(RS2, operandmap.get(RS2)));

            // build full imm field from 4 fields
            bit12 = Integer.valueOf(operandmap.get(BIT12));
            bit11 = Integer.valueOf(operandmap.get(BIT11));
            imm6 = Integer.valueOf(operandmap.get(IMMSIX));
            imm4 = Integer.valueOf(operandmap.get(IMMFOUR));
            fullimm = (bit12 << 12) | (bit11 << 11) | (imm6 << 5) | (imm4 << 1);
            operands.add(newImmediate(IMM, fullimm));
            break;

        ///////////////////////////////////////////////////////////////////////
        default:
            break;
        }

        return operands;
    }

    /*
    * Get target of branch if instruction is branch
    */
    public Number getBranchTarget() {
        return RiscvAsmBranchTargetGetter.getFrom(this);
    }
}
