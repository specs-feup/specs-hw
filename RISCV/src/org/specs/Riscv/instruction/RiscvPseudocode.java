/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.specs.Riscv.instruction;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public enum RiscvPseudocode implements InstructionPseudocode {
    
    add("RD = RA + RB;"),
    sub("RD = RA - RB;"),
    slt("if(signed(RA) < signed(RB)) RD = 1; else RD = 0;"),
    sltu("if(unsigned(RA) < unsigned(RB)) RD = 1; else RD = 0;"),
    xor("RD = RA ^ RB;"),
    or("RD = RA | RB;"),
    and("RD = RA & RB;"),
    sll("RD = RA << RB[4:0];"),
    srl("RD = RA >> RB[4:0];"),
    sra("RD = RA >>> RB[4:0];"),
    

    addi("RD = RA + sext(IMM);"),
    slti("if(signed(RA) < signed(IMM)) RD = 1; else RD = 0;"),
    sltiu("if(unsigned(RA) < unsigned(IMM)) RD = 1; else RD = 0;"),
    xori("RD = RA ^ IMM;"),
    ori("RD = RA | IMM;"),
    andi("RD = RA & IMM;"),
    slli("RD = RS << IMM[4:0];"),
    srli("RD = RS >> IMM[4:0];"),
    srai("RD = RS >>> IMM[4:0];"),
    //lb(""),
    //lh(""),
    //lw(""),
    //lnu(""),
    //lhu(""),
    jalr("RD = $pc + 4; $pc = RA + sext(IMM);"),
    
    //sb(""),
    //sh(""),
    //sw(""),
    
    //lui(""),
    auipic("RD = $pc + (IMM << 12);"),
    
    beq("if(RA == RB) $pc = $pc + sext(IMM);"),
    bne("if(RA != RB) $pc = $pc + sext(IMM);"),
    blt("if(RA < RB) $pc = $pc + sext(IMM);"),
    bge("if(RA > RB) $pc = $pc + sext(IMM);"),
    bltu("if(unsigned(RA) < unsigned(RB)) $pc = $pc + sext(IMM);"),
    bgeu("if(unsigned(RA) > unsigned(RB)) $pc = $pc + sext(IMM);"),
    
    jal("RD = $pc + 4; $pc = $pc + sext(IMM);"),
    
    mul("RD = lsw(RA * RB);"),
    mulh("RD = msw(RA * RB);"),
    mulhsu("RD = msw(signed(RA) * unsigned(RB));"),
    mulhu("RD = msw(unsigned(RA) * unsigned(RB));"),
    div("RD = signed(RA) / signed(RB);"), // Nota: na especificação é referido que o div e o rem podem ser juntos numa só operação, deve-se ter isto em conta ?
    divu("RD = unsigned(RA) / unsigned(RB);"),
    //rem("RD = "),
    //remu("RD = "),
    
    //lr_w("lr.w", "" ),
    //sc_w("sc.w", ""),
    //amoswap_w("amoswap.w", ""),
    //amoadd_w("amoadd.w" , ""),
    //amoxor_w("amoxor.w", ""),
    //amoand_w("amoand.w", ""),
    //amoor_w("amoor.w", ""),
    //amomin_w("amomin.w", ""),
    //amomax_w("amomax.w", ""),
    //amominu_w("amominu.w", ""),
    //amomaxu_w("amomaxu.w", ""),
    
    //Falta lidar com as excepções dos float (Nan e assim) e tambem implementar o rounding mode
    
    fadd_s("fadd.s", "RD = float(RA) + float(RB);"),
    fsub_s("fsub.s", "RD = float(RA) - float(RB);"),
    fmul_s("fmul.s", "RD = float(RA) * float(RB);"),
    fdiv_s("fdiv.s", "RD = float(RA) / float(RB);"),
    fsqrt_s("fsqrt.s", "RD = sqrt(float(RA));"),
    //fsqnj_s(""),
    //fsqrtn_s(""),
    //fsqnjx_s(""),
    fmin_s("fmin.s", "if (float(RA) < float(RB)) RD = float(RA); else RD = float(RB);"),
    fmax_s("fmax.s", "if (float(RA) > float(RB)) RD = float(RA); else RD = float(RB);"),
    
    feq_s("feq.s", "if(float(RA) == float(RB)) RD = 1; else RD = 0;"),
    flt_s("flt.s", "if(float(RA) < float(RB)) RD = 1; else RD = 0;"),
    fle_s("fle.s", "if(float(RA) <= float(RB)) RD = 1; else RD = 0;"),
    
    fmv_w_x("fmv.w.x", "RD = float(RA);"),
    
    fmadd_s("fmadd.s", "RD = (float(RA) * float(RB)) + float(RC);"),
    fmsub_s("fmsub.s", "RD = (float(RA) * float(RB)) - float(RC);"),
    fnmadd_s("fnmadd.s", "RD = -(float(RA) * float(RB)) + float(RC);"),
    fnmsub_s("fnmsub.s", "RD = -(float(RA) * float(RB)) - float(RC);"),
    
    //flw(""),
    
    //fsw(""),
    
    
    defaultCode("RD = RA;");
    
    private final String name;
    private final String pseudocode;

    private RiscvPseudocode(String pseudocode) {
        this.pseudocode = pseudocode;
        this.name = name();
    }

    private RiscvPseudocode(String explicitname, String pseudocode) {
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

    public static InstructionPseudocode getDefault() {
        return RiscvPseudocode.defaultCode;
    }

}
