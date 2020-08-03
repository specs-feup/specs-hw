package org.specs.Riscv.parsing;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;

public enum RiscvAsmFieldType implements AsmFieldType {

    /*
     * Encoding types (i.e., instruction formats) for RISCV
     * see: https://en.wikipedia.org/wiki/RISC-V#ISA_base_and_extensions
     */

    // For the base ISA, i.e., "32-bit RISC-V instruction formats"
    // https://github.com/riscv/riscv-opcodes/blob/master/opcodes-rv32i
    REG2REG,
    IMMEDIATE,
    STORE,
    UPPERIMM,
    // RISU

    BRANCH,
    JUMP
}
