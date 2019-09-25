/*
 * Copyright 2010 SPeCS Research Group.
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

package org.specs.MicroBlaze;

import java.util.EnumSet;

import org.specs.MicroBlaze.isa.MbInstructionName;

/**
 * Several properties of MicroBlaze instructions.
 * 
 * @author Joao Bispo
 */
public interface InstructionProperties {
    // public class InstructionProperties {

    /**
     * Which Instructions are Branches
     */
    // public final static EnumSet<MbInstructionName> JUMP_INSTRUCTIONS = EnumSet.of(
    EnumSet<MbInstructionName> JUMP_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.beq,
            MbInstructionName.beqd,
            MbInstructionName.beqi,
            MbInstructionName.beqid,
            MbInstructionName.bge,
            MbInstructionName.bged,
            MbInstructionName.bgei,
            MbInstructionName.bgeid,
            MbInstructionName.bgt,
            MbInstructionName.bgtd,
            MbInstructionName.bgti,
            MbInstructionName.bgtid,
            MbInstructionName.ble,
            MbInstructionName.bled,
            MbInstructionName.blei,
            MbInstructionName.bleid,
            MbInstructionName.blt,
            MbInstructionName.bltd,
            MbInstructionName.blti,
            MbInstructionName.bltid,
            MbInstructionName.bne,
            MbInstructionName.bned,
            MbInstructionName.bnei,
            MbInstructionName.bneid,
            MbInstructionName.br,
            MbInstructionName.bra,
            MbInstructionName.brd,
            MbInstructionName.brad,
            MbInstructionName.brld,
            MbInstructionName.brald,
            MbInstructionName.bri,
            MbInstructionName.brai,
            MbInstructionName.brid,
            MbInstructionName.braid,
            MbInstructionName.brlid,
            MbInstructionName.bralid,
            MbInstructionName.brk,
            MbInstructionName.brki,
            MbInstructionName.rtbd,
            MbInstructionName.rtid,
            MbInstructionName.rted,
            MbInstructionName.rtsd);

    /**
     * Which unconditional branch instructions perform linking to rD
     */
    /*
    public final static EnumSet<MbInstructionName> LINKING_INSTRUCTIONS = EnumSet.of(
           MbInstructionName.brld,
           MbInstructionName.brald,
           MbInstructionName.brlid,
           MbInstructionName.bralid);
    */
    /**
     * Which instructions have delay slot
     */
    // public final static EnumSet<MbInstructionName> INSTRUCTIONS_WITH_DELAY_SLOT = EnumSet.of(
    EnumSet<MbInstructionName> INSTRUCTIONS_WITH_DELAY_SLOT = EnumSet.of(
            MbInstructionName.beqd,
            MbInstructionName.beqid,
            MbInstructionName.bged,
            MbInstructionName.bgeid,
            MbInstructionName.bgtd,
            MbInstructionName.bgtid,
            MbInstructionName.bled,
            MbInstructionName.bleid,
            MbInstructionName.bltd,
            MbInstructionName.bltid,
            MbInstructionName.bned,
            MbInstructionName.bneid,
            MbInstructionName.brd,
            MbInstructionName.brad,
            MbInstructionName.brld,
            MbInstructionName.brald,
            MbInstructionName.brid,
            MbInstructionName.braid,
            MbInstructionName.brlid,
            MbInstructionName.bralid,
            MbInstructionName.rtbd,
            MbInstructionName.rtid,
            MbInstructionName.rted,
            MbInstructionName.rtsd);

    /**
     * Which instructions are loads
     */
    public final static EnumSet<MbInstructionName> LOAD_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.lbu,
            MbInstructionName.lbur,
            MbInstructionName.lbui,
            MbInstructionName.lhu,
            MbInstructionName.lhur,
            MbInstructionName.lhui,
            MbInstructionName.lw,
            MbInstructionName.lwr,
            MbInstructionName.lwi);

    /**
     * Which instructions are stores
     */
    // public final static EnumSet<MbInstructionName> STORE_INSTRUCTIONS = EnumSet.of(
    EnumSet<MbInstructionName> STORE_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.sb,
            MbInstructionName.sbr,
            MbInstructionName.sbi,
            MbInstructionName.sh,
            MbInstructionName.shr,
            MbInstructionName.shi,
            MbInstructionName.sw,
            MbInstructionName.swr,
            MbInstructionName.swi);

    /**
     * Which instructions have side-effects
     */
    /*
    public final static EnumSet<MbInstructionName> INSTRUCTIONS_WITH_SIDE_EFFECTS;
    
    static {
      INSTRUCTIONS_WITH_SIDE_EFFECTS = EnumSet.noneOf(MbInstructionName.add.getDeclaringClass());
      INSTRUCTIONS_WITH_SIDE_EFFECTS.addAll(STORE_INSTRUCTIONS);
    }
    * 
    */
    /**
     * Which instructions represent the addition operation
     */
    // public final static EnumSet<MbInstructionName> ADD_INSTRUCTIONS = EnumSet.of(
    EnumSet<MbInstructionName> ADD_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.add,
            MbInstructionName.addc,
            MbInstructionName.addi,
            MbInstructionName.addic,
            MbInstructionName.addik,
            MbInstructionName.addikc,
            MbInstructionName.addk,
            MbInstructionName.addkc);

    /**
     * Which instructions represent the reverse subtraction operation
     */
    // public final static EnumSet<MbInstructionName> RSUB_INSTRUCTIONS = EnumSet.of(
    EnumSet<MbInstructionName> RSUB_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.rsub,
            MbInstructionName.rsubc,
            MbInstructionName.rsubi,
            MbInstructionName.rsubic,
            MbInstructionName.rsubik,
            MbInstructionName.rsubikc,
            MbInstructionName.rsubk,
            MbInstructionName.rsubkc);

    /**
     * Which instructions represent binary logical operations, including comparators and shifters
     */
    // public final static EnumSet<MbInstructionName> BINARY_LOGICAL_INSTRUCTIONS = EnumSet.of(
    EnumSet<MbInstructionName> BINARY_LOGICAL_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.and,
            MbInstructionName.andi,
            MbInstructionName.andn,
            MbInstructionName.andni,
            MbInstructionName.or,
            MbInstructionName.ori,
            MbInstructionName.xor,
            MbInstructionName.xori,
            MbInstructionName.cmp,
            MbInstructionName.cmpu,
            MbInstructionName.bsll,
            MbInstructionName.bslli,
            MbInstructionName.bsra,
            MbInstructionName.bsrai,
            MbInstructionName.bsrl,
            MbInstructionName.bsrli);

    /**
     * Which instructions represent unary logical operations, including shifters.
     */
    // public final static EnumSet<MbInstructionName> UNARY_LOGICAL_INSTRUCTIONS = EnumSet.of(
    EnumSet<MbInstructionName> UNARY_LOGICAL_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.sra,
            MbInstructionName.srl,
            MbInstructionName.src);

    /**
     * Which instructions strictly represent shifts.
     */
    EnumSet<MbInstructionName> SHIFT_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.bsll,
            MbInstructionName.bslli,
            MbInstructionName.bsra,
            MbInstructionName.bsrai,
            MbInstructionName.bsrl,
            MbInstructionName.bsrli,
            MbInstructionName.sra,
            MbInstructionName.srl,
            MbInstructionName.src);

    /**
     * Which instructions represent single shifts.
     */
    EnumSet<MbInstructionName> SHIFT_SINGLE_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.sra,
            MbInstructionName.srl,
            MbInstructionName.src);

    /**
     * Which instructions represent shifts by an amount.
     */
    EnumSet<MbInstructionName> SHIFT_BARREL_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.bsll,
            MbInstructionName.bslli,
            MbInstructionName.bsra,
            MbInstructionName.bsrai,
            MbInstructionName.bsrl,
            MbInstructionName.bsrli);

    /**
     * Which instructions strictly represent comparators.
     */
    EnumSet<MbInstructionName> COMPARATOR_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.cmp,
            MbInstructionName.cmpu,
            MbInstructionName.pcmpbf,
            MbInstructionName.pcmpeq,
            MbInstructionName.pcmpne);

    /**
     * Which instructions represent pattern compare.
     */
    EnumSet<MbInstructionName> COMPARATOR_PATTERN_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.pcmpbf,
            MbInstructionName.pcmpeq,
            MbInstructionName.pcmpne);

    /**
     * Which instructions represent integer compare.
     */
    EnumSet<MbInstructionName> COMPARATOR_INTEGER_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.cmp,
            MbInstructionName.cmpu);

    /**
     * Which instructions strictly represent boolean operations.
     *
     * <p>
     * Ex.: and, or, xor
     */
    EnumSet<MbInstructionName> BOOLEAN_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.and,
            MbInstructionName.andi,
            MbInstructionName.andn,
            MbInstructionName.andni,
            MbInstructionName.or,
            MbInstructionName.ori,
            MbInstructionName.xor,
            MbInstructionName.xori);

    /**
     * Which instructions strictly represent boolean operations.
     */
    EnumSet<MbInstructionName> SIGN_EXTENSION_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.sext8,
            MbInstructionName.sext16);

    /**
     * Which instructions strictly represent boolean operations.
     */
    EnumSet<MbInstructionName> MULTIPLICATION_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.mul,
            MbInstructionName.mulh,
            MbInstructionName.mulhsu,
            MbInstructionName.mulhu,
            MbInstructionName.muli);

    EnumSet<MbInstructionName> MULTIPLICATION_SIGNED_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.mul,
            MbInstructionName.mulh,
            MbInstructionName.muli);

    EnumSet<MbInstructionName> MULTIPLICATION_UNSIGNED_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.mulhsu,
            MbInstructionName.mulhu);

    /**
     * Which instructions strictly represent boolean operations.
     */
    EnumSet<MbInstructionName> DIVISION_INSTRUCTIONS = EnumSet.of(
            MbInstructionName.idiv,
            MbInstructionName.idivu);

    /**
     * Which instructions represent conditional branches
     */
    // public final static EnumSet<MbInstructionName> CONDITIONAL_BRANCHES = EnumSet.of(
    EnumSet<MbInstructionName> CONDITIONAL_BRANCHES = EnumSet.of(
            MbInstructionName.beq,
            MbInstructionName.beqd,
            MbInstructionName.beqi,
            MbInstructionName.beqid,
            MbInstructionName.bge,
            MbInstructionName.bged,
            MbInstructionName.bgei,
            MbInstructionName.bgeid,
            MbInstructionName.bgt,
            MbInstructionName.bgtd,
            MbInstructionName.bgti,
            MbInstructionName.bgtid,
            MbInstructionName.ble,
            MbInstructionName.bled,
            MbInstructionName.blei,
            MbInstructionName.bleid,
            MbInstructionName.blt,
            MbInstructionName.bltd,
            MbInstructionName.blti,
            MbInstructionName.bltid,
            MbInstructionName.bne,
            MbInstructionName.bned,
            MbInstructionName.bnei,
            MbInstructionName.bneid);
    /**
     * Which instructions represent conditional branches with immediates
     */
    EnumSet<MbInstructionName> CONDITIONAL_BRANCHES_IMM = EnumSet.of(
            MbInstructionName.beqi,
            MbInstructionName.beqid,
            MbInstructionName.bgei,
            MbInstructionName.bgeid,
            MbInstructionName.bgti,
            MbInstructionName.bgtid,
            MbInstructionName.blei,
            MbInstructionName.bleid,
            MbInstructionName.blti,
            MbInstructionName.bltid,
            MbInstructionName.bnei,
            MbInstructionName.bneid);

    /**
     * Which instructions represent unconditional branches
     */
    // public final static EnumSet<MbInstructionName> UNCONDITIONAL_BRANCHES = EnumSet.of(
    EnumSet<MbInstructionName> UNCONDITIONAL_BRANCHES = EnumSet.of(
            MbInstructionName.br,
            MbInstructionName.bra,
            MbInstructionName.brd,
            MbInstructionName.brad,
            MbInstructionName.brld,
            MbInstructionName.brald,
            MbInstructionName.bri,
            MbInstructionName.brai,
            MbInstructionName.brid,
            MbInstructionName.braid,
            MbInstructionName.brlid,
            MbInstructionName.bralid,
            MbInstructionName.brk,
            MbInstructionName.brki,
            MbInstructionName.rtbd,
            MbInstructionName.rtid,
            MbInstructionName.rted,
            MbInstructionName.rtsd);

    EnumSet<MbInstructionName> UNCONDITIONAL_JUMP_IMM = EnumSet.of(
            MbInstructionName.bri,
            MbInstructionName.brai,
            MbInstructionName.brid,
            MbInstructionName.braid,
            MbInstructionName.brlid,
            MbInstructionName.bralid);

    EnumSet<MbInstructionName> UNCONDITIONAL_JUMP_REG = EnumSet.of(
            MbInstructionName.br,
            MbInstructionName.bra,
            MbInstructionName.brd,
            MbInstructionName.brad,
            MbInstructionName.brld,
            MbInstructionName.brald);

    /**
     * Which instructions represent branch and link
     */
    // public final static EnumSet<MbInstructionName> BRANCH_AND_LINK = EnumSet.of(
    EnumSet<MbInstructionName> BRANCH_AND_LINK = EnumSet.of(
            MbInstructionName.brld,
            MbInstructionName.brald,
            MbInstructionName.brlid,
            MbInstructionName.bralid,
            MbInstructionName.brk,
            MbInstructionName.brki);

    EnumSet<MbInstructionName> RETURN = EnumSet.of(
            MbInstructionName.rtbd,
            MbInstructionName.rted,
            MbInstructionName.rtid,
            MbInstructionName.rtsd);

    EnumSet<MbInstructionName> FLOATING_POINT = EnumSet.of(
            MbInstructionName.fadd,
            MbInstructionName.fcmp_eq,
            MbInstructionName.fcmp_ge,
            MbInstructionName.fcmp_gt,
            MbInstructionName.fcmp_le,
            MbInstructionName.fcmp_lt,
            MbInstructionName.fcmp_ne,
            MbInstructionName.fcmp_un,
            MbInstructionName.fdiv,
            MbInstructionName.fint,
            MbInstructionName.flt,
            MbInstructionName.fmul,
            MbInstructionName.frsub,
            MbInstructionName.fsqrt);

    EnumSet<MbInstructionName> FLOAT_ARITH = EnumSet.of(
            MbInstructionName.fadd,
            MbInstructionName.fdiv,
            MbInstructionName.fmul,
            MbInstructionName.frsub,
            MbInstructionName.fsqrt);

    EnumSet<MbInstructionName> FLOAT_COMP = EnumSet.of(
            MbInstructionName.fcmp_eq,
            MbInstructionName.fcmp_ge,
            MbInstructionName.fcmp_gt,
            MbInstructionName.fcmp_le,
            MbInstructionName.fcmp_lt,
            MbInstructionName.fcmp_ne,
            MbInstructionName.fcmp_un);

    EnumSet<MbInstructionName> CONVERSION = EnumSet.of(
            MbInstructionName.flt,
            MbInstructionName.fint);

    EnumSet<MbInstructionName> TYPE_B = EnumSet.of(
            MbInstructionName.addi,
            MbInstructionName.addic,
            MbInstructionName.addik,
            MbInstructionName.addikc,
            MbInstructionName.rsubi,
            MbInstructionName.rsubic,
            MbInstructionName.rsubik,
            MbInstructionName.rsubikc,
            MbInstructionName.muli,
            MbInstructionName.ori,
            MbInstructionName.andi,
            MbInstructionName.xori,
            MbInstructionName.andni,
            MbInstructionName.bri,
            MbInstructionName.brid,
            MbInstructionName.brlid,
            MbInstructionName.brai,
            MbInstructionName.braid,
            MbInstructionName.bralid,
            MbInstructionName.brki,
            MbInstructionName.beqi,
            MbInstructionName.bnei,
            MbInstructionName.blti,
            MbInstructionName.blei,
            MbInstructionName.bgti,
            MbInstructionName.bgei,
            MbInstructionName.beqid,
            MbInstructionName.bneid,
            MbInstructionName.bltid,
            MbInstructionName.bleid,
            MbInstructionName.bgtid,
            MbInstructionName.bgeid,
            MbInstructionName.lbui,
            MbInstructionName.lhui,
            MbInstructionName.lwi,
            MbInstructionName.sbi,
            MbInstructionName.shi,
            MbInstructionName.swi);

}
