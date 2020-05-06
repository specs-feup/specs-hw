/**
 * Copyright 2019 SPeCS.
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

package org.specs.Arm.parsing;

import static org.specs.Arm.parsing.ArmAsmFieldType.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.parsing.AsmParser;
import pt.up.fe.specs.binarytranslation.parsing.binaryasmparser.BinaryAsmInstructionParser;

public interface ArmInstructionParsers {

    static AsmParser newInstance(AsmFieldType type, String rule,
            Predicate<Map<String, String>> predicate) {

        return new BinaryAsmInstructionParser(type, rule, predicate);
    }

    static AsmParser newInstance(AsmFieldType type, String rule) {
        return newInstance(type, rule, null);
    }

    /*
     * For ARMv8 aarch64 instruction set
     */

    List<AsmParser> PARSERS = Arrays.asList(

            ///////////////////////////////////////////////////////////////////
            // 1. Data processing (Immediate) (C4.1.2) ////////////////////////

            newInstance(DPI_PCREL,
                    "opcodea(1)_imml(2)_10000_imm(19)_registerd(5)"),

            newInstance(DPI_ADDSUBIMM,
                    "sf(1)_opcodea(2)_100010_opcodeb(1)_imm(12)_registern(5)_registerd(5)"),

            newInstance(DPI_ADDSUBIMM_TAGS,
                    "sf(1)_opcodea(2)_100011_opcodeb(1)_immh(6)_opcodec(2)_imml(4)_registern(5)_registerd(5)"),

            newInstance(LOGICAL,
                    "sf(1)_opcodea(2)_100100_N(1)_immr(6)_imms(6)_registern(5)_registerd(5)"),

            newInstance(MOVEW,
                    "sf(1)_opcodea(2)_100101_opcodeb(2)_imm(16)_registerd(5)"),
            // TODO predicates

            newInstance(BITFIELD,
                    "sf(1)_opcodea(2)_100110_x_immr(6)_imms(6)_registern(5)_registerd(5)"),

            newInstance(EXTRACT,
                    "sf(1)_001_0011_1x0_registerm(5)_imms(6)_registern(5)_registerd(5)"),

            ///////////////////////////////////////////////////////////////////
            // 2. Branches, Exception Generating and System instructions (C4.1.3)

            newInstance(CONDITIONALBRANCH,
                    "0101010_opcodea(1)_imm(19)_opcodeb(1)_cond(4)"),

            newInstance(EXCEPTION,
                    "11010100_opcodea(3)_imm(16)_opcodeb(3)_opcodec(2)"),

            newInstance(HINTS,
                    "11010101000000110010_registerm(4)_opcodea(3)_11111"),

            newInstance(BARRIER,
                    "11010101000000110011_registerm(4)_opcodea(3)_registert(5)"),

            newInstance(SYSREGMOVE,
                    "1101_0101_00_opcodea(1)_1_O(1)_immh(3)_registern(4)_registerm(4)_imml(3)_registert(5)"),

            newInstance(UCONDITIONALBRANCH_REG,
                    "1101011_opcodea(4)_opcodeb(5)_opcodec(6)_registern(5)_opcoded(5)"),

            newInstance(UCONDITIONALBRANCH_IMM,
                    "opcodea(1)_00101_imm(26)"),

            newInstance(COMPARE_AND_BRANCH_IMM,
                    "sf(1)_011010_opcodea(1)_imm(19)_registert(5)"),

            newInstance(TEST_AND_BRANCH,
                    "sf(1)_011011_opcodea(1)_registerm(5)_imm(14)_registert(5)"),

            ///////////////////////////////////////////////////////////////////
            // 3. Loads and Stores (C4.1.4) ///////////////////////////////////

            newInstance(LOAD_REG_LITERAL_FMT1,
                    "sf(2)_011_simd(1)_00_imm(19)_registert(5)",
                    data -> !((data.get("sf").equals("10") && data.get("simd").equals("0"))
                            || data.get("sf").equals("11"))),

            newInstance(LOAD_REG_LITERAL_FMT2,
                    "opcodea(2)_011000_imm(19)_registert(5)"),

            newInstance(LOAD_STORE_PAIR_NO_ALLOC,
                    "sf(2)_101_simd(1)_000_opcodea(1)_imm(7)_registerm(5)_registern(5)_registert(5)"),

            ///////////////////

            // these parsers gather LOAD_STORE_PAIR_POSTINDEX + LOAD_STORE_PAIR_OFFSET + LOAD_STORE_PAIR_PREINDEX
            // Load/store register pair (offset) - C4-282
            // Load/store register pair (pre-indexed) - C4-282
            // Load/store register pair (post-indexed) - C4-281

            newInstance(LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1,
                    "sf(2)_101_simd(1)_0_memtype(2)_opcodea(1)_imm(7)_registerm(5)_registern(5)_registert(5)",
                    data -> ((!data.get("memtype").equals("00"))
                            && !(data.get("sf").equals("01") && data.get("simd").equals("0")))),

            newInstance(LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2,
                    "sf(2)_1010_0_memtype(2)_opcodea(1)_imm(7)_registerm(5)_registern(5)_registert(5)",
                    data -> (!data.get("memtype").equals("00"))),

            ////////////////////

            // these parsers gather LOAD_STORE_PAIR_UNPRIV and LOAD_STORE_PAIR_IMM_UNSCALED
            // Load/store register (unprivileged) - C4-286
            // Load/store register (unscaled immediate) C4-283

            newInstance(LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT1,
                    "opcodea(2)_111_simd(1)_00_opcodeb(2)_0_imm(9)_opcodec(1)_0_registern(5)_registert(5)",
                    isFmt1().and(isPrivorUnscaledAccess()).and(data -> data.get("simd").equals("0"))),

            newInstance(LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT2,
                    "opcodea(2)_111_simd(1)_00_opcodeb(2)_0_imm(9)_opcodec(1)_0_registern(5)_registert(5)",
                    isPrivorUnscaledAccess().and(data -> data.get("simd").equals("0"))),

            newInstance(LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT3,
                    "sfa(2)_111_simd(1)_00_sfb(1)_opcodeb(1)_0_imm(9)_opcodec(1)_0_registern(5)_registert(5)",
                    isPrivorUnscaledAccess().and(data -> data.get("simd").equals("1"))),

            ////////////////////

            // these parsers gather LOAD_STORE_IMM Pre and post indexed
            // Load/store register (immediate pre-indexed) - C4-286
            // Load/store register (immediate post-indexed) - C4-284

            newInstance(LOAD_STORE_REG_IMM_PREPOST_FMT1,
                    "opcodea(2)_111_simd(1)_00_opcodeb(2)_0_imm(9)_memtype(1)_1_registern(5)_registert(5)",
                    isFmt1().and(isPrePostAccess()).and(data -> data.get("simd").equals("0"))),

            newInstance(LOAD_STORE_REG_IMM_PREPOST_FMT2,
                    "opcodea(2)_111_simd(1)_00_opcodeb(2)_0_imm(9)_memtype(1)_1_registern(5)_registert(5)",
                    isPrePostAccess().and(data -> data.get("simd").equals("0"))),

            newInstance(LOAD_STORE_REG_IMM_PREPOST_FMT3,
                    "sfa(2)_111_simd(1)_00_sfb(1)_opcodeb(1)_0_imm(9)_memtype(1)_1_registern(5)_registert(5)",
                    isPrePostAccess().and(data -> data.get("simd").equals("1"))),

            ////////////////////

            // Load/store register (register offset) - C4-295

            newInstance(LOAD_STORE_REG_OFF_FMT1,
                    "opcodea(2)_111_simd(1)_00_opcodeb(2)_1_registerm(5)_option(3)_S(1)_10_registern(5)_registert(5)",
                    isFmt1().and(data -> data.get("simd").equals("0"))),

            newInstance(LOAD_STORE_REG_OFF_FMT2,
                    "opcodea(2)_111_simd(1)_00_opcodeb(2)_1_registerm(5)_option(3)_S(1)_10_registern(5)_registert(5)",
                    data -> data.get("simd").equals("0")),

            newInstance(LOAD_STORE_REG_OFF_FMT3,
                    "sfa(2)_111_simd(1)_00_sfb(1)_opcodeb(1)_1_registerm(5)_option(3)_S(1)_10_registern(5)_registert(5)",
                    data -> data.get("simd").equals("1")),

            ////////////////////

            // Load/store register (unsigned immediate) - C4-297

            newInstance(LOAD_STORE_REG_UIMM_FMT1,
                    "opcodea(2)_111_simd(1)_01_opcodeb(2)_imm(12)_registern(5)_registert(5)",
                    isFmt1().and(data -> data.get("simd").equals("0"))),

            newInstance(LOAD_STORE_REG_UIMM_FMT2,
                    "opcodea(2)_111_simd(1)_01_opcodeb(2)_imm(12)_registern(5)_registert(5)",
                    data -> data.get("simd").equals("0")),

            newInstance(LOAD_STORE_REG_UIMM_FMT3,
                    "sfa(2)_111_simd(1)_01_sfb(1)_opcodeb(1)_imm(12)_registern(5)_registert(5)",
                    data -> data.get("simd").equals("1")),

            ///////////////////////////////////////////////////////////////////
            // 4. Data Processing (Register) (C4.1.5) /////////////////////////

            newInstance(DPR_TWOSOURCE,
                    "sf(1)_0_opcodea(1)_11010110_registerm(5)_opcodeb(6)_registern(5)_registerd(5)"),

            newInstance(DPR_ONESOURCE,
                    "sf(1)_1_opcodea(1)_11010110_opcodeb(5)_opcodec(6)_registern(5)_registerd(5)"),

            newInstance(LOGICAL_SHIFT_REG,
                    "sf(1)_opcodea(2)_01010_shift(2)_S(1)_registerm(5)_imm(6)_registern(5)_registerd(5)"),

            newInstance(ADD_SUB_SHIFT_REG,
                    "sf(1)_opcodea(2)_01011_shift(2)_0_registerm(5)_imm(6)_registern(5)_registerd(5)"),

            newInstance(ADD_SUB_EXT_REG,
                    "sf(1)_opcodea(2)_01011001_registerm(5)_option(3)_imm(3)_registern(5)_registerd(5)"),

            newInstance(ADD_SUB_CARRY,
                    "sf(1)_opcodea(2)_11010000_registerm(5)_000000_registern(5)_registerd(5)"),

            newInstance(CONDITIONAL_CMP_REG,
                    "sf(1)_opcodea(2)_11010010_registerm(5)_cond(4)_0_opcodeb(1)_registern(5)_opcodec(1)_nzcv(4)"),

            newInstance(CONDITIONAL_CMP_IMM,
                    "sf(1)_opcodea(2)_11010010_imm(5)_cond(4)_1_opcodeb(1)_registern(5)_opcodec(1)_nzcv(4)"),

            newInstance(CONDITIONAL_SELECT,
                    "sf(1)_opcodea(2)_11010100_registerm(5)_cond(4)_opcodeb(2)_registern(5)_registerd(5)"),

            newInstance(DPR_THREESOURCE,
                    "sf(1)_opcodea(2)_11011_opcodeb(3)_registerm(5)_opcodec(1)_registera(5)_registern(5)_registerd(5)"),

            ///////////////////////////////////////////////////////////////////////////////
            // 5. Data Processing -- Scalar Floating-Point and Advanced SIMD (C4.1.6) /////

            // Floating-point data-processing (1 source)- C4-352
            newInstance(FP_DPR_ONESOURCE,
                    "M(1)_0_S(1)_11110_ptype(2)_1_opcodea(6)_10000_registern(5)_registerd(5)"),

            // Floating-point compare C4-355
            newInstance(FP_COMPARE,
                    "M(1)_0_S(1)_11110_ptype(2)_1_registerm(5)_opcodea(2)_1000_registern(5)_opcodeb(5)"),

            // Floating-point immediate C4-356
            newInstance(FP_IMMEDIATE,
                    "M(1)_0_S(1)_11110_ptype(2)_1_imm(8)_100_imms(5)_registerd(5)"),

            // Floating-point conditional compare C4-356
            newInstance(FP_COND_COMPARE,
                    "M(1)_0_S(1)_11110_ptype(2)_1_registerm(5)_cond(4)_01_registern(5)_opcodea(1)_nzcv(4)"),

            // Floating-point data-processing (2 source) C4-357
            newInstance(FP_DPR_TWOSOURCE,
                    "M(1)_0_S(1)_11110_ptype(2)_1_registerm(5)_opcodea(4)_10_registern(5)_registerd(5)"),

            // Floating-point conditional select C4-358
            newInstance(FP_COND_SELECT,
                    "M(1)_0_S(1)_11110_ptype(2)_1_registerm(5)_cond(4)_11_registern(5)_registerd(5)"),

            // Floating-point data-processing (3 source) C4-359
            newInstance(FP_DPR_THREESOURCE,
                    "M(1)_0_S(1)_11111_ptype(2)_opcodea(1)_registerm(5)_opcodeb(1)_registera(5)_registern(5)_registerd(5)"),

            ////////////////////

            newInstance(UNDEFINED, "x(32)"));

    /*
     * predicate == "cannot be ST(U)R or LD(U)R"
     * &&
     * predicate == "cannot be LD(U)RSB or LD(U)RSH
     */
    private static Predicate<Map<String, String>> isFmt1() {
        return data -> !(((data.get("opcodeb").equals("00") || data.get("opcodeb").equals("01"))
                && (data.get("opcodea").equals("10") || data.get("opcodea").equals("11")))
                ||
                ((data.get("opcodeb").equals("11") || data.get("opcodeb").equals("10"))
                        && (data.get("opcodea").equals("00") || data.get("opcodea").equals("01"))));
    }

    /*
     * // predicate == check if memory access type is not privileged or unscaled (i.e. must be pre os post indexed)
     */
    private static Predicate<Map<String, String>> isPrePostAccess() {
        return data -> (data.get("memtype").equals("0") || data.get("memtype").equals("1"));
    }

    /*
     * // predicate == check if memory access type is not privileged type
     */
    private static Predicate<Map<String, String>> isPrivorUnscaledAccess() {
        return data -> (data.get("opcodec").equals("1") || data.get("opcodec").equals("0"));
    }
}
