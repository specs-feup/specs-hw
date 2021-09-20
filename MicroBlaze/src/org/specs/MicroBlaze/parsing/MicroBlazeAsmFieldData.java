package org.specs.MicroBlaze.parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.MicroBlaze.instruction.MicroBlazeRegisterDump;
import org.specs.MicroBlaze.parsing.getters.MicroBlazeAsmBranchGetter;
import org.specs.MicroBlaze.parsing.getters.MicroBlazeAsmOperandGetter;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

public class MicroBlazeAsmFieldData extends AsmFieldData {

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
        this(fieldData.get(ADDR), fieldData.get(TYPE), fieldData.get(FIELDS));
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
        var mbreg = (MicroBlazeRegisterDump) registers;
        return MicroBlazeAsmOperandGetter.getFrom(this, mbreg);
    }

    /*
    * Get target of branch if instruction is branch
    */
    @Override
    public Number getBranchTarget(RegisterDump registers) { // (List<Operand> operands) { //TODO: replace by
                                                            // list of
        // operands
        var mbreg = (MicroBlazeRegisterDump) registers;
        return MicroBlazeAsmBranchGetter.getFrom(this, mbreg);
    }
}
