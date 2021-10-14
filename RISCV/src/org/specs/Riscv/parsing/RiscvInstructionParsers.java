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

import static org.specs.Riscv.parsing.RiscvAsmFieldType.*;
import static org.specs.Riscv.parsing.RiscvBaseFormats.*;

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
            newInstance(OP, R.getFormat() + OP.getFormat()),
            newInstance(AMO, R.getFormat() + AMO.getFormat()),

            newInstance(OPFPb, "opcodea(7)_rs2(5)_rs1(5)_rm(3)_rd(5)" + OPFPb.getFormat(),
                    data -> data.get("opcodea").charAt(2) == '0'),

            newInstance(OPFPa, R.getFormat() + OPFPa.getFormat()),

            // R4 types
            newInstance(MADD, R4.getFormat() + MADD.getFormat()),
            newInstance(MSUB, R4.getFormat() + MSUB.getFormat()),
            newInstance(NMADD, R4.getFormat() + NMADD.getFormat()),
            newInstance(NMSUB, R4.getFormat() + NMSUB.getFormat()),

            // I types
            newInstance(OPIMM, I.getFormat() + OPIMM.getFormat()),
            newInstance(LOAD, I.getFormat() + LOAD.getFormat()),
            newInstance(JALR, I.getFormat() + JALR.getFormat()),
            newInstance(LOADFP, I.getFormat() + LOADFP.getFormat()), // TODO: check if correct

            // S types
            newInstance(STOREFP, S.getFormat() + STOREFP.getFormat()),
            // newInstance(LOADFP, S.getFormat() + LOADFP.getFormat()),
            newInstance(STORE, S.getFormat() + STORE.getFormat()),

            // U types
            newInstance(LUI, U.getFormat() + LUI.getFormat()),
            newInstance(AUIPC, U.getFormat() + AUIPC.getFormat()),

            // B types
            newInstance(BRANCH, B.getFormat() + BRANCH.getFormat()),

            // J types
            newInstance(JAL, UJ.getFormat() + JAL.getFormat()),

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
