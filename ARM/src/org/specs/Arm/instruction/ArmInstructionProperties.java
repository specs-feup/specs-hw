package org.specs.Arm.instruction;

import static org.specs.Arm.parsing.ArmAsmFieldType.*;
import static pt.up.fe.specs.binarytranslation.instruction.InstructionType.*;

import java.util.Arrays;
import java.util.List;

import org.specs.Arm.parsing.ArmAsmFieldType;
import org.specs.Arm.parsing.ArmIsaParser;

import pt.up.fe.specs.binarytranslation.instruction.InstructionExpression;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.parsing.IsaParser;

public enum ArmInstructionProperties implements InstructionProperties {

    // name, binary code, latency, delay, subtype
    // within ISA (i.e. instruction binary format), generic type

    // Annotations of chaper and page numbers are relative to the ARMv8 reference manual:
    // https://static.docs.arm.com/ddi0487/ea/DDI0487E_a_armv8_arm.pdf

    // DPI_PCREL (C4-253)
    adr(0x1000_0000, 1, 1, DPI_PCREL, G_OTHER),
    adrp(0x9000_0000, 1, 1, DPI_PCREL, G_OTHER),

    // DPI_ADDSUBIMM (C4-253 to C4-254)
    add(0x1100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    adds(0x3100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    sub(0x5100_0000, 1, 1, DPI_ADDSUBIMM, G_SUB),
    subs(0x7100_0000, 1, 1, DPI_ADDSUBIMM, G_SUB),

    // DPI_ADDSUBIMM_TAGS (C4-254)
    addg(0x9180_0000, 1, 1, DPI_ADDSUBIMM_TAGS, G_ADD),
    subg(0xD180_0000, 1, 1, DPI_ADDSUBIMM_TAGS, G_SUB),

    // LOGICAL (C4-255)
    and(0x1200_0000, 1, 1, LOGICAL, G_LOGICAL),
    orr(0x3200_0000, 1, 1, LOGICAL, G_LOGICAL),
    eor(0x5200_0000, 1, 1, LOGICAL, G_LOGICAL),
    ands(0x7200_0000, 1, 1, LOGICAL, G_LOGICAL),

    // MOVEW (C4-255)
    movn(0x1280_0000, 1, 1, MOVEW, G_OTHER),
    movz(0x5280_0000, 1, 1, MOVEW, G_OTHER),
    movk(0x7280_0000, 1, 1, MOVEW, G_OTHER),

    // BITFIELD (C4-256)
    sbfm(0x1300_0000, 1, 1, BITFIELD, G_OTHER),
    bfm(0x3300_0000, 1, 1, BITFIELD, G_OTHER),
    ubfm(0x5300_0000, 1, 1, BITFIELD, G_OTHER),

    // EXTRACT (C4-256)
    extr(0x1380_0000, 1, 1, EXTRACT, G_OTHER),

    // CONDITIONALBRANCH (C4-257)
    bcond("b", 0x5400_0000, 1, 1, CONDITIONALBRANCH, G_JUMP, G_CJUMP, G_RJUMP),

    // EXCEPTION (C4-258)
    svc(0xD400_0001, 1, 1, EXCEPTION, G_OTHER),
    hvc(0xD400_0002, 1, 1, EXCEPTION, G_OTHER),
    smc(0xD400_0003, 1, 1, EXCEPTION, G_OTHER),
    brk(0xD420_0000, 1, 1, EXCEPTION, G_OTHER),
    hlt(0xD440_0000, 1, 1, EXCEPTION, G_OTHER),
    dcps1(0xD4A0_0001, 1, 1, EXCEPTION, G_OTHER),
    dcps2(0xD4A0_0002, 1, 1, EXCEPTION, G_OTHER),
    dcps3(0xD4A0_0003, 1, 1, EXCEPTION, G_OTHER),

    // UNCONDITIONAL BRANCH (REGISTER) (C4-262 to C4-264)
    br(0xD61F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    braaz(0xD61F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    braa(0xD71F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    brabz(0xD61F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    brab(0xD71F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blraaz(0xD63F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blraa(0xD73F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blrabz(0xD63F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blrab(0xD73F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blr(0xD63F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    ret(0xD65F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    retaa(0xD65F_0BFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    retab(0xD65F_0FFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    eret(0xD69F_03E0, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    eretaa(0xD69F_0EFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    eretab(0xD69F_0FFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),

    // UNCONDITIONAL BRANCH (IMMEDIATE) (C4-264 to C4-265)
    b(0x1400_0000, 1, 1, UCONDITIONALBRANCH_IMM, G_JUMP, G_UJUMP, G_IJUMP),
    bl(0x9400_0000, 1, 1, UCONDITIONALBRANCH_IMM, G_JUMP, G_UJUMP, G_IJUMP),

    // COMPARE AND BRANCH (C4-265)
    cbz(0x3400_0000, 1, 1, COMPARE_AND_BRANCH_IMM, G_CMP, G_JUMP, G_IJUMP),
    cbnz(0x3500_0000, 1, 1, COMPARE_AND_BRANCH_IMM, G_CMP, G_JUMP, G_IJUMP),

    // TEST_AND_BRANCH (C4-265)
    tbz(0x3600_0000, 1, 1, TEST_AND_BRANCH, G_CMP, G_JUMP, G_IJUMP),
    tbnz(0x3700_0000, 1, 1, TEST_AND_BRANCH, G_CMP, G_JUMP, G_IJUMP),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_REG_LITERAL (C4-280)
    ldr_reg("ldr", 0x1800_0000, 1, 1, LOAD_REG_LITERAL_FMT1, G_MEMORY, G_LOAD),
    ldrsw_reg("ldrsw", 0x9800_0000, 1, 1, LOAD_REG_LITERAL_FMT2, G_MEMORY, G_LOAD),
    prfm(0xD800_0000, 1, 1, LOAD_REG_LITERAL_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR (LOAD_STORE_NOALLOC) (C4-280 to C4-281)
    stnp(0x2800_0000, 1, 1, LOAD_STORE_PAIR_NO_ALLOC, G_MEMORY, G_STORE),
    ldnp(0x2840_0000, 1, 1, LOAD_STORE_PAIR_NO_ALLOC, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_PAIR (LOAD_STORE_PAIR_POSTINDEX +
    // LOAD_STORE_PAIR_OFFSET + LOAD_STORE_PAIR_PREINDEX) (C4-281 to C4-282)
    stp(0x2880_0000, 1, 1, LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1, G_MEMORY, G_STORE),
    ldp(0x28C0_0000, 1, 1, LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2 (C4-282)
    stgp(0x6880_0000, 1, 1, LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2, G_MEMORY, G_STORE),
    ldpsw(0x68C0_0000, 1, 1, LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_PAIR_IMM_UNSCALED_FMT1 (C4-283 to C4-284)
    sturb(0x3800_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_STORE),
    ldurb(0x3840_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    sturh(0x7800_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_STORE),
    ldurh(0x7840_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    ldursw(0xB880_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    prfum(0xF880_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_UNSCALED_FMT2 (C4-283 to C4-284)
    ldursb32("ldursb", 0x38C0_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldursb64("ldursb", 0x3880_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldursh32("ldursh", 0x78C0_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldursh64("ldursh", 0x7880_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    stur32("stur", 0xB800_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_STORE),
    stur64("stur", 0xF800_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_STORE),
    ldur32("ldur", 0xB840_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldur64("ldur", 0xF840_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_UNSCALED_FMT3 (C4-283 to C4-284)
    stur_simd("stur", 0x3C00_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT3, G_MEMORY, G_STORE),
    ldur_simd("ldur", 0x3C40_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT3, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_PAIR_UNPRIV_FMT1 (C4-286)
    sttrb(0x3800_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_STORE),
    ldtrb(0x3840_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    sttrh(0x7800_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_STORE),
    ldtrh(0x7840_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    ldtrsw(0xB880_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_UNPRIV_FMT2 (C4-286)
    ldtrsb32("ldtrsb", 0x38C0_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldtrsb64("ldtrsb", 0x3880_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldtrsh32("ldtrsh", 0x78C0_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldtrsh64("ldtrsh", 0x7880_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    sttr32("sttr", 0xB800_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_STORE),
    sttr64("sttr", 0xF800_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_STORE),
    ldtr32("ldtr", 0xB840_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldtr64("ldtr", 0xF840_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_PAIR_IMM_PREPOST_FMT1 (C4-284 to C4-285 and C4-286 to C4-287)
    strb(0x3800_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT1, G_MEMORY, G_STORE),
    ldrb(0x3840_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT1, G_MEMORY, G_LOAD),
    strh(0x7800_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT1, G_MEMORY, G_STORE),
    ldrh(0x7840_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT1, G_MEMORY, G_LOAD),
    ldrsw(0xB880_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_PREPOST_FMT2 (C4-284 to C4-285 and C4-286 to C4-287)
    ldrsb32("ldrsb", 0x38C0_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT2, G_MEMORY, G_LOAD),
    ldrsb64("ldrsb", 0x3880_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT2, G_MEMORY, G_LOAD),
    ldrsh32("ldrsh", 0x78C0_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT2, G_MEMORY, G_LOAD),
    ldrsh64("ldrsh", 0x7880_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT2, G_MEMORY, G_LOAD),
    str32("str", 0xB800_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT2, G_MEMORY, G_STORE),
    str64("str", 0xF800_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT2, G_MEMORY, G_STORE),
    ldr32("ldr", 0xB840_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT2, G_MEMORY, G_LOAD),
    ldr64("ldr", 0xF840_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_PREPOST_FMT3 (C4-284 to C4-285 and C4-286 to C4-287)
    str_simd("str", 0x3C00_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT3, G_MEMORY, G_STORE),
    ldr_simd("ldr", 0x3C40_0400, 1, 1, LOAD_STORE_IMM_PREPOST_FMT3, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_REG_OFF_FMT1 (C4-295 to C4-297) /////////////////////////////
    strb_reg_off("strb", 0x3820_0800, 1, 1, LOAD_STORE_REG_OFF_FMT1, G_MEMORY, G_STORE),
    ldrb_reg_off("ldrb", 0x3860_0800, 1, 1, LOAD_STORE_REG_OFF_FMT1, G_MEMORY, G_LOAD),
    strh_reg_off("strh", 0x7820_0800, 1, 1, LOAD_STORE_REG_OFF_FMT1, G_MEMORY, G_STORE),
    ldrh_reg_off("ldrh", 0x7860_0800, 1, 1, LOAD_STORE_REG_OFF_FMT1, G_MEMORY, G_LOAD),
    // ldrsw
    // prfm

    // LOAD_STORE_REG_OFF_FMT2 (C4-295 to C4-297) /////////////////////////////
    ldrsb32_reg_off("ldrsb", 0x38E0_0800, 1, 1, LOAD_STORE_REG_OFF_FMT2, G_MEMORY, G_LOAD),
    ldrsb64_reg_off("ldrsb", 0x38A0_0800, 1, 1, LOAD_STORE_REG_OFF_FMT2, G_MEMORY, G_LOAD),
    ldrsh32_reg_off("ldrsh", 0x78E0_0800, 1, 1, LOAD_STORE_REG_OFF_FMT2, G_MEMORY, G_LOAD),
    ldrsh64_reg_off("ldrsh", 0x78A0_0800, 1, 1, LOAD_STORE_REG_OFF_FMT2, G_MEMORY, G_LOAD),
    str32_reg_off("str", 0xB820_0800, 1, 1, LOAD_STORE_REG_OFF_FMT2, G_MEMORY, G_STORE),
    str64_reg_off("str", 0xF820_0800, 1, 1, LOAD_STORE_REG_OFF_FMT2, G_MEMORY, G_STORE),
    ldr32_reg_off("ldr", 0xB860_0800, 1, 1, LOAD_STORE_REG_OFF_FMT2, G_MEMORY, G_LOAD),
    ldr64_reg_off("ldr", 0xF860_0800, 1, 1, LOAD_STORE_REG_OFF_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_REG_OFF_FMT3 (C4-295 to C4-297) /////////////////////////////
    str_simd_reg_off("str", 0x3C20_0800, 1, 1, LOAD_STORE_REG_OFF_FMT3, G_MEMORY, G_STORE),
    ldr_simd_reg_off("ldr", 0x3C60_0800, 1, 1, LOAD_STORE_REG_OFF_FMT3, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_REG_UIMM_FMT1 (C4-297) //////////////////////////////////////
    strb_uimm("strb", 0x3900_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT1, G_MEMORY, G_STORE),
    ldrb_uimm("ldrb", 0x3940_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT1, G_MEMORY, G_LOAD),
    strh_uimm("strh", 0x7900_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT1, G_MEMORY, G_STORE),
    ldrh_uimm("ldrh", 0x7940_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT1, G_MEMORY, G_LOAD),
    ldrsw_uimm("ldrsw", 0xB980_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT1, G_MEMORY, G_LOAD),
    prfm_uimm("prfm", 0xF980_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_REG_UIMM_FMT2 (C4-297) //////////////////////////////////////
    ldrsb32_uimm("ldrsb", 0x39C0_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT2, G_MEMORY, G_LOAD),
    ldrsb64_uimm("ldrsb", 0x3980_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT2, G_MEMORY, G_LOAD),
    ldrsh32_uimm("ldrsh", 0x79C0_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT2, G_MEMORY, G_LOAD),
    ldrsh64_uimm("ldrsh", 0x7980_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT2, G_MEMORY, G_LOAD),
    str32_uimm("str", 0xB900_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT2, G_MEMORY, G_STORE),
    str64_uimm("str", 0xF900_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT2, G_MEMORY, G_STORE),
    ldr32_uimm("ldr", 0xB940_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT2, G_MEMORY, G_LOAD),
    ldr64_uimm("ldr", 0xF940_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_REG_UIMM_FMT3 (C4-297) //////////////////////////////////////
    str_simd_uimm("str", 0x3D00_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT3, G_MEMORY, G_STORE),
    ldr_simd_uimm("ldr", 0x3D40_0000, 1, 1, LOAD_STORE_REG_UIMM_FMT3, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // DPR_TWOSOURCE (C4-299) /////////////////////////////////////////////////
    udiv(0x1AC0_0800, 1, 1, DPR_TWOSOURCE, G_OTHER),
    sdiv(0x1AC0_0C00, 1, 1, DPR_TWOSOURCE, G_OTHER),
    lslv(0x1AC0_2000, 1, 1, DPR_TWOSOURCE, G_LOGICAL),
    lsrv(0x1AC0_2400, 1, 1, DPR_TWOSOURCE, G_LOGICAL),
    asrv(0x1AC0_2800, 1, 1, DPR_TWOSOURCE, G_LOGICAL),
    rorv(0x1AC0_2C00, 1, 1, DPR_TWOSOURCE, G_LOGICAL),
    crc32b(0x1AC0_4000, 1, 1, DPR_TWOSOURCE, G_OTHER),
    crc32h(0x1AC0_4400, 1, 1, DPR_TWOSOURCE, G_OTHER),
    crc32w(0x1AC0_4800, 1, 1, DPR_TWOSOURCE, G_OTHER),
    crc32x(0x9AC0_4C00, 1, 1, DPR_TWOSOURCE, G_OTHER),
    crc32cb(0x1AC0_5000, 1, 1, DPR_TWOSOURCE, G_OTHER),
    crc32ch(0x1AC0_5400, 1, 1, DPR_TWOSOURCE, G_OTHER),
    crc32cw(0x1AC0_5800, 1, 1, DPR_TWOSOURCE, G_OTHER),
    crc32cx(0x9AC0_5C00, 1, 1, DPR_TWOSOURCE, G_OTHER),
    subp(0x9AC0_0000, 1, 1, DPR_TWOSOURCE, G_SUB, G_OTHER),
    irg(0x9AC0_1000, 1, 1, DPR_TWOSOURCE, G_OTHER),
    gmi(0x9AC0_1400, 1, 1, DPR_TWOSOURCE, G_OTHER),
    pacga(0x9AC0_3000, 1, 1, DPR_TWOSOURCE, G_OTHER),
    subps(0xBAC0_0000, 1, 1, DPR_TWOSOURCE, G_SUB, G_OTHER),

    // DPR_ONESOURCE (C4-301) /////////////////////////////////////////////////
    rbit(0x5AC0_0000, 1, 1, DPR_ONESOURCE, G_UNARY, G_LOGICAL),
    rev16(0x5AC0_0400, 1, 1, DPR_ONESOURCE, G_UNARY, G_LOGICAL),
    rev(0x5AC0_0800, 1, 1, DPR_ONESOURCE, G_UNARY, G_LOGICAL),
    clz(0x5AC0_1000, 1, 1, DPR_ONESOURCE, G_UNARY, G_LOGICAL),
    cls(0x5AC0_1400, 1, 1, DPR_ONESOURCE, G_UNARY, G_LOGICAL),
    rev32(0xDAC0_0800, 1, 1, DPR_ONESOURCE, G_UNARY, G_LOGICAL), // TODO: is this an alias?

    /*
    pacia(0xDAC1_0000, 1, 1, DPR_ONESOURCE, G_UNARY, G_LOGICAL),
    paciza(0xDAC1_2000, 1, 1, DPR_ONESOURCE, G_UNARY, G_LOGICAL),
    pacia1716(0xD503_211F, 1, 1, DPR_ONESOURCE, G_UNARY, G_LOGICAL),
    // TODO all the "pacias", all the "autias", all the "autdb", and all the "xpacds"
    */

    // LOGICAL_SHIFT_REG (C4-303) /////////////////////////////////////////////
    and_shift_reg("and", 0x0A00_0000, 1, 1, LOGICAL_SHIFT_REG, G_LOGICAL),
    bic_shift_reg("bic", 0x0A20_0000, 1, 1, LOGICAL_SHIFT_REG, G_LOGICAL),
    orr_shift_reg("orr", 0x2A00_0000, 1, 1, LOGICAL_SHIFT_REG, G_LOGICAL),
    orn_shift_reg("orn", 0x2A20_0000, 1, 1, LOGICAL_SHIFT_REG, G_LOGICAL),
    eor_shift_reg("eor", 0x4A00_0000, 1, 1, LOGICAL_SHIFT_REG, G_LOGICAL),
    eon_shift_reg("eon", 0x4A20_0000, 1, 1, LOGICAL_SHIFT_REG, G_LOGICAL),
    ands_shift_reg("ands", 0x6A00_0000, 1, 1, LOGICAL_SHIFT_REG, G_LOGICAL),

    // ADD_SUB_SHIFT_REG (C4-303) /////////////////////////////////////////////
    add_shift_reg("add", 0x0B00_0000, 1, 1, ADD_SUB_SHIFT_REG, G_ADD),
    adds_shift_reg("adds", 0x2B00_0000, 1, 1, ADD_SUB_SHIFT_REG, G_ADD),
    sub_shift_reg("sub", 0x4B00_0000, 1, 1, ADD_SUB_SHIFT_REG, G_SUB),
    subs_shift_reg("subs", 0x6B00_0000, 1, 1, ADD_SUB_SHIFT_REG, G_SUB),

    // ADD_SUB_EXT_REG (C4-304) ///////////////////////////////////////////////
    add_ext_reg("add", 0x0B20_0000, 1, 1, ADD_SUB_EXT_REG, G_ADD),
    adds_ext_reg("adds", 0x2B20_0000, 1, 1, ADD_SUB_EXT_REG, G_ADD),
    sub_ext_reg("sub", 0x4B20_0000, 1, 1, ADD_SUB_EXT_REG, G_SUB),
    subs_ext_reg("subs", 0x6B20_0000, 1, 1, ADD_SUB_EXT_REG, G_SUB),

    // ADD_SUB_CARRY (C4-305) /////////////////////////////////////////////////
    adc(0x1A00_0000, 1, 1, ADD_SUB_CARRY, G_ADD),
    adcs(0x3A00_0000, 1, 1, ADD_SUB_CARRY, G_ADD),
    sbc(0x5A00_0000, 1, 1, ADD_SUB_CARRY, G_SUB),
    sbcs(0x7A00_0000, 1, 1, ADD_SUB_CARRY, G_SUB),

    // CONDITIONAL_CMP_REG (C4-306) ///////////////////////////////////////////
    ccmn(0x3A40_0000, 1, 1, CONDITIONAL_CMP_REG, G_CMP),
    ccmp(0x7A40_0000, 1, 1, CONDITIONAL_CMP_REG, G_CMP),

    // CONDITIONAL_CMP_IMM (C4-306) ///////////////////////////////////////////
    ccmn_imm("ccmn", 0x3A40_0800, 1, 1, CONDITIONAL_CMP_IMM, G_CMP),
    ccmp_imm("ccmp", 0x7A40_0800, 1, 1, CONDITIONAL_CMP_IMM, G_CMP),

    // CONDITIONAL_SELECT (C4-307) ///////////////////////////////////////////
    csel(0x1A80_0000, 1, 1, CONDITIONAL_SELECT, G_OTHER),
    csinc(0x1A80_0400, 1, 1, CONDITIONAL_SELECT, G_OTHER),
    csinv(0x5A80_0000, 1, 1, CONDITIONAL_SELECT, G_OTHER),
    csneg(0x5A80_0400, 1, 1, CONDITIONAL_SELECT, G_OTHER),

    // DPR_THREESOURCE (C4-308) /////////////////////////////////////////////////
    madd(0x1B00_0000, 1, 1, DPR_THREESOURCE, G_ADD),
    msub(0x1B00_8000, 1, 1, DPR_THREESOURCE, G_SUB),
    smaddl(0x9B20_0000, 1, 1, DPR_THREESOURCE, G_ADD),
    smsubl(0x9B20_8000, 1, 1, DPR_THREESOURCE, G_SUB),
    umaddl(0x9BA0_0000, 1, 1, DPR_THREESOURCE, G_ADD),
    umsubl(0x9BA0_8000, 1, 1, DPR_THREESOURCE, G_SUB),
    umulh(0x9BC0_0000, 1, 1, DPR_THREESOURCE, G_MUL),

    unknown(0x000000000, 1, 1, UNDEFINED, G_UNKN);

    ///////////////////////////////////////////////////////////////////////////

    /*
     * Instruction property fields
     */
    private String instructionName;
    private String enumName;
    private final int opcode; // 32 bit instruction code without operands
    private final int reducedopcode; // only the bits that matter, built after parsing the fields
    private final int latency;
    private final int delay;
    private final ArmAsmFieldType codetype;
    private final List<InstructionType> genericType;
    private final AsmFieldData fieldData; // decoded fields of this instruction
    private final IsaParser parser = new ArmIsaParser();

    /*
     *  Helper constructor
     */
    private ArmInstructionProperties(int opcode, int latency,
            int delay, ArmAsmFieldType mbtype, List<InstructionType> tp) {
        this.instructionName = "";
        this.enumName = name();
        this.opcode = opcode;
        this.latency = latency;
        this.delay = delay;
        this.codetype = mbtype;
        this.genericType = tp;

        // use the parser to initialize private fields of instruction set itself
        this.fieldData = parser.parse(Integer.toHexString(opcode));
        this.reducedopcode = this.fieldData.getReducedOpcode();
    }

    /*
     * Constructor
     */
    private ArmInstructionProperties(int opcode, int latency,
            int delay, ArmAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, latency, delay, mbtype, Arrays.asList(tp));
        this.instructionName = name();
        // System.out.print(this.codetype.toString() + ": " + this.instructionName + "\n");
        // System.out.print(this.instructionName + ": " + Integer.toBinaryString(this.reducedopcode) + "\n");
    }

    /*
     * Helper constructor with explicit name for instruction
     */
    private ArmInstructionProperties(String explicitname, int opcode, int latency,
            int delay, ArmAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, latency, delay, mbtype, Arrays.asList(tp));
        this.instructionName = explicitname;
        // System.out.print(this.codetype.toString() + ": " + this.instructionName + "\n");
    }

    /*
     * helper method too look up the list
     */
    public int getLatency() {
        return this.latency;
    }

    /*
     * helper method too look up the list
     */
    public int getDelay() {
        return this.delay;
    }

    /*
     * helper method too get full opcode
     */
    public int getOpCode() {
        return this.opcode;
    }

    /*
     * helper method too get only the bits that matter
     */
    public int getReducedOpCode() {
        return this.reducedopcode;
    }

    /*
     * helper method too look up type in the list
     */
    public List<InstructionType> getGenericType() {
        return this.genericType;
    }

    /*
     * Private helper method too look up name the list
     */
    public String getName() {
        return this.instructionName;
    }

    /*
     * Returns name of enum (should be unique)
     */
    public String getEnumName() {
        return enumName;
    }

    /*
     * get code type of a particular instruction
     */
    public AsmFieldType getCodeType() {
        return this.codetype;
    }

    /*
     * Only used for Junit tests!
     */
    public AsmFieldData getFieldData() {
        return fieldData;
    }

    @Override
    public InstructionExpression getExpression() {
        // TODO Auto-generated method stub
        return null;
    }
}
