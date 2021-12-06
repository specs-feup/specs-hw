/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package org.specs.Riscv.parsing;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;

public enum RiscvAsmFieldType implements AsmFieldType {

    /*
     * Encoding types (i.e., instruction formats) for RISCV
     * see: https://en.wikipedia.org/wiki/RISC-V#ISA_base_and_extensions
     */

    // For the base ISA, i.e., "32-bit RISC-V instruction formats"
    // https://github.com/riscv/riscv-opcodes/blob/master/opcodes-rv32i

    // R Types
    OP("0110011"), // I-extension and M-extension
    AMO("0101111"), // A-extension

    // 3 formats for OP-FP (based on R type)
    OPFPa("1010011"), // F-extension
    OPFPb("1010011"), // F-extension (same on purpose)
    OPFPc("1010011"), // F-extension

    // R4 Types
    MADD("1000011"), // F-extension
    MSUB("1000111"), // F-extension
    NMADD("1001111"), // F-extension
    NMSUB("1001011"), // F-extension

    // I Types
    OPIMM("0010011"), // I-extension
    LOAD("0000011"), // I-extension
    JALR("1100111"), // I-extension

    // S Types
    STOREFP("0100111"), // F-extension
    LOADFP("0000111"), // F-extension
    STORE("0100011"), // I-extension

    // U Types
    LUI("0110111"), // I-extension
    AUIPC("0010111"), // I-extension

    // B Types
    BRANCH("1100011"), // I-extension

    // J Type
    JAL("1101111"), // I-extension

    INVALID("0011111"), // This is needed for custom instruction generation, in accordance with the RISCV ISA specification, this is an invalid 32 bit opcode since it represents a 48 bit instruction
    
    UNDEFINED("0000000");

    private String format;

    private RiscvAsmFieldType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
