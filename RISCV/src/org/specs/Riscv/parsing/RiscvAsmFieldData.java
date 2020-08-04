package org.specs.Riscv.parsing;

import static org.specs.Riscv.instruction.RiscvOperand.*;
import static org.specs.Riscv.parsing.RiscvAsmField.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;

public class RiscvAsmFieldData extends AsmFieldData {

    /**
     * 
     */
    private static final long serialVersionUID = 4739288388638623897L;

    /*
     * Create raw
     */
    public RiscvAsmFieldData(Number addr, AsmFieldType type, Map<String, String> fields) {
        super(addr, type, fields);
    }

    /*
     * Create from parent class
     */
    public RiscvAsmFieldData(AsmFieldData fieldData) {
        super(fieldData.get(ADDR), fieldData.get(TYPE), fieldData.get(FIELDS));
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

        int fullimm = 0;

        // assign to Operand objects based on field format
        List<Operand> operands = new ArrayList<Operand>();

        // order of operands MUST be preserved (or should be)
        switch (type) {

        ///////////////////////////////////////////////////////////////////////
        case R:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RS1, operandmap.get(RS1)));
            operands.add(newReadRegister(RS2, operandmap.get(RS2)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case I:
        case LOAD:
        case JALR:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RS1, operandmap.get(RS1)));
            operands.add(newImmediate(IMMTWELVE, operandmap.get(IMMTWELVE)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case S:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RS1, operandmap.get(RS1)));

            // build full imm field from 2 fields
            var imm7 = Integer.valueOf(operandmap.get(IMMSEVEN));
            var imm5 = Integer.valueOf(operandmap.get(IMMFIVE));
            fullimm = (imm7 << 5) | imm5;
            operands.add(newImmediate(IMM, fullimm));
            break;

        ///////////////////////////////////////////////////////////////////////
        case U:
        case UJ:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newImmediate(IMMTWENTY, operandmap.get(IMMTWENTY)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case SB:
            operands.add(newReadRegister(RS1, operandmap.get(RS1)));
            operands.add(newReadRegister(RS2, operandmap.get(RS2)));

            // build full imm field from 4 fields
            var bit12 = Integer.valueOf(operandmap.get(BIT12));
            var bit11 = Integer.valueOf(operandmap.get(BIT11));
            var imm6 = Integer.valueOf(operandmap.get(IMMSIX));
            var imm4 = Integer.valueOf(operandmap.get(IMMFOUR));
            fullimm = (bit12 << 12) | (bit11 << 11) | (imm6 << 5) | (imm4 << 1);
            operands.add(newImmediate(IMM, fullimm));
            break;

        ///////////////////////////////////////////////////////////////////////
        default:
            break;
        }

        return operands;
    }
}
