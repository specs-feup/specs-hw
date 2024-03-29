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

package org.specs.MicroBlaze.parsing;

import static org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmParser;
import pt.up.fe.specs.binarytranslation.asm.parsing.binaryasmparser.BinaryAsmInstructionParser;

public interface MicroBlazeInstructionParsers {

    static AsmParser newInstance(AsmFieldType type, String rule,
            Predicate<Map<String, String>> predicate) {

        return new BinaryAsmInstructionParser(type, rule, predicate);
    }

    static AsmParser newInstance(AsmFieldType type, String rule) {
        return newInstance(type, rule, null);
    }

    List<AsmParser> PARSERS = Arrays.asList(

            newInstance(MSR, "100101_registerd(5)_1000_opcodea(1)_0_imm(15)"),
            newInstance(MFS, "100101_registerd(5)_0_opcodea(1)_00010_registers(14)"),
            newInstance(MTS, "100101_0_opcodea(1)_000_registera(5)_11_registers(14)"),

            // newInstance(SPECIAL, "100101_opcodea(5)_opcodeb(5)_opcodec(2)_opcoded(14)"),

            newInstance(MBAR, "101110_imm(5)_00010_0(13)_100"),

            newInstance(UBRANCH, "100110_0(5)_opcodea(2)_000_registerb(5)_0(11)"),
            newInstance(ULBRANCH, "100110_registerd(5)_opcodea(2)_100_registerb(5)_0(11)"),
            newInstance(UIBRANCH, "101110_0(5)_opcodea(2)_000_imm(16)"),
            newInstance(UILBRANCH, "101110_registerd(5)_opcodea(2)_100_imm(16)"),
            // only these 4 types are capable of absolute OR relative jumps (via opcodea)

            newInstance(CBRANCH, "100111_opcodea(5)_registera(5)_registerb(5)_0(11)"),
            newInstance(CIBRANCH, "101111_opcodea(5)_registera(5)_imm(16)"),

            newInstance(RETURN, "101101_opcodea(5)_registera(5)_imm(16)"),
            newInstance(IBARREL_FMT1, "011001_registerd(5)_registera(5)_00000_opcodeb(2)_0000_imm(5)"),
            newInstance(IBARREL_FMT2, "011001_registerd(5)_registera(5)_opcodea(2)_000_immw(5)_0_imm(5)"),
            newInstance(STREAM, "011011_registerd(5)_registera(5)_opcodea(1)_0(15)"),
            newInstance(DSTREAM, "010011_registerd(5)_registera(5)_registerb(5)_opcodea(1)_0(10)"),
            newInstance(IMM, "101100_00000_00000_imm(16)"),

            newInstance(TYPE_A_STORE, "opcodea(2)_0_opcodeb(3)_registerd(5)_registera(5)_registerb(5)_opcodec(11)",
                    data -> (data.get("opcodea").equals("11") && data.get("opcodeb").charAt(0) == '1')),

            newInstance(TYPE_A, "opcodea(2)_0_opcodeb(3)_registerd(5)_registera(5)_registerb(5)_opcodec(11)"),

            newInstance(TYPE_B_STORE, "opcodea(2)_1_opcodeb(3)_registerd(5)_registera(5)_imm(16)",
                    data -> (data.get("opcodea").equals("11") && data.get("opcodeb").charAt(0) == '1')),

            newInstance(TYPE_B, "opcodea(2)_1_opcodeb(3)_registerd(5)_registera(5)_imm(16)"),

            newInstance(UNDEFINED, "x(32)")); // note: invented code

    // TODO typeA and typeB subtyupes for store instructions where the operand r/w types are different
}
