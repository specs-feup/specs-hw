package org.specs.MicroBlaze.parsing;

import static org.specs.MicroBlaze.instruction.MicroBlazeOperand.*;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;

public class MicroBlazeAsmFieldData extends AsmFieldData {

    /*
     * Create raw
     */
    public MicroBlazeAsmFieldData(Number addr, AsmFieldType type, Map<String, String> fields) {
        super(addr, type, fields);
    }

    /*
     * Create from parent class
     */
    public MicroBlazeAsmFieldData(AsmFieldData fieldData) {
        super(fieldData.get(ADDR), fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * Copy "constructor"
     */
    public MicroBlazeAsmFieldData copy() {
        return new MicroBlazeAsmFieldData(
                this.get(ADDR), this.getType(),
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
        Map<MicroBlazeAsmField, Integer> operandmap = new HashMap<MicroBlazeAsmField, Integer>();
        for (MicroBlazeAsmField field : MicroBlazeAsmField.values()) {
            if (map1.containsKey(field.getFieldName())) {
                operandmap.put(field, Integer.parseInt(map1.get(field.getFieldName()), 2));
            }
        }

        // assign to Operand objects based on field format
        List<Operand> operands = new ArrayList<Operand>();

        // order of operands MUST be preserved (or should be)
        switch (type) {

        ///////////////////////////////////////////////////////////////////////
        case MBAR:
            operands.add(newImmediate(IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case ULBRANCH:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RB, operandmap.get(RB)));
            break;

        /////////////////////////////////////////////////////////////////
        case UBRANCH:
            operands.add(newReadRegister(RB, operandmap.get(RB)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case UILBRANCH:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newImmediate(IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case UIBRANCH:
            operands.add(newImmediate(IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case CIBRANCH:
        case RETURN:
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newImmediate(IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case CBRANCH:
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newReadRegister(RB, operandmap.get(RB)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case IBARREL_FMT1:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newImmediate(IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case IBARREL_FMT2:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newImmediate(IMM, operandmap.get(IMM)));
            operands.add(newImmediate(IMMW, operandmap.get(IMMW)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case STREAM:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case DSTREAM:
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case IMM:
            operands.add(newImmediate(IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case TYPE_A:

            // TODO add carry output here, after checking instruction type

            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newReadRegister(RB, operandmap.get(RB)));

            // TODO add carry input here, after checking instruction type

            // TODO BUG HERE FOR SWI
            // swi only has 2 oeprands!

            break;

        ///////////////////////////////////////////////////////////////////////
        case TYPE_B:

            // TODO add carry output here, after checking instruction type

            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newImmediate(IMM, operandmap.get(IMM)));

            // TODO add carry input here, after checking instruction type

            break;

        default:
            break;
        }

        return operands;
    }
}
