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
            newInstance(REG2REG, "funct7_rs2_rs1_funct3_rd_0110000"), // OP encoding for opcode
            newInstance(IMMEDIATE, "imm12high_rs1_funct3_rd_0010000")// , // OP-IMM encoding for opcode

    // newInstance(UPPERIMM, "imm20high_rd_0010000"), // OP-IMM encoding for opcode
    );
}
