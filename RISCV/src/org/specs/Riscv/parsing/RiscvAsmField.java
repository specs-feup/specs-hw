package org.specs.Riscv.parsing;

import java.util.HashSet;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;

public enum RiscvAsmField implements AsmField {

    // FUNCT7,
    RS3,
    RS2,
    RS1,
    RM,
    // FUNCT3,
    RD,
    OPCODEA,
    OPCODEB,
    IMM,
    IMMTWENTY,
    IMMTWELVE,
    IMMTEN,
    IMMEIGHT,
    IMMSEVEN,
    IMMSIX,
    IMMFIVE,
    IMMFOUR,
    BIT12,
    BIT11,
    BIT20;

    // TODO: check IMM field names (can only one suffice??)

    private String fieldName;

    private RiscvAsmField() {
        this.fieldName = name().toLowerCase();
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
