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

    // SPECIAL
    /*mts(""),
    mtse(""),
    mfs(""),
    mfse(""),*/
    msrclr("RD = $msr; $msr = $msr & ~IMM;"),
    msrset("RD = $msr; $msr = $msr | IMM;"),

    // TODO: does each ISA have to define its own implementation for the builtins?
    // e.g., getCarry in microblaze equals "return $msr[carryindex];"
    // maybe this part should be an AST pass, prior to the generation of verilog, which
    // replaces certain builtins with expressions?

    // maybe each ISA can define its own built-ins?

    // MBAR
    // mbar(""),

    // UBRANCH
    br("$pc = $pc + RB;"),
    bra("$pc = RB;"),
    brd("$pc = $pc + RB;"), // TODO: delay slot??
    brad("$pc = RB;"), // TODO: delay slot??

    // ULBRANCH
    brld("RD = $pc; $pc = $pc + RB;"), // TODO: delay slot??
    brald("RD = $pc; $pc = RB;"), // TODO: delay slot??
    brk("RD = $pc; $pc = RB;"), // TODO: MSR[UM] handling for when MMU is enabled?

    // UIBRANCH
    bri("$pc = $pc + sext(IMM);"),
    brai("$pc = sext(IMM);"),
    brid("$pc = $pc + sext(IMM);"), // TODO: delay slot??
    braid("$pc = sext(IMM);"), // TODO: delay slot??

    /*
    // UILBRANCH
    brlid(0xB814_0000, 1, 1, UILBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),
    bralid(0xB81C_0000, 1, 1, UILBRANCH, G_UJUMP, G_AJUMP, G_IJUMP),
    brki(0xB80C_0000, UILBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),
    
    // brk and brald are same instruction??
    
    // CBRANCH
    beq(0x9C00_0000, CBRANCH, G_CJUMP, G_RJUMP),
    beqd(0x9E00_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    bge(0x9CA0_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bged(0x9EA0_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    bgt(0x9C80_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bgtd(0x9E80_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    ble(0x9C60_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bled(0x9E60_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    blt(0x9C40_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bltd(0x9E40_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    bne(0x9C20_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bned(0x9E20_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    
    // CIBRANCH
    beqi(0xBC00_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    beqid(0xBE00_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgei(0xBCA0_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgeid(0xBEA0_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgti(0xBC80_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgtid(0xBE80_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    blei(0xBC60_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bleid(0xBE60_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    blti(0xBC40_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bltid(0xBE40_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bnei(0xBC20_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bneid(0xBE20_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    
    // RETURN
    rtbd(0xB640_0000, 1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    rtid(0xB620_0000, 1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    rted(0xB680_0000, 1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    rtsd(0xB600_0000, 1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    
    // IBARREL
    bsrli(0x6400_0000, IBARREL_FMT1, G_LOGICAL),
    bsrai(0x6400_0200, IBARREL_FMT1, G_LOGICAL),
    bslli(0x6400_0400, IBARREL_FMT1, G_LOGICAL),
    bsefi(0x6400_4000, IBARREL_FMT2, G_LOGICAL),
    bsifi(0x6400_8000, IBARREL_FMT2, G_LOGICAL),
    
    // STREAM
    get(0x6C00_0000, STREAM, G_OTHER),
    put(0x6C00_8000, 2, 0, STREAM, G_OTHER),
    
    // DSTREAM
    getd(0x4C00_0000, 1, 1, DSTREAM, G_OTHER),
    putd(0x4C00_0400, 2, 1, DSTREAM, G_OTHER),
    
        // TODO: put and get requests could be forwarded to the microblaze??
    
    // IMM
    imm(0xB000_0000, 1, 1, IMM, G_IMMV),
    // NOTE: I assign "1" to the delay value of imm
    // since it emulates the atomicity of the instruction    
    */

    // TYPE A
    add("RD = RA + RB;"),
    addc("RD = RA + RB + getCarry(); setCarry(msb(RD));"),
    addk("RD = RA + RB;"),
    addkc("RD = RA + RB + getCarry();"),
    rsub("RD = RB + ~RA + 1;"),
    rsubc("RD = RB + ~RA + 1 + getCarry(); setCarry(msb(RD));"),
    rsubk("RD = RB + ~RA + 1;"),
    rsubkc("RD = RB + ~RA + 1 + getCarry();"),
    cmp("RD = RB + ~RA + 1; RD[31] = signed(RA) > signed(RB);"),
    cmpu("RD = RB + ~RA + 1; RD[31] = unsigned(RA) > unsigned(RB);"),
    mul("RD = lsw(RA * RB);"),
    mulh("RD = msw(RA * RB);"),
    // TODO implement the modifiers/functions/built-ins "lsw" and "msw"
    mulhu("RD = unsigned(RA * RB);"),
    mulhsu("RD = unsigned(signed(RA) * unsigned(RB));"),
    bsrl("RD = RA >> RB[4:0];"),
    bsra("RD = RA >>> RB[4:0];"),
    bsll("RD = RA << RB[4:0];"),
    idiv("if(RA == 0) {RD = 0; $dzo = 1;} else if(RA == -1 && RB == (1 << 31)) {RD = (1 << 31); $dzo = 1;} else {RD = RB / RA;}"),
    // TODO: other flags in MSR (replace $dzo and others by single $MSR meta?
    idivu("if(RA == 0) {RD = 0; $dzo = 1;} else {RD = RB / RA;}"),
    fadd("RD = RA + RB;"),
    // TODO: exceptions for fadd
    frsub("RD = RB) - RA;"),
    fmul("RD = RB) * RA);"),
    fdiv("RD = RB) / RA);"),

    fcmp_un("fcmp.un", "RD = 1;"),
    fcmp_lt("fcmp.lt", "RD = (RB < RA);"),
    fcmp_eq("fcmp.eq", "RD = (RB == RA);"),
    fcmp_le("fcmp.le", "RD = (RB <= RA);"),
    fcmp_gt("fcmp.gt", "RD = (RB > RA);"),
    fcmp_ne("fcmp.ne", "RD = (RB != RA);"),
    fcmp_ge("fcmp.ge", "RD = (RB >= RA);"),
    // TODO: exceptions

    flt("RD = float(RA);"),
    fint("RD = signed(RA);"),
    fsqrt("RD = sqrt(RA);"),
    or("RD = RA | RB;"),
    pcmpbf("if(RB[0:7] == RA[0:7]) RD = 1; else "
            + "if(RB[8:15] == RA[8:15]) RD = 2; else "
            + "if(RB[16:23] == RA[16:23]) RD = 3; else "
            + "if(RB[24:31] == RA[24:31]) RD = 4; else RD = 0;"),
    and("RD = RA & RB;"),
    xor("RD = RA ^ RB;"),
    pcmpeq("RD = RB == RA;"),
    andn("RD = RA ^ ~RB;"),
    pcmpne("RD = ~(RB == RA);"),
    sra("RD[31] = msb(RA); setCarry(lsb(RA)); RD[1:31] = RA[0:30];"),
    src("RD[31] = getCarry(); setCarry(lsb(RA)); RD[1:31] = RA[0:30];"),
    srl("RD[31] = 0; setCarry(lsb(RA)); RD[1:31] = RA[0:30];"),
    sext8("RD = sext(RA[24:31]);"),
    sext16("RD = sext(RA[16:31]);"),
    clz("RD = clz(RA);"), // requires a built in due to complexity of code that must be generated
    /*swapb("byte(RD, 0) = byte(RA, 3); "
    + "byte(RD, 1) = byte(RA, 2); "
    + "byte(RD, 2) = byte(RA, 1); "
    + "byte(RD, 3) = byte(RA, 0);"),*/
    swapb("RD[7:0] = byte(RA, 3); "
            + "RD[15:8] = byte(RA, 2); "
            + "RD[23:16] = byte(RA, 1); "
            + "RD[31:24] = byte(RA, 0);"),

    /* swaph("byte(RD, 0) = byte(RA, 2); "
            + "byte(RD, 1) = byte(RA, 3); "
            + "byte(RD, 2) = byte(RA, 0); "
            + "byte(RD, 3) = byte(RA, 1);"),*/
    swaph("RD[7:0] = byte(RA, 2); "
            + "RD[15:8] = byte(RA, 3); "
            + "RD[23:16] = byte(RA, 0); "
            + "RD[31:24] = byte(RA, 1);"),

    /*   
    wic(0x9000_0068, TYPE_A, G_OTHER),
    wdc(0x9000_0064, TYPE_A, G_OTHER),
    wdc_flush("wdc.flush", 0x9000_0074, TYPE_A, G_OTHER),
    wdc_clear("wdc.clear", 0x9000_0066, TYPE_A, G_OTHER),
    wdc_clear_ea("wdc.clear.ea", 0x9000_00E6, TYPE_A, G_OTHER),
    wdc_ext_flush("wdc.ext.flush", 0x9000_0476, TYPE_A, G_OTHER),
    wdc_ext_clear("wdc.ext.clear", 0x9000_0466, TYPE_A, G_OTHER),
    lbu(0xC000_0000, 2, 0, TYPE_A, G_LOAD),
    lbur(0xC000_0200, 2, 0, TYPE_A, G_LOAD),
    lbuea(0xC000_0080, 2, 0, TYPE_A, G_LOAD),
    lhu(0xC400_0000, 2, 0, TYPE_A, G_LOAD),
    lhur(0xC400_0200, 2, 0, TYPE_A, G_LOAD),
    lhuea(0xC400_0080, 2, 0, TYPE_A, G_LOAD),
    lw(0xC800_0000, 2, 0, TYPE_A, G_LOAD),
    lwr(0xC800_0200, 2, 0, TYPE_A, G_LOAD),
    lwx(0xC800_0400, 2, 0, TYPE_A, G_LOAD),
    lwea(0xC800_0080, 2, 0, TYPE_A, G_LOAD),
    sb(0xD000_0000, 2, 0, TYPE_A, G_STORE),
    sbr(0xD000_0200, 2, 0, TYPE_A, G_STORE),
    sbea(0xD000_0080, 2, 0, TYPE_A, G_STORE),
    sh(0xD400_0000, 2, 0, TYPE_A, G_STORE),
    shr(0xD400_0200, 2, 0, TYPE_A, G_STORE),
    shea(0xD400_0080, 2, 0, TYPE_A, G_STORE),
    sw(0xD800_0000, 2, 0, TYPE_A, G_STORE),
    swr(0xD800_0200, 2, 0, TYPE_A, G_STORE),
    swx(0xD800_0400, 2, 0, TYPE_A, G_STORE),
    swea(0xD800_0080, 2, 0, TYPE_A, G_STORE),
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

    defaultCode("RD = RA;"); // i.e. nop

    private final String name;
    private final String pseudocode;

    private MicroBlazePseudocode(String pseudocode) {
        this.pseudocode = pseudocode;
        this.name = name();
    }

    private MicroBlazePseudocode(String explicitname, String pseudocode) {
        this.pseudocode = pseudocode;
        this.name = explicitname;
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
