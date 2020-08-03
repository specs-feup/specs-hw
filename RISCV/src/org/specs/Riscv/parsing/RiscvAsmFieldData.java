package org.specs.Riscv.parsing;

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
        return null; // TODO
    }

}
