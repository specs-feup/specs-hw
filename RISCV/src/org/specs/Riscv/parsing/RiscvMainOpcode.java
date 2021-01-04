package org.specs.Riscv.parsing;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;

public enum RiscvMainOpcode implements AsmFieldType {

    // R Types
    OP("0110011"), // I-extension and M-extension
    AMO("0101111"), // A-extension
    OPFP("1010011"), // F-extension

    // R4 Types
    MADD("1000011"), // F-extension
    NMADD("1001111"), // F-extension
    NMSUB("1001011"), // F-extension

    // I Types
    OPIMM("0010011"), // I-extension
    LOAD("0000011"), // I-extension
    JARL("1100111"), // I-extension

    // S Types
    STOREFP("0100111"), // F-extension
    LOADFP("0000111"), // F-extension
    STORE("0100011"); // I-extension

    private String format;

    private RiscvMainOpcode(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
