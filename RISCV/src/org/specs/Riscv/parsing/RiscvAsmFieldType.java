package org.specs.Riscv.parsing;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;

public enum RiscvAsmFieldType implements AsmFieldType {

    /*
     * Encoding types (i.e., instruction formats) for RISCV
     * see: https://en.wikipedia.org/wiki/RISC-V#ISA_base_and_extensions
     */

    // For the base ISA, i.e., "32-bit RISC-V instruction formats"
    // https://github.com/riscv/riscv-opcodes/blob/master/opcodes-rv32i
    R_OP,
    R_AMO,
    R_OPFP,

    R4_MADD,
    R4_NMADD,
    R4_NMSUB,

    I_OPIMM,
    I_LOAD,
    I_JARL,

    S_STOREFP,
    S_LOADFP,
    S_LOAD,

    U,
    // RISU
    /*
    LOAD, // I-type variant
    JALR, // I-type variant
    SB,
    UJ,*/
    // variants

    UNDEFINED
}
