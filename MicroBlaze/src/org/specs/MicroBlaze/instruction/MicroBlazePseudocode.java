/**
 * Copyright 2020 SPeCS.
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

package org.specs.MicroBlaze.instruction;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public enum MicroBlazePseudocode implements InstructionPseudocode {

    // TYPE A
    add("RD = RA + RB;"),
    addc("RD = RA + RB + getCarry(); setCarry(msb(RD));"),
    addk("RD = RA + RB;"),
    addkc("RD = RA + RB + getCarry();"),
    // TODO: how to handle carry input?? keeping carry
    // is easier (?) but enforcing a carry out back to the processor is not

    rsub("RD = RB + ~RA + 1;"),
    rsubc("RD = RB + ~RA + 1 + getCarry(); setCarry(msb(RD));"),
    rsubk("RD = RB + ~RA + 1;"),
    rsubkc("RD = RB + ~RA + 1 + getCarry();"),
    // TODO: how to handle carry input?? keeping carry
    // is easier (?) but enforcing a carry out back to the processor is not

    cmp("RD = RB + ~RA + 1; msb(RD) = (RA > RB);"),
    // TODO implement the built-in function "msb"

    // cmpu("RD = RB + ~RA + 1; msb(RD) = (RA > RB);"); TODO: how to deal with unsigned values??

    mul("RD = lsw(RA x RB);"),
    mulh("RD = msw(RA x RB);"),
    // TODO implement the modifiers/functions/built-ins "lsw" and "msw"

    /*
    mulhu(0x4000_0003, 3, 0, TYPE_A, G_MUL),
    mulhsu(0x4000_0002, 3, 0, TYPE_A, G_MUL),*/

    bsrl("RD = RA >> RB[4:0];"), // TODO: handle this syntax
    bsra("RD = RA >>> RB[4:0];"), // TODO: handle this syntax
    bsll("RD = RA << RB[4:0];"), // TODO: handle this syntax

    /*idiv(""),
    idivu(""),    
    fadd(0x5800_0000, 6, 0, TYPE_A, G_FLOAT, G_ADD),
    frsub(0x5800_0080, 6, 0, TYPE_A, G_FLOAT, G_SUB),
    fmul(0x5800_0100, 6, 0, TYPE_A, G_FLOAT, G_MUL),
    fdiv(0x5800_0180, 30, 0, TYPE_A, G_FLOAT),
    fcmp_un("fcmp.un", 0x5800_0200, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_lt("fcmp.lt", 0x5800_0210, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_eq("fcmp.eq", 0x5800_0220, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_le("fcmp.le", 0x5800_0230, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_gt("fcmp.gt", 0x5800_0240, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_ne("fcmp.ne", 0x5800_0250, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_ge("fcmp.ge", 0x5800_0260, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    flt(0x5800_0280, 6, 0, TYPE_A, G_FLOAT),
    fint(0x5800_0300, 5, 0, TYPE_A, G_FLOAT),
    fsqrt(0x5800_0380, 29, 0, TYPE_A, G_FLOAT),
    or(0x8000_0000, TYPE_A, G_LOGICAL),
    pcmpbf(0x8000_0400, TYPE_A, G_CMP),
    and(0x8400_0000, TYPE_A, G_LOGICAL),
    xor(0x8800_0000, TYPE_A, G_LOGICAL),
    pcmpeq(0x8800_0400, TYPE_A, G_CMP),
     */

    // TYPE B
    addi("RD = RA + IMM;"),
    addic("RD = RA + IMM + getCarry(); setCarry(msb(RD));"),
    addik("RD = RA + IMM;"),
    addikc("RD = RA + IMM + getCarry();"),
    rsubi("RD = IMM + ~RA + 1;"),
    rsubic("RD = IMM + ~RA + getCarry();"),
    rsubik("RD = IMM + ~RA + 1;"),
    rsubikc("RD = IMM + ~RA + getCarry();"),
    muli("RD = lsw(RA * IMM);"),
    ori("RD = RA | IMM;"),
    andi("RD = RA & IMM;"),
    xori("RD = RA ^ IMM;"),
    andni("RD = RA | (~IMM);"),
    /*
    lbui(0xE000_0000, 2, 0, TYPE_B, G_LOAD),
    lhui(0xE400_0000, 2, 0, TYPE_B, G_LOAD),
    lwi(0xE800_0000, 2, 0, TYPE_B, G_LOAD),
    sbi(0xF000_0000, 2, 0, TYPE_B, G_STORE),
    shi(0xF400_0000, 2, 0, TYPE_B, G_STORE),
    swi(0xF800_0000, 2, 0, TYPE_B, G_STORE),
    */

    defaultCode("RD = RD;"); // i.e. nop

    /*
    mulhu(0x4000_0003, 3, 0, TYPE_A, G_MUL),
    mulhsu(0x4000_0002, 3, 0, TYPE_A, G_MUL),*/

    private final String name;
    private final String pseudocode;

    private MicroBlazePseudocode(String pseudocode) {
        this.pseudocode = pseudocode;
        this.name = name();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return pseudocode;
    }

    @Override
    public PseudoInstructionContext getParseTree() {
        var parser = new PseudoInstructionParser(
                new CommonTokenStream(new PseudoInstructionLexer(new ANTLRInputStream(this.getCode()))));
        return parser.pseudoInstruction();
    }

    /*
     * Using this for now to deal with unimplemented codes!
     */
    public static InstructionPseudocode getDefault() {
        return MicroBlazePseudocode.defaultCode;
    }
}
