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
    
    add("RD = RS1 + RS2;"),
    sub("RD = RS1 - RS2;"),
    slt("if(signed(RS1) < signed(RS2)) RD = 1; else RD = 0;"),
    sltu("if(unsigned(RS1) < unsigned(RS2)) RD = 1; else RD = 0;"),
    xor("RD = RS1 ^ RS2;"),
    or("RD = RS1 | RS2;"),
    and("RD = RS1 & RS2;"),
    sll("RD = RS1 << RS2[4:0];"),
    srl("RD = RS1 >> RS2[4:0]"),
    sra("RD = RS1 >>> RS2[4:0]"),
    

    addi("RD = RS1 + sext(IMM);"),
    slti("if(signed(RS1) < signed(IMM)) RD = 1; else RD = 0;"),
    sltiu("if(unsgned(RS1) < unsigned(IMM)) RD = 1; else RD = 0;"),
    xori("RD = RS1 ^ IMM;"),
    ori("RD = RS1 | IMM;"),
    andi("RD = RS1 & IMM;"),
    slli("RD = RS << IMM[4:0];"),
    srli("RD = RS >> IMM[4:0];"),
    srai("RD = RS >>> IMM[4:0];"),
    //lb(""),
    //lh(""),
    //lw(""),
    //lnu(""),
    //lhu(""),
    jalr("RD = $pc + 4; $pc = RS1 + sext(IMM);"),
    
    //sb(""),
    //sh(""),
    //sw(""),
    
    //lui(""),
    auipic("RD = $pc + (IMM << 12);"),
    
    beq("if(RS1 == RS2) $pc = $pc + sext(IMM);"),
    bne("if(RS1 != RS2) $pc = $pc + sext(IMM);"),
    blt("if(RS1 < RS2) $pc = $pc + sext(IMM);"),
    bge("if(RS1 > RS2) $pc = $pc + sext(IMM);"),
    bltu("if(unsigned(RS1) < unsigned(RS2)) $pc = $pc + sext(IMM);"),
    bgeu("if(unsigned(RS1) > unsigned(RS2)) $pc = $pc + sext(IMM);"),
    
    jal("RD = $pc + 4; $pc = $pc + sext(IMM);"),
    
    mul("RD = lsw(RS1 * RS2);"),
    mulh("RD = msw(RS1 * RS2)"),
    mulhsu("RD = msw(signed(RS1) * unsigned(RS2));"),
    mulhu("RD = msw(unsigned(RS1) * unsigned(RS2));"),
    div("RD = signed(RS1) / signed(RS2);"), // Nota: na especificação é referido que o div e o rem podem ser juntos numa só operação, deve-se ter isto em conta ?
    divu("RD = unsigned(RS1) / unsigned(RS2);"),
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
    
    fadd_s("fadd.s", "RD = float(RS1) + float(RS2);"),
    fsub_s("fsub.s", "RD = float(RS1) - float(RS2);"),
    fmul_s("fmul.s", "RD = float(RS1) * float(RS2);"),
    fdiv_s("fdiv.s", "RD = float(RS1) / float(RS2);"),
    fsqrt_s("fsqrt.s", "RD = sqrt(float(RS1));"),
    //fsqnj_s(""),
    //fsqrtn_s(""),
    //fsqnjx_s(""),
    fmin_s("fmin.s", "if (float(RS1) < float(RS2)) RD = float(RS1); else RD = float(RS2);"),
    fmax_s("fmax.s", "if (float(RS1) > float(RS2)) RD = float(RS1); else RD = float(RS2);"),
    
    feq_s("feq.s", "if(float(RS1) == float(RS2)) RD = 1; else RD = 0;"),
    flt_s("flt.s", "if(float(RS1) < float(RS2)) RD = 1; else RD = 0;"),
    fle_s("fle.s", "if(float(RS1) <= float(RS2)) RD = 1; else RD = 0;"),
    
    fmv_w_x("fmv.w.x", "RD = float(RS1);"),
    
    fmadd_s("fmadd.s", "RD = (float(RS1) * float(RS2)) + float(RS3);"),
    fmsub_s("fmsub.s", "RD = (float(RS1) * float(RS2)) - float(RS3);"),
    fnmadd_s("fnmadd.s", "RD = -(float(RS1) * float(RS2)) + float(RS3);"),
    fnmsub_s("fnmsub.s", "RD = -(float(RS1) * float(RS2)) - float(RS3);"),
    
    //flw(""),
    
    //fsw(""),
    
    
    defaultCode("RD = RS1;");
    
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
