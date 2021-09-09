package org.specs.MicroBlaze.parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.MicroBlaze.parsing.getters.MicroBlazeAsmBranchGetter;
import org.specs.MicroBlaze.parsing.getters.MicroBlazeAsmOperandGetter;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;

public class MicroBlazeAsmFieldData extends AsmFieldData {

    /*
     * Helpers for IMM values
     */
    // private static boolean postedImm = false;
    // private static int upper16Imm = 0;

    /*
     * re-mapping of <string, string> to <asmfield, string>
     */
    private final Map<MicroBlazeAsmField, Integer> map = new HashMap<MicroBlazeAsmField, Integer>();

    /*
     * Create raw
     */
    public MicroBlazeAsmFieldData(Number addr, AsmFieldType type, Map<String, String> fields) {
        super(addr, type, fields);

        // get int values from fields
        for (var field : MicroBlazeAsmField.values()) {
            if (fields.containsKey(field.getFieldName())) {
                map.put(field, Integer.parseInt(fields.get(field.getFieldName()), 2));
            }
        }
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
     
    public List<Operand> getOperands(InstructionProperties props) {
    
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
    
        // resolve IMM value first, operation has any
        int fullimm = 0;
        if (operandmap.containsKey(IMM)) {
            var lower16 = operandmap.get(IMM);
    
            // sign extend if no posted IMM
            if (MicroBlazeAsmFieldData.postedImm == false) {
                fullimm = (lower16 << (16)) >> (16);
            }
    
            // else combine (assume upper16Imm already shifted up 16 bits)
            else {
                MicroBlazeAsmFieldData.postedImm = false;
                fullimm = upper16Imm | lower16;
            }
        }
    
        // order of operands MUST be preserved (or should be)
        switch (type) {
    
        ///////////////////////////////////////////////////////////////////////
        case MBAR:
            operands.add(newImmediate(IMM, operandmap.get(IMM))); // TODO: needs fullIMM?
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
            operands.add(newImmediate(IMM, fullimm));
            break;
    
        ///////////////////////////////////////////////////////////////////////
        case UIBRANCH:
            operands.add(newImmediate(IMM, fullimm));
            break;
    
        ///////////////////////////////////////////////////////////////////////
        case CIBRANCH:
        case RETURN:
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newImmediate(IMM, fullimm));
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
            operands.add(newImmediate(IMM, fullimm));
            break;
    
        ///////////////////////////////////////////////////////////////////////
        case IBARREL_FMT2:
            operands.add(newWriteRegister(RD, operandmap.get(RD)));
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newImmediate(IMM, operandmap.get(IMM))); // TODO: needs full IMM?
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
            MicroBlazeAsmFieldData.upper16Imm = (operandmap.get(IMM) << 16);
            MicroBlazeAsmFieldData.postedImm = true;
            break;
    
        ///////////////////////////////////////////////////////////////////////
        case TYPE_A:
    
            // TODO add carry output here, after checking instruction type
    
            // TODO QUICK HACK FOR SUPPORT OF EXCEPTION OF OPERAND TYPE IN LD/ST
    
            if (props.getGenericType().contains(InstructionType.G_STORE))
                operands.add(newReadRegister(RD, operandmap.get(RD)));
            else
                operands.add(newWriteRegister(RD, operandmap.get(RD)));
    
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newReadRegister(RB, operandmap.get(RB)));
    
            // TODO add carry input here, after checking instruction type
    
            break;
    
        ///////////////////////////////////////////////////////////////////////
        case TYPE_B:
    
            // if()
    
            // TODO add carry output here, after checking instruction type
    
            // TODO QUICK HACK FOR SUPPORT OF EXCEPTION OF OPERAND TYPE IN LD/ST
    
            if (props.getGenericType().contains(InstructionType.G_STORE))
                operands.add(newReadRegister(RD, operandmap.get(RD)));
            else
                operands.add(newWriteRegister(RD, operandmap.get(RD)));
    
            operands.add(newReadRegister(RA, operandmap.get(RA)));
            operands.add(newImmediate(IMM, fullimm));
    
            // TODO add carry input here, after checking instruction type
    
            break;
    
        default:
            break;
        }
    
        return operands;
    }*/

    /*
     * 
     */
    public Map<MicroBlazeAsmField, Integer> getMap() {
        return map;
    }

    /*
    * Gets a list of integers which represent the operands in the fields
    * This manner of field parsing, maintains the operand order as parsed
    * in the AsmFields
    */
    @Override
    public List<Operand> getOperands(RegisterDump registers) {
        return MicroBlazeAsmOperandGetter.getFrom(this, registers);
    }

    /*
    * Get target of branch if instruction is branch
    */
    @Override
    public Number getBranchTarget(RegisterDump registers) { // (List<Operand> operands) { //TODO: replace by list of
                                                            // operands
        return MicroBlazeAsmBranchGetter.getFrom(this, registers);
    }
}
