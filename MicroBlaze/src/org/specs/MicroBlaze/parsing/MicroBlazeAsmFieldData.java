package org.specs.MicroBlaze.parsing;

import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.*;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmFields.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.MicroBlaze.instruction.MicroBlazeOperand;

import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;
import pt.up.fe.specs.util.SpecsSystem;

public class MicroBlazeAsmFieldData extends AsmFieldData {

    /*
     * Create raw
     */
    public MicroBlazeAsmFieldData(AsmFieldType type, Map<String, String> fields) {
        super(type, fields);
    }

    /*
     * Create from parent class
     */
    public MicroBlazeAsmFieldData(AsmFieldData fieldData) {
        super(fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * Copy "constructor"
     */
    public MicroBlazeAsmFieldData copy() {
        return new MicroBlazeAsmFieldData(this.getType(),
                new HashMap<String, String>(this.getFields()));
    }

    /*
     * Gets a list of integers which represent the operands in the fields
     * This manner of field parsing, maintains the operand order as parsed
     * in the AsmFields
     */
    public List<Operand> getOperands() {

        MicroBlazeAsmFieldType type = (MicroBlazeAsmFieldType) this.get(TYPE);
        var map1 = this.get(FIELDS);

        // get int values from fields
        Map<String, Integer> operandmap = new HashMap<String, Integer>();
        for (String field : MicroBlazeAsmFields.getFields()) {
            if (map1.containsKey(field)) {
                operandmap.put(field, Integer.parseInt(map1.get(field), 2));
            }
        }

        // assign to Operand objects based on field format
        List<Operand> operands = new ArrayList<Operand>();

        // order of operands MUST be preserved (or should be)
        switch (type) {

        ///////////////////////////////////////////////////////////////////////
        case MBAR:
            operands.add(new MicroBlazeOperand(immediate, operandmap.get(IMM.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case ULBRANCH:
            operands.add(new MicroBlazeOperand(register_write, operandmap.get(RD.getFieldName())));
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RB.getFieldName())));
            break;

        /////////////////////////////////////////////////////////////////
        case UBRANCH:
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RB.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case UILBRANCH:
            operands.add(new MicroBlazeOperand(register_write, operandmap.get(RD.getFieldName())));
            operands.add(new MicroBlazeOperand(immediate, operandmap.get(IMM.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case UIBRANCH:
            operands.add(new MicroBlazeOperand(immediate, operandmap.get(IMM.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case CIBRANCH:
        case RETURN:
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RA.getFieldName())));
            operands.add(new MicroBlazeOperand(immediate, operandmap.get(IMM.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case CBRANCH:
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RA.getFieldName())));
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RB.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case IBARREL_FMT1:
            operands.add(new MicroBlazeOperand(register_write, operandmap.get(RD.getFieldName())));
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RA.getFieldName())));
            operands.add(new MicroBlazeOperand(immediate, operandmap.get(IMM.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case IBARREL_FMT2:
            operands.add(new MicroBlazeOperand(register_write, operandmap.get(RD.getFieldName())));
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RA.getFieldName())));
            operands.add(new MicroBlazeOperand(immediate, operandmap.get(IMM.getFieldName())));
            operands.add(new MicroBlazeOperand(immediate, operandmap.get(IMMW.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case STREAM:
            operands.add(new MicroBlazeOperand(register_write, operandmap.get(RD.getFieldName())));
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RA.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case DSTREAM:
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RA.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case IMM:
            operands.add(new MicroBlazeOperand(immediate, operandmap.get(IMM.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case TYPE_A:
            operands.add(new MicroBlazeOperand(register_write, RD, operandmap.get(RD.getFieldName())));
            operands.add(new MicroBlazeOperand(register_read, RA, operandmap.get(RA.getFieldName())));
            operands.add(new MicroBlazeOperand(register_read, RB, operandmap.get(RB.getFieldName())));
            break;

        ///////////////////////////////////////////////////////////////////////
        case TYPE_B:
            operands.add(new MicroBlazeOperand(register_write, operandmap.get(RD.getFieldName())));
            operands.add(new MicroBlazeOperand(register_read, operandmap.get(RA.getFieldName())));
            operands.add(new MicroBlazeOperand(immediate, operandmap.get(IMM.getFieldName())));
            break;

        default:
            break;
        }

        return operands;
    }
}
