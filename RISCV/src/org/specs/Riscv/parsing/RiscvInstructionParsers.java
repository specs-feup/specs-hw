package org.specs.Riscv.parsing;

import static org.specs.Riscv.parsing.RiscvAsmFieldType.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmParser;
import pt.up.fe.specs.binarytranslation.asm.parsing.binaryasmparser.BinaryAsmInstructionParser;

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

            // R types
            newInstance(R_OP, RiscvBaseFormats.R.getFormat() + RiscvMainOpcode.OP.getFormat()),
            newInstance(R_AMO, RiscvBaseFormats.R.getFormat() + RiscvMainOpcode.AMO.getFormat()),
            newInstance(R_OPFP, RiscvBaseFormats.R.getFormat() + RiscvMainOpcode.OPFP.getFormat()),

            // R4 types
            newInstance(R4_MADD, RiscvBaseFormats.R4.getFormat() + RiscvMainOpcode.MADD.getFormat()),
            newInstance(R4_NMADD, RiscvBaseFormats.R4.getFormat() + RiscvMainOpcode.NMADD.getFormat()),
            newInstance(R4_NMSUB, RiscvBaseFormats.R4.getFormat() + RiscvMainOpcode.NMSUB.getFormat()),

            // I types
            newInstance(I_OPIMM, RiscvBaseFormats.I.getFormat() + RiscvMainOpcode.OPIMM.getFormat()),
            newInstance(I_LOAD, RiscvBaseFormats.I.getFormat() + RiscvMainOpcode.LOAD.getFormat()),
            newInstance(I_JARL, RiscvBaseFormats.I.getFormat() + RiscvMainOpcode.JARL.getFormat()),

            // S types
            newInstance(S_STOREFP, RiscvBaseFormats.S.getFormat() + RiscvMainOpcode.STOREFP.getFormat()),
            newInstance(S_STOREFP, RiscvBaseFormats.S.getFormat() + RiscvMainOpcode.LOADFP.getFormat()),
            newInstance(S_STOREFP, RiscvBaseFormats.S.getFormat() + RiscvMainOpcode.STORE.getFormat()),

            // U type

            /*
            
            newInstance(I, "immtwelve(12)_rs1(5)_funct3(3)_rd(5)_0010011"), // OP-IMM encoding for opcode
            
            newInstance(S, "immseven(7)_rs2(5)_rs1(5)_opcode(3)_immfive(5)_0100011"), // STORE encoding for
                                                                                      // opcode
            
            newInstance(U, "immtwenty(20)_rd(5)_0_opcode(1)_10111"), // U-type, used for AUIPC (opcode=0) and LUI
            // (opcode=1)
            
            newInstance(LOAD, "immtwelve(12)_rs1(5)_funct3(3)_rd(5)_0000011"), // LOAD encoding for opcode (other
                                                                               // fields
            // equal to I-Type)
            
            newInstance(JALR, "immtwelve(12)_rs1(5)_funct3(3)_rd(5)_1100111"), // JALR encoding for opcode (other
                                                                               // fields
            // equal to I-Type)
            
            newInstance(SB, "bit12(1)_immsix(6)_rs2(5)_rs1(5)_funct3(3)_immfour(4)_bit11(1)_1100011"), // BRANCH
            // encoding
            // for opcode
            
            newInstance(UJ, "bit20(1)_immten(10)_bit11(1)_immeight(8)_rd(5)_1101111"), // unconditional jumps
                                                                                       // (JAL
            // encoding)
            */

            newInstance(UNDEFINED, "x(32)"));
}
