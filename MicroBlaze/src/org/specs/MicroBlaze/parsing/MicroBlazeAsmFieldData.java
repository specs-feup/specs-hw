package org.specs.MicroBlaze.parsing;

import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.immediate;
import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.register_read;
import static org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties.register_write;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.IMM;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.IMMW;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.RA;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.RB;
import static org.specs.MicroBlaze.parsing.MicroBlazeAsmField.RD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.MicroBlaze.instruction.MicroBlazeOperand;
import org.specs.MicroBlaze.instruction.MicroBlazeOperandProperties;

import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;

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
            operands.add(new MicroBlazeOperand(immediate, IMM, operandmap.get(IMM))); 
            break;

        ///////////////////////////////////////////////////////////////////////
        case ULBRANCH:
            operands.add(new MicroBlazeOperand(register_write, RD, operandmap.get(RD)));
            operands.add(new MicroBlazeOperand(register_read, RB, operandmap.get(RB)));
            break;

        /////////////////////////////////////////////////////////////////
        case UBRANCH:
            operands.add(new MicroBlazeOperand(register_read, RB, operandmap.get(RB)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case UILBRANCH:
            operands.add(new MicroBlazeOperand(register_write, RD, operandmap.get(RD)));
            operands.add(new MicroBlazeOperand(immediate, IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case UIBRANCH:
            operands.add(new MicroBlazeOperand(immediate, IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case CIBRANCH:
        case RETURN:
            operands.add(new MicroBlazeOperand(register_read, RA, operandmap.get(RA)));
            operands.add(new MicroBlazeOperand(immediate, IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case CBRANCH:
            operands.add(new MicroBlazeOperand(register_read, RA, operandmap.get(RA)));
            operands.add(new MicroBlazeOperand(register_read, RB, operandmap.get(RB)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case IBARREL_FMT1:
            operands.add(new MicroBlazeOperand(register_write, RD, operandmap.get(RD)));
            operands.add(new MicroBlazeOperand(register_read, RA, operandmap.get(RA)));
            operands.add(new MicroBlazeOperand(immediate, IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case IBARREL_FMT2:
            operands.add(new MicroBlazeOperand(register_write, RD, operandmap.get(RD)));
            operands.add(new MicroBlazeOperand(register_read, RA, operandmap.get(RA)));
            operands.add(new MicroBlazeOperand(immediate, IMM, operandmap.get(IMM)));
            operands.add(new MicroBlazeOperand(immediate, IMMW, operandmap.get(IMMW)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case STREAM:
            operands.add(new MicroBlazeOperand(register_write, RD, operandmap.get(RD)));
            operands.add(new MicroBlazeOperand(register_read, RA, operandmap.get(RA)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case DSTREAM:
            operands.add(new MicroBlazeOperand(register_read, RA, operandmap.get(RA)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case IMM:
            operands.add(new MicroBlazeOperand(immediate, IMM, operandmap.get(IMM)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case TYPE_A:
            operands.add(new MicroBlazeOperand(register_write, RD, operandmap.get(RD)));
            operands.add(new MicroBlazeOperand(register_read, RA, operandmap.get(RA)));
            operands.add(new MicroBlazeOperand(register_read, RB, operandmap.get(RB)));
            break;

        ///////////////////////////////////////////////////////////////////////
        case TYPE_B:
            operands.add(new MicroBlazeOperand(register_write, RD, operandmap.get(RD)));
            operands.add(new MicroBlazeOperand(register_read, RA, operandmap.get(RA)));
            operands.add(new MicroBlazeOperand(immediate, IMM, operandmap.get(IMM)));
            break;

        default:
            break;
        }
        
        /*
         * for 
         *  operaands.add(new MicroBlazeOperand(<type>, <fieldname>, <value>);
         */

        return operands;
    }
}
