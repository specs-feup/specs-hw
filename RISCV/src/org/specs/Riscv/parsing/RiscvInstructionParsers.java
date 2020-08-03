package org.specs.Riscv.parsing;

import static org.specs.Riscv.parsing.RiscvAsmFieldType.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.parsing.AsmParser;
import pt.up.fe.specs.binarytranslation.parsing.binaryasmparser.BinaryAsmInstructionParser;

public interface RiscvInstructionParsers {

    static AsmParser newInstance(AsmFieldType type, String rule,
            Predicate<Map<String, String>> predicate) {

        return new BinaryAsmInstructionParser(type, rule, predicate);
    }

    static AsmParser newInstance(AsmFieldType type, String rule) {
        return newInstance(type, rule, null);
    }

    // see "https://www2.eecs.berkeley.edu/Pubs/TechRpts/2014/EECS-2014-54.pdf"
    // page 49 for encodings for field "opcode"
    List<AsmParser> PARSERS = Arrays.asList(
            newInstance(R, "funct7(7)_rs2(5)_rs1(5)_funct3(3)_rd(5)_0110000(7)"), // OP encoding for opcode
            newInstance(I, "imm12bits(12)_rs1(5)_funct3(3)_rd(5)_0010000(7)"), // OP-IMM encoding for opcode
            newInstance(S, "imm7bits(7)_rs2(5)_rs1(5)_funct3(3)_imm5bits(5)_0100100(7)"), // STORE encoding for opcode
            newInstance(U, "imm20bits(20)_rd(5)_0_opcode_101"), // U-type, used for AUIPC (opcode=0) and LUI (opcode=1)
            newInstance(LOAD, "imm12bits(1)_rs1(5)_funct3(3)_rd(5)_0000000"), // LOAD encoding for opcode (other fields
                                                                              // equal to
            // I-Type)

            newInstance(JALR, "imm12bits(12)_rs1(5)_funct3(3)_rd(5)_1100100"), // JALR encoding for opcode (other fields
                                                                               // equal to
            // I-Type)

            newInstance(SB, "bit12_imm6bits(6)_rs2(5)_rs1(5)_funct3(3)_imm4bits(4)_bit11_1100000"), // BRANCH encoding
                                                                                                    // for opcode
            newInstance(UJ, "bit20_imm10bits(10)_bit11_imm8bits(8)_rd(5)_1101100") // unconditional jumps (JAL encoding)
    );
}
