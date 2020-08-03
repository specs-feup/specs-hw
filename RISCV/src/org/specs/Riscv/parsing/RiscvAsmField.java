package org.specs.Riscv.parsing;

import java.util.HashSet;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.parsing.AsmField;

public enum RiscvAsmField implements AsmField {

    FUNCT7,
    RS2,
    RS1,
    FUNCT3,
    RD,
    OPCODE,
    IMM12HIGH("imm"),
    IMM20HIGH("imm"),
    IMM7HIGH("imm"),
    BIT12,
    IMM6HIGH("imm"),
    IMM4LOW("imm"),
    BIT11,
    BIT20,
    IMM11HIGH("imm"),
    IMM8MID;
    // TODO: check IMM field names (can only one suffice??)

    private String fieldName;

    private RiscvAsmField() {
        this.fieldName = name().toLowerCase();
    }

    private RiscvAsmField(String name) {
        this.fieldName = name;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> getFields() {
        Set<String> sset = new HashSet<String>();
        for (RiscvAsmField val : RiscvAsmField.values()) {
            sset.add(val.getFieldName());
        }

        return sset;
    }
}
