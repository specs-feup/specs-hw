package org.specs.Arm.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.Arm.instruction.ArmInstruction;
import org.specs.Arm.instruction.ArmInstructionData;
import org.specs.Arm.parsing.ArmAsmFieldData;
import org.specs.Arm.parsing.ArmAsmFieldType;

public class ArmInstructionInstatiationTest {

    /*
     * Instantiate one of every single instruction in the ISA as specified by the manual:
     * https://static.docs.arm.com/ddi0487/ea/DDI0487E_a_armv8_arm.pdf
     * To see if they get correctly identified into the proper mnemonic
     */

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // DPI_PCREL //////////////////////////////////////////////////////////
    public void test_Adr(ArmAsmFieldType fmt) {
        testDecode("adr", "adr", fmt, Integer.toHexString(0b000_10000_0000000000000000000_00000));
    }

    public void test_Adrp(ArmAsmFieldType fmt) {
        testDecode("adrp", "adrp", fmt, Integer.toHexString(0b100_10000_0000000000000000000_00000));
    }

    @Test
    public void test_DpiPCRel() {
        test_Adr(ArmAsmFieldType.DPI_PCREL);
        test_Adrp(ArmAsmFieldType.DPI_PCREL);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // DPI_ADDSUBIMM //////////////////////////////////////////////////////
    // 32 bit add
    public void test_AddImm32(ArmAsmFieldType fmt) {
        testDecode("add_imm_32", "add", fmt, Integer.toHexString(0b000_100010_0_000000000000_00000_00000));
    }

    // 32 bit add w/set flag
    public void test_AddsImm32(ArmAsmFieldType fmt) {
        testDecode("adds_imm_32", "adds", fmt, Integer.toHexString(0b001_100010_0_000000000000_00000_00000));
    }

    // 32 bit sub
    public void test_SubImm32(ArmAsmFieldType fmt) {
        testDecode("sub_imm_32", "sub", fmt, Integer.toHexString(0b010_100010_0_000000000000_00000_00000));
    }

    // 32 bit sub w/set flag
    public void test_SubsImm32(ArmAsmFieldType fmt) {
        testDecode("subs_imm_32", "subs", fmt, Integer.toHexString(0b011_100010_0_000000000000_00000_00000));
    }

    // 64 bit add
    public void test_AddImm64(ArmAsmFieldType fmt) {
        testDecode("add_imm_64", "add", fmt, Integer.toHexString(0b100_100010_0_000000000000_00000_00000));
    }

    // 64 bit add w/set flag
    public void test_AddsImm64(ArmAsmFieldType fmt) {
        testDecode("adds_imm_64", "adds", fmt, Integer.toHexString(0b101_100010_0_000000000000_00000_00000));
    }

    // 64 bit sub
    public void test_SubImm64(ArmAsmFieldType fmt) {
        testDecode("sub_imm_64", "sub", fmt, Integer.toHexString(0b110_100010_0_000000000000_00000_00000));
    }

    // 64 bit sub w/set flag
    public void test_SubsImm64(ArmAsmFieldType fmt) {
        testDecode("subs_imm_64", "subs", fmt, Integer.toHexString(0b111_100010_0_000000000000_00000_00000));
    }

    @Test
    public void test__DpiAddSubImm() {
        test_AddImm32(ArmAsmFieldType.DPI_ADDSUBIMM);
        test_AddsImm32(ArmAsmFieldType.DPI_ADDSUBIMM);
        test_SubImm32(ArmAsmFieldType.DPI_ADDSUBIMM);
        test_SubsImm32(ArmAsmFieldType.DPI_ADDSUBIMM);
        test_AddImm64(ArmAsmFieldType.DPI_ADDSUBIMM);
        test_AddsImm64(ArmAsmFieldType.DPI_ADDSUBIMM);
        test_SubImm64(ArmAsmFieldType.DPI_ADDSUBIMM);
        test_SubsImm64(ArmAsmFieldType.DPI_ADDSUBIMM);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // DPI_ADDSUBIMM_TAGS /////////////////////////////////////////////////
    // add with tags
    public void test_Addg(ArmAsmFieldType fmt) {
        testDecode("addg", "addg", fmt, Integer.toHexString(0b100_100011_0_000000000000_00000_00000));
    }

    // sub with tags
    public void test_Subg(ArmAsmFieldType fmt) {
        testDecode("subg", "subg", fmt, Integer.toHexString(0b110_100011_0_000000000000_00000_00000));
    }

    @Test
    public void test_DpiAddSumImmTags() {
        test_Addg(ArmAsmFieldType.DPI_ADDSUBIMM_TAGS);
        test_Subg(ArmAsmFieldType.DPI_ADDSUBIMM_TAGS);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICAL ////////////////////////////////////////////////////////////
    // logical and immediate 32 bits
    public void test_AndImm32(ArmAsmFieldType fmt) {
        testDecode("and_imm_32", "and", fmt, Integer.toHexString(0b000_100100_0_000000000000_00000_00000));
    }

    // logical orr immediate 32 bits
    public void test_OrrImm32(ArmAsmFieldType fmt) {
        testDecode("orr_imm_32", "orr", fmt, Integer.toHexString(0b001_100100_0_000000000000_00000_00000));
    }

    // logical eor immediate 32 bits
    public void test_EorImm32(ArmAsmFieldType fmt) {
        testDecode("eor_imm_32", "eor", fmt, Integer.toHexString(0b010_100100_0_000000000000_00000_00000));
    }

    // logical ands immediate 32 bits
    public void test_AndsImm32(ArmAsmFieldType fmt) {
        testDecode("ands_imm_32", "ands", fmt, Integer.toHexString(0b011_100100_0_000000000000_00000_00000));
    }

    // logical and immediate 64 bits
    public void test_AndImm64(ArmAsmFieldType fmt) {
        testDecode("and_imm_64", "and", fmt, Integer.toHexString(0b100_100100_0_000000000000_00000_00000));
    }

    // logical orr immediate 64 bits
    public void test_OrrImm64(ArmAsmFieldType fmt) {
        testDecode("orr_imm_64", "orr", fmt, Integer.toHexString(0b101_100100_0_000000000000_00000_00000));
    }

    // logical eor immediate 64 bits
    public void test_EorImm64(ArmAsmFieldType fmt) {
        testDecode("eor_imm_64", "eor", fmt, Integer.toHexString(0b110_100100_0_000000000000_00000_00000));
    }

    // logical ands immediate 64 bits
    public void test_AndsImm64(ArmAsmFieldType fmt) {
        testDecode("ands_imm_64", "ands", fmt, Integer.toHexString(0b111_100100_0_000000000000_00000_00000));
    }

    @Test
    public void test__LOGICAL() {
        test_AndImm32(ArmAsmFieldType.LOGICAL);
        test_OrrImm32(ArmAsmFieldType.LOGICAL);
        test_EorImm32(ArmAsmFieldType.LOGICAL);
        test_AndsImm32(ArmAsmFieldType.LOGICAL);
        test_AndImm64(ArmAsmFieldType.LOGICAL);
        test_OrrImm64(ArmAsmFieldType.LOGICAL);
        test_EorImm64(ArmAsmFieldType.LOGICAL);
        test_AndsImm64(ArmAsmFieldType.LOGICAL);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // MOVEW //////////////////////////////////////////////////////////////////
    // moven 32 bit
    public void test_Movn32(ArmAsmFieldType fmt) {
        testDecode("movn_imm_32", "movn", fmt,
                Integer.toHexString(0b000_100101_0_000000000000_00000_00000));
    }

    // movez 32 bit
    public void test_Movz32(ArmAsmFieldType fmt) {
        testDecode("movz_imm_32", "movz", fmt,
                Integer.toHexString(0b010_100101_0_000000000000_00000_00000));
    }

    // movek 32 bit
    public void test_Movk32(ArmAsmFieldType fmt) {
        testDecode("movk_imm_32", "movk", fmt,
                Integer.toHexString(0b011_100101_0_000000000000_00000_00000));
    }

    // moven 64 bit
    public void test_Movn64(ArmAsmFieldType fmt) {
        testDecode("movn_imm_64", "movn", fmt,
                Integer.toHexString(0b100_100101_0_000000000000_00000_00000));
    }

    // movez 64 bit
    public void test_Movz64(ArmAsmFieldType fmt) {
        testDecode("movz_imm_64", "movz", fmt,
                Integer.toHexString(0b110_100101_0_000000000000_00000_00000));
    }

    // movek 64 bit
    public void test_Movk64(ArmAsmFieldType fmt) {
        testDecode("movk_imm_64", "movk", fmt,
                Integer.toHexString(0b111_100101_0_000000000000_00000_00000));
    }

    public void test_Movew(ArmAsmFieldType fmt) {
        test_Movn32(ArmAsmFieldType.MOVEW);
        test_Movz32(ArmAsmFieldType.MOVEW);
        test_Movk32(ArmAsmFieldType.MOVEW);
        test_Movn64(ArmAsmFieldType.MOVEW);
        test_Movk64(ArmAsmFieldType.MOVEW);
        test_Movz64(ArmAsmFieldType.MOVEW);
        test_Movn32(ArmAsmFieldType.MOVEW);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // BITFIELD ///////////////////////////////////////////////////////////////
    // sbfm 32 bit
    public void test_Sbfm32(ArmAsmFieldType fmt) {
        testDecode("sbfm_imm_32", "sbfm", fmt,
                Integer.toHexString(0b000_100110_0_000000000000_00000_00000));
    }

    // bfm 32 bit
    public void test_Bfm32(ArmAsmFieldType fmt) {
        testDecode("bfm_imm_32", "bfm", fmt,
                Integer.toHexString(0b001_100110_0_000000000000_00000_00000));
    }

    // ubfm 32 bit
    public void test_Ubfm32(ArmAsmFieldType fmt) {
        testDecode("ubfm_imm_32", "ubfm", fmt,
                Integer.toHexString(0b010_100110_0_000000000000_00000_00000));
    }

    // sbfm 64 bit
    public void test_Sbfm64(ArmAsmFieldType fmt) {
        testDecode("sbfm_imm_64", "sbfm", fmt,
                Integer.toHexString(0b100_100110_0_000000000000_00000_00000));
    }

    // bfm 64 bit
    public void test_Bfm64(ArmAsmFieldType fmt) {
        testDecode("bfm_imm_64", "bfm", fmt,
                Integer.toHexString(0b101_100110_0_000000000000_00000_00000));
    }

    // ubfm 64 bit
    public void test_Ubfm64(ArmAsmFieldType fmt) {
        testDecode("ubfm_imm_64", "ubfm", fmt,
                Integer.toHexString(0b110_100110_0_000000000000_00000_00000));
    }

    public void test_Bitfield(ArmAsmFieldType fmt) {
        test_Sbfm32(ArmAsmFieldType.BITFIELD);
        test_Bfm32(ArmAsmFieldType.BITFIELD);
        test_Ubfm32(ArmAsmFieldType.BITFIELD);
        test_Sbfm64(ArmAsmFieldType.BITFIELD);
        test_Bfm64(ArmAsmFieldType.BITFIELD);
        test_Ubfm64(ArmAsmFieldType.BITFIELD);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // EXTRACT ////////////////////////////////////////////////////////////////
    // extr 32 bit
    public void test_Extr32(ArmAsmFieldType fmt) {
        testDecode("extr_imm_32", "extr", fmt,
                Integer.toHexString(0b000_100111_0_000000000000_00000_00000));
    }

    // extr 64 bit
    public void test_Extr64(ArmAsmFieldType fmt) {
        testDecode("extr_imm_64", "extr", fmt,
                Integer.toHexString(0b100_100111_1_000000000000_00000_00000));
    }

    @Test
    public void test_Extract() {
        test_Extr32(ArmAsmFieldType.EXTRACT);
        test_Extr64(ArmAsmFieldType.EXTRACT);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONDITIONALBRANCH //////////////////////////////////////////////////////
    // b.eq
    public void test_Beq(ArmAsmFieldType fmt) {
        testDecode("b.eq", "b.eq", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_0000));
    }

    // b.ne
    public void test_Bne(ArmAsmFieldType fmt) {
        testDecode("b.ne", "b.ne", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_0001));
    }

    // b.cs
    public void test_Bcs(ArmAsmFieldType fmt) {
        testDecode("b.cs", "b.cs", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_0010));
    }

    // b.cc
    public void test_Bcc(ArmAsmFieldType fmt) {
        testDecode("b.cc", "b.cc", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_0011));
    }

    // b.mn
    public void test_Bmn(ArmAsmFieldType fmt) {
        testDecode("b.mn", "b.mn", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_0100));
    }

    // b.pl
    public void test_Bpl(ArmAsmFieldType fmt) {
        testDecode("b.pl", "b.pl", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_0101));
    }

    // b.vs
    public void test_Bvs(ArmAsmFieldType fmt) {
        testDecode("b.vs", "b.vs", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_0110));
    }

    // b.vc
    public void test_Bvc(ArmAsmFieldType fmt) {
        testDecode("b.vc", "b.vc", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_0111));
    }

    // b.hi
    public void test_Bhi(ArmAsmFieldType fmt) {
        testDecode("b.hi", "b.hi", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_1000));
    }

    // b.ls
    public void test_Bls(ArmAsmFieldType fmt) {
        testDecode("b.ls", "b.ls", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_1001));
    }

    // b.ge
    public void test_Bge(ArmAsmFieldType fmt) {
        testDecode("b.ge", "b.ge", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_1010));
    }

    // b.lt
    public void test_Blt(ArmAsmFieldType fmt) {
        testDecode("b.lt", "b.lt", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_1011));
    }

    // b.gt
    public void test_Bgt(ArmAsmFieldType fmt) {
        testDecode("b.gt", "b.gt", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_1100));
    }

    // b.le
    public void test_Ble(ArmAsmFieldType fmt) {
        testDecode("b.le", "b.le", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_1101));
    }

    // b.al
    public void test_Bal(ArmAsmFieldType fmt) {
        testDecode("b.al", "b.al", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_1110));

        // test with positive relative target (+12) (0x0C)
        // testDecode("240", "b.al", "b.al", fmt,
        // Integer.toHexString(0b0101_0100_0000000000000001100_0_1110));

        // test with negative relative target (-36) (0x24)
        // testDecode("434", "b.al", "b.al", fmt,
        // Integer.toHexString(0b0101_0100_1111111111111011100_0_1110));
    }

    // b.nvb
    public void test_Bnvb(ArmAsmFieldType fmt) {
        testDecode("b.nvb", "b.nvb", fmt,
                Integer.toHexString(0b0101_0100_0000000000000000000_0_1111));
    }

    @Test
    public void test_ConditionalBranch() {
        var fmt = ArmAsmFieldType.CONDITIONALBRANCH;
        test_Beq(fmt);
        test_Bne(fmt);
        test_Bcs(fmt);
        test_Bcc(fmt);
        test_Bmn(fmt);
        test_Bpl(fmt);
        test_Bvs(fmt);
        test_Bvc(fmt);
        test_Bhi(fmt);
        test_Bls(fmt);
        test_Bge(fmt);
        test_Blt(fmt);
        test_Bgt(fmt);
        test_Ble(fmt);
        test_Bal(fmt);
        test_Bnvb(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // EXCEPTION //////////////////////////////////////////////////////////////
    // svc
    public void test_Svc(ArmAsmFieldType fmt) {
        testDecode("svc", "svc", fmt,
                Integer.toHexString(0b1101_0100_000_0000000000000000_000_01));
    }

    // hvc
    public void test_Hvc(ArmAsmFieldType fmt) {
        testDecode("hvc", "hvc", fmt,
                Integer.toHexString(0b1101_0100_000_0000000000000000_000_10));
    }

    // smc
    public void test_Smc(ArmAsmFieldType fmt) {
        testDecode("smc", "smc", fmt,
                Integer.toHexString(0b1101_0100_000_0000000000000000_000_11));
    }

    // brk
    public void test_Brk(ArmAsmFieldType fmt) {
        testDecode("brk", "brk", fmt,
                Integer.toHexString(0b1101_0100_001_0000000000000000_000_00));
    }

    // hlt
    public void test_Hlt(ArmAsmFieldType fmt) {
        testDecode("hlt", "hlt", fmt,
                Integer.toHexString(0b1101_0100_010_0000000000000000_000_00));
    }

    // dcps1
    public void test_Dcps1(ArmAsmFieldType fmt) {
        testDecode("dcps1", "dcps1", fmt,
                Integer.toHexString(0b1101_0100_101_0000000000000000_000_01));
    }

    // dcps2
    public void test_Dcps2(ArmAsmFieldType fmt) {
        testDecode("dcps2", "dcps2", fmt,
                Integer.toHexString(0b1101_0100_101_0000000000000000_000_10));
    }

    // dcps3
    public void test_Dcps3(ArmAsmFieldType fmt) {
        testDecode("dcps3", "dcps3", fmt,
                Integer.toHexString(0b1101_0100_101_0000000000000000_000_11));
    }

    @Test
    public void test_Exception() {
        var fmt = ArmAsmFieldType.EXCEPTION;
        test_Svc(fmt);
        test_Hvc(fmt);
        test_Smc(fmt);
        test_Brk(fmt);
        test_Hlt(fmt);
        test_Dcps1(fmt);
        test_Dcps2(fmt);
        test_Dcps3(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // SYSREGMOVE /////////////////////////////////////////////////////////////
    // mrs reg
    public void test_msr_reg(ArmAsmFieldType fmt) {
        testDecode("msr_reg", "msr", fmt,
                Integer.toHexString(0b1101_0101_0011_00000000000000000000));
    }

    // mrs
    public void test_msr(ArmAsmFieldType fmt) {
        testDecode("msr", "msr", fmt,
                Integer.toHexString(0b1101_0101_0001_00000000000000000000));
    }

    @Test
    public void test_Sysregmove() {
        var fmt = ArmAsmFieldType.SYSREGMOVE;
        test_msr_reg(fmt);
        test_msr(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // UNCONDITIONAL BRANCH (REGISTER) ////////////////////////////////////////
    // br
    public void test_Br(ArmAsmFieldType fmt) {
        testDecode("br", "br", fmt,
                Integer.toHexString(0b1101_011_0000_11111_000000_00000_00000));
    }

    // braaz
    public void test_Braaz(ArmAsmFieldType fmt) {
        testDecode("braaz", "braaz", fmt,
                Integer.toHexString(0b1101_011_0000_11111_000010_00000_00000));
    }

    // braa
    public void test_Braa(ArmAsmFieldType fmt) {
        testDecode("braa", "braa", fmt,
                Integer.toHexString(0b1101_011_1000_11111_000010_00000_00000));
    }

    // brabz
    public void test_Brabz(ArmAsmFieldType fmt) {
        testDecode("brabz", "brabz", fmt,
                Integer.toHexString(0b1101_011_0000_11111_000011_00000_00000));
    }

    // brab
    public void test_Brab(ArmAsmFieldType fmt) {
        testDecode("brab", "brab", fmt,
                Integer.toHexString(0b1101_011_1000_11111_000011_00000_00000));
    }

    // blraaz
    public void test_Blraaz(ArmAsmFieldType fmt) {
        testDecode("blraaz", "blraaz", fmt,
                Integer.toHexString(0b1101_011_0001_11111_000010_00000_00000));
    }

    // braa
    public void test_Blraa(ArmAsmFieldType fmt) {
        testDecode("blraa", "blraa", fmt,
                Integer.toHexString(0b1101_011_1001_11111_000010_00000_00000));
    }

    // blrabz
    public void test_Blrabz(ArmAsmFieldType fmt) {
        testDecode("blrabz", "blrabz", fmt,
                Integer.toHexString(0b1101_011_0001_11111_000011_00000_00000));
    }

    // blrab
    public void test_Blrab(ArmAsmFieldType fmt) {
        testDecode("blrab", "blrab", fmt,
                Integer.toHexString(0b1101_011_1001_11111_000011_00000_00000));
    }

    @Test
    public void test_UnconditionalBranchRegister() {
        var fmt = ArmAsmFieldType.UCONDITIONALBRANCH_REG;
        test_Br(fmt);
        test_Braaz(fmt);
        test_Braa(fmt);
        test_Brabz(fmt);
        test_Brab(fmt);
        test_Blraaz(fmt);
        test_Blraa(fmt);
        test_Blrabz(fmt);
        test_Blrab(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // UNCONDITIONAL BRANCH (IMMEDIATE) (C4-264 to C4-265) ////////////////////
    // b
    @Test
    public void test_UnconditionalBranchImmediate() {
        var fmt = ArmAsmFieldType.UCONDITIONALBRANCH_IMM;
        testDecode("b", "b", fmt,
                Integer.toHexString(0b0_00101_00000000000000000000000000));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // COMPARE AND BRANCH (MMEDIATE) (C4-265) /////////////////////////////////
    // cbz 32 bits
    public void test_Cbz32(ArmAsmFieldType fmt) {
        testDecode("cbz32", "cbz", fmt,
                Integer.toHexString(0b0_011010_0_000000000000000000000000));
    }

    // cbz 64 bits
    public void test_Cbz64(ArmAsmFieldType fmt) {
        testDecode("cbz64", "cbz", fmt,
                Integer.toHexString(0b1_011010_0_000000000000000000000000));
    }

    // cbnz 32 bits
    public void test_Cbnz32(ArmAsmFieldType fmt) {
        testDecode("cbnz32", "cbnz", fmt,
                Integer.toHexString(0b0_011010_1_000000000000000000000000));
    }

    // cbnz 64 bits
    public void test_Cbnz64(ArmAsmFieldType fmt) {
        testDecode("cbnz64", "cbnz", fmt,
                Integer.toHexString(0b1_011010_1_000000000000000000000000));
    }

    @Test
    public void test_CompareAndBranch() {
        var fmt = ArmAsmFieldType.COMPARE_AND_BRANCH_IMM;
        test_Cbz32(fmt);
        test_Cbz64(fmt);
        test_Cbnz32(fmt);
        test_Cbnz64(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // TEST_AND_BRANCH (C4-265) ///////////////////////////////////////////////
    // tbz
    public void test_Tbz(ArmAsmFieldType fmt) {
        testDecode("tbz", "tbz", fmt,
                Integer.toHexString(0b0_011011_0_000000000000000000000000));
    }

    // tbnz
    public void test_Tbnz(ArmAsmFieldType fmt) {
        testDecode("tbnz", "tbnz", fmt,
                Integer.toHexString(0b0_011011_1_000000000000000000000000));
    }

    @Test
    public void test_test_and_branch() {
        var fmt = ArmAsmFieldType.TEST_AND_BRANCH;
        test_Tbz(fmt);
        test_Tbnz(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_REG_LITERAL (C4-280) //////////////////////////////////////////////
    // ldr literal 32 bits
    public void test_ldr32literal(ArmAsmFieldType fmt) {
        testDecode("ldr_32_literal", "ldr", fmt,
                Integer.toHexString(0b00_011_000_0000000000000000000_00000));
    }

    // ldr literal 64 bits
    public void test_ldr64literal(ArmAsmFieldType fmt) {
        testDecode("ldr_64_literal", "ldr", fmt,
                Integer.toHexString(0b01_011_000_0000000000000000000_00000));
    }

    // ldr literal simd 32 bits
    public void test_ldr32literalsimd(ArmAsmFieldType fmt) {
        testDecode("ldr_32_literal_simd", "ldr", fmt,
                Integer.toHexString(0b00_011_100_0000000000000000000_00000));
    }

    // ldr literal simd 64 bits
    public void test_ldr64literalsimd(ArmAsmFieldType fmt) {
        testDecode("ldr_64_literal_simd", "ldr", fmt,
                Integer.toHexString(0b01_011_100_0000000000000000000_00000));
    }

    // ldr literal simd 128 bits
    public void test_ldr128literalsimd(ArmAsmFieldType fmt) {
        testDecode("ldr_128_literal_simd", "ldr", fmt,
                Integer.toHexString(0b10_011_100_0000000000000000000_00000));
    }

    @Test
    public void test_load_reg_literal_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_REG_LITERAL_FMT1;
        test_ldr32literal(fmt);
        test_ldr64literal(fmt);
        test_ldr32literalsimd(fmt);
        test_ldr64literalsimd(fmt);
        test_ldr128literalsimd(fmt);
    }

    // ldrsw_reg
    public void test_ldrsw_reg(ArmAsmFieldType fmt) {
        testDecode("ldrsw_reg", "ldrsw", fmt,
                Integer.toHexString(0b10_011_000_0000000000000000000_00000));
    }

    // prfm_reg
    public void test_prfm_reg(ArmAsmFieldType fmt) {
        testDecode("prfm_reg", "prfm", fmt,
                Integer.toHexString(0b11_011_000_0000000000000000000_00000));
    }

    @Test
    public void test_load_reg_literal_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_REG_LITERAL_FMT2;
        test_ldrsw_reg(fmt);
        test_prfm_reg(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_PAIR_NO_ALLOC (C4-280 to C4-281) ////////////////
    // stnp 32
    public void test_stnp32(ArmAsmFieldType fmt) {
        testDecode("stnp_32_scalar", "stnp", fmt,
                Integer.toHexString(0b00_101_0_000_0_0000000_00000_00000_00000));
    }

    // stnp 64
    public void test_stnp64(ArmAsmFieldType fmt) {
        testDecode("stnp_64_scalar", "stnp", fmt,
                Integer.toHexString(0b10_101_0_000_0_0000000_00000_00000_00000));
    }

    // stnp 32 simd
    public void test_stnp32simd(ArmAsmFieldType fmt) {
        testDecode("stnp_32_simd", "stnp", fmt,
                Integer.toHexString(0b00_101_1_000_0_0000000_00000_00000_00000));
    }

    // stnp 64 simd
    public void test_stnp64simd(ArmAsmFieldType fmt) {
        testDecode("stnp_64_simd", "stnp", fmt,
                Integer.toHexString(0b01_101_1_000_0_0000000_00000_00000_00000));
    }

    // stnp 128 simd
    public void test_stnp128simd(ArmAsmFieldType fmt) {
        testDecode("stnp_128_simd", "stnp", fmt,
                Integer.toHexString(0b10_101_1_000_0_0000000_00000_00000_00000));
    }

    // ldnp 32
    public void test_ldnp32(ArmAsmFieldType fmt) {
        testDecode("ldnp_32_scalar", "ldnp", fmt,
                Integer.toHexString(0b00_101_0_000_1_0000000_00000_00000_00000));
    }

    // ldnp 64
    public void test_ldnp64(ArmAsmFieldType fmt) {
        testDecode("ldnp_64_scalar", "ldnp", fmt,
                Integer.toHexString(0b10_101_0_000_1_0000000_00000_00000_00000));
    }

    // ldnp 32 simd
    public void test_ldnp32simd(ArmAsmFieldType fmt) {
        testDecode("ldnp_32_simd", "ldnp", fmt,
                Integer.toHexString(0b00_101_1_000_1_0000000_00000_00000_00000));
    }

    // ldnp 64 simd
    public void test_ldnp64simd(ArmAsmFieldType fmt) {
        testDecode("ldnp_64_simd", "ldnp", fmt,
                Integer.toHexString(0b01_101_1_000_1_0000000_00000_00000_00000));
    }

    // ldnp 128 simd
    public void test_ldnp128simd(ArmAsmFieldType fmt) {
        testDecode("ldnp_128_simd", "ldnp", fmt,
                Integer.toHexString(0b10_101_1_000_1_0000000_00000_00000_00000));
    }

    @Test
    public void test_load_store_pair_no_alloc() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_NO_ALLOC;
        test_stnp32(fmt);
        test_stnp64(fmt);
        test_stnp32simd(fmt);
        test_stnp64simd(fmt);
        test_stnp128simd(fmt);
        test_ldnp32(fmt);
        test_ldnp64(fmt);
        test_ldnp32simd(fmt);
        test_ldnp64simd(fmt);
        test_ldnp128simd(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_PAIR (POST INDEXED) (C4-281 to C4-282) //////////////////////
    // stp 32
    public void test_stp32post(ArmAsmFieldType fmt) {
        testDecode("stp_32_post", "stp", fmt,
                Integer.toHexString(0b00_101_0_001_0_0000000_00000_00000_00000));
    }

    // stp 64
    public void test_stp64post(ArmAsmFieldType fmt) {
        testDecode("stp_64_post", "stp", fmt,
                Integer.toHexString(0b10_101_0_001_0_0000000_00000_00000_00000));
    }

    // stp 32 simd
    public void test_stp32simdpost(ArmAsmFieldType fmt) {
        testDecode("stp_32_simd_post", "stp", fmt,
                Integer.toHexString(0b00_101_1_001_0_0000000_00000_00000_00000));
    }

    // stp 64 simd
    public void test_stp64simdpost(ArmAsmFieldType fmt) {
        testDecode("stp_64_simd_post", "stp", fmt,
                Integer.toHexString(0b01_101_1_001_0_0000000_00000_00000_00000));
    }

    // stp 128 simd
    public void test_stp128simdpost(ArmAsmFieldType fmt) {
        testDecode("stp_128_simd_post", "stp", fmt,
                Integer.toHexString(0b10_101_1_001_0_0000000_00000_00000_00000));
    }

    // ldp 32
    public void test_ldp32post(ArmAsmFieldType fmt) {
        testDecode("ldp_32_post", "ldp", fmt,
                Integer.toHexString(0b00_101_0_001_1_0000000_00000_00000_00000));
    }

    // ldp 64
    public void test_ldp64post(ArmAsmFieldType fmt) {
        testDecode("ldp_64_post", "ldp", fmt,
                Integer.toHexString(0b10_101_0_001_1_0000000_00000_00000_00000));
    }

    // ldp 32 simd
    public void test_ldp32simdpost(ArmAsmFieldType fmt) {
        testDecode("ldp_32_simd_post", "ldp", fmt,
                Integer.toHexString(0b00_101_1_001_1_0000000_00000_00000_00000));
    }

    // ldp 64 simd
    public void test_ldp64simdpost(ArmAsmFieldType fmt) {
        testDecode("ldp_64_simd_post", "ldp", fmt,
                Integer.toHexString(0b01_101_1_001_1_0000000_00000_00000_00000));
    }

    // ldp 128 simd
    public void test_ldp128simdpost(ArmAsmFieldType fmt) {
        testDecode("ldp_128_simd_post", "ldp", fmt,
                Integer.toHexString(0b10_101_1_001_1_0000000_00000_00000_00000));
    }

    @Test
    public void test_load_store_pair_post_index_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1;
        test_stp32post(fmt);
        test_stp64post(fmt);
        test_stp32simdpost(fmt);
        test_stp64simdpost(fmt);
        test_stp128simdpost(fmt);

        test_ldp32post(fmt);
        test_ldp64post(fmt);
        test_ldp32simdpost(fmt);
        test_ldp64simdpost(fmt);
        test_ldp128simdpost(fmt);
    }

    // stgp
    public void test_stgp_post(ArmAsmFieldType fmt) {
        testDecode("stgp_post", "stgp", fmt,
                Integer.toHexString(0b01_101_0_001_0_0000000_00000_00000_00000));
    }

    // ldpsw
    public void test_ldpsw_post(ArmAsmFieldType fmt) {
        testDecode("ldpsw_post", "ldpsw", fmt,
                Integer.toHexString(0b01_101_0_001_1_0000000_00000_00000_00000));
    }

    @Test
    public void test_load_store_pair_post_index_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2;
        test_stgp_post(fmt);
        test_ldpsw_post(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_PAIR (PRE INDEXED) (C4-281 to C4-282) //////////////////////
    // stp 32
    public void test_stp32pre(ArmAsmFieldType fmt) {
        testDecode("stp_32_pre", "stp", fmt,
                Integer.toHexString(0b00_101_0_011_0_0000000_00000_00000_00000));
    }

    // stp 64
    public void test_stp64pre(ArmAsmFieldType fmt) {
        testDecode("stp_64_pre", "stp", fmt,
                Integer.toHexString(0b10_101_0_011_0_0000000_00000_00000_00000));
    }

    // stp 32 simd
    public void test_stp32simdpre(ArmAsmFieldType fmt) {
        testDecode("stp_32_simd_pre", "stp", fmt,
                Integer.toHexString(0b00_101_1_011_0_0000000_00000_00000_00000));
    }

    // stp 64 simd
    public void test_stp64simdpre(ArmAsmFieldType fmt) {
        testDecode("stp_64_simd_pre", "stp", fmt,
                Integer.toHexString(0b01_101_1_011_0_0000000_00000_00000_00000));
    }

    // stp 128 simd
    public void test_stp128simdpre(ArmAsmFieldType fmt) {
        testDecode("stp_128_simd_pre", "stp", fmt,
                Integer.toHexString(0b10_101_1_011_0_0000000_00000_00000_00000));
    }

    // ldp 32
    public void test_ldp32pre(ArmAsmFieldType fmt) {
        testDecode("ldp_32_pre", "ldp", fmt,
                Integer.toHexString(0b00_101_0_011_1_0000000_00000_00000_00000));
    }

    // ldp 64
    public void test_ldp64pre(ArmAsmFieldType fmt) {
        testDecode("ldp_64_pre", "ldp", fmt,
                Integer.toHexString(0b10_101_0_011_1_0000000_00000_00000_00000));
    }

    // ldp 32 simd
    public void test_ldp32simdpre(ArmAsmFieldType fmt) {
        testDecode("ldp_32_simd_pre", "ldp", fmt,
                Integer.toHexString(0b00_101_1_011_1_0000000_00000_00000_00000));
    }

    // ldp 64 simd
    public void test_ldp64simdpre(ArmAsmFieldType fmt) {
        testDecode("ldp_64_simd_pre", "ldp", fmt,
                Integer.toHexString(0b01_101_1_011_1_0000000_00000_00000_00000));
    }

    // ldp 128 simd
    public void test_ldp128simdpre(ArmAsmFieldType fmt) {
        testDecode("ldp_128_simd_pre", "ldp", fmt,
                Integer.toHexString(0b10_101_1_011_1_0000000_00000_00000_00000));
    }

    @Test
    public void test_load_store_pair_pre_index_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1;
        test_stp32pre(fmt);
        test_stp64pre(fmt);
        test_stp32simdpre(fmt);
        test_stp64simdpre(fmt);
        test_stp128simdpre(fmt);

        test_ldp32pre(fmt);
        test_ldp64pre(fmt);
        test_ldp32simdpre(fmt);
        test_ldp64simdpre(fmt);
        test_ldp128simdpre(fmt);
    }

    // stgp
    public void test_stgp_pre(ArmAsmFieldType fmt) {
        testDecode("stgp_pre", "stgp", fmt,
                Integer.toHexString(0b01_101_0_011_0_0000000_00000_00000_00000));
    }

    // ldpsw
    public void test_ldpsw_pre(ArmAsmFieldType fmt) {
        testDecode("ldpsw_pre", "ldpsw", fmt,
                Integer.toHexString(0b01_101_0_011_1_0000000_00000_00000_00000));
    }

    @Test
    public void test_load_store_pair_pre_index_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2;
        test_stgp_pre(fmt);
        test_ldpsw_pre(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_PAIR_OFFSET (C4-281 to C4-282) //////////////////////
    // stp 32
    public void test_stp32off(ArmAsmFieldType fmt) {
        testDecode("stp_32_off", "stp", fmt,
                Integer.toHexString(0b00_101_0_010_0_0000000_00000_00000_00000));
    }

    // stp 64
    public void test_stp64off(ArmAsmFieldType fmt) {
        testDecode("stp_64_off", "stp", fmt,
                Integer.toHexString(0b10_101_0_010_0_0000000_00000_00000_00000));
    }

    // stp 32 simd
    public void test_stp32simdoff(ArmAsmFieldType fmt) {
        testDecode("stp_32_simd_off", "stp", fmt,
                Integer.toHexString(0b00_101_1_010_0_0000000_00000_00000_00000));
    }

    // stp 64 simd
    public void test_stp64simdoff(ArmAsmFieldType fmt) {
        testDecode("stp_64_simd_off", "stp", fmt,
                Integer.toHexString(0b01_101_1_010_0_0000000_00000_00000_00000));
    }

    // stp 128 simd
    public void test_stp128simdoff(ArmAsmFieldType fmt) {
        testDecode("stp_128_simd_off", "stp", fmt,
                Integer.toHexString(0b10_101_1_010_0_0000000_00000_00000_00000));
    }

    // ldp 32
    public void test_ldp32off(ArmAsmFieldType fmt) {
        testDecode("ldp_32_off", "ldp", fmt,
                Integer.toHexString(0b00_101_0_010_1_0000000_00000_00000_00000));
    }

    // ldp 64
    public void test_ldp64off(ArmAsmFieldType fmt) {
        testDecode("ldp_64_off", "ldp", fmt,
                Integer.toHexString(0b10_101_0_010_1_0000000_00000_00000_00000));
    }

    // ldp 32 simd
    public void test_ldp32simdoff(ArmAsmFieldType fmt) {
        testDecode("ldp_32_simd_off", "ldp", fmt,
                Integer.toHexString(0b00_101_1_010_1_0000000_00000_00000_00000));
    }

    // ldp 64 simd
    public void test_ldp64simdoff(ArmAsmFieldType fmt) {
        testDecode("ldp_64_simd_off", "ldp", fmt,
                Integer.toHexString(0b01_101_1_010_1_0000000_00000_00000_00000));
    }

    // ldp 128 simd
    public void test_ldp128simdoff(ArmAsmFieldType fmt) {
        testDecode("ldp_128_simd_off", "ldp", fmt,
                Integer.toHexString(0b10_101_1_010_1_0000000_00000_00000_00000));
    }

    @Test
    public void test_load_store_pair_off_index_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1;
        test_stp32off(fmt);
        test_stp64off(fmt);
        test_stp32simdoff(fmt);
        test_stp64simdoff(fmt);
        test_stp128simdoff(fmt);

        test_ldp32off(fmt);
        test_ldp64off(fmt);
        test_ldp32simdoff(fmt);
        test_ldp64simdoff(fmt);
        test_ldp128simdoff(fmt);
    }

    // stgp
    public void test_stgp_off(ArmAsmFieldType fmt) {
        testDecode("stgp_off", "stgp", fmt,
                Integer.toHexString(0b01_101_0_010_0_0000000_00000_00000_00000));
    }

    // ldpsw
    public void test_ldpsw_off(ArmAsmFieldType fmt) {
        testDecode("ldpsw_off", "ldpsw", fmt,
                Integer.toHexString(0b01_101_0_010_1_0000000_00000_00000_00000));
    }

    @Test
    public void test_load_store_pair_off_index_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2;
        test_stgp_off(fmt);
        test_ldpsw_off(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_PAIR_IMM_UNSCALED (C4-283 to C4-284) ///////////////////
    // sturb
    public void test_sturb(ArmAsmFieldType fmt) {
        testDecode("sturb", "sturb", fmt,
                Integer.toHexString(0b00_111_0_00_00_0_000000000_00_00000_00000));
    }

    // ldurb
    public void test_ldurb(ArmAsmFieldType fmt) {
        testDecode("ldurb", "ldurb", fmt,
                Integer.toHexString(0b00_111_0_00_01_0_000000000_00_00000_00000));
    }

    // sturh
    public void test_sturh(ArmAsmFieldType fmt) {
        testDecode("sturh", "sturh", fmt,
                Integer.toHexString(0b01_111_0_00_00_0_000000000_00_00000_00000));
    }

    // ldurh
    public void test_ldurh(ArmAsmFieldType fmt) {
        testDecode("ldurh", "ldurh", fmt,
                Integer.toHexString(0b01_111_0_00_01_0_000000000_00_00000_00000));
    }

    // ldursw
    public void test_ldursw(ArmAsmFieldType fmt) {
        testDecode("ldursw", "ldursw", fmt,
                Integer.toHexString(0b10_111_000_10_0_000000000_00_00000_00000));
    }

    // prfm_reg
    public void test_prfum(ArmAsmFieldType fmt) {
        testDecode("prfum", "prfum", fmt,
                Integer.toHexString(0b11_111_000_10_0_000000000_00_00000_00000));
    }

    @Test
    public void test_load_store_pair_imm_unscaled_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT1;
        test_sturb(fmt);
        test_ldurb(fmt);
        test_sturh(fmt);
        test_ldurh(fmt);
        test_ldursw(fmt);
        test_prfum(fmt);
    }

    // ldursb 32 bit
    public void test_ldursb32(ArmAsmFieldType fmt) {
        testDecode("ldursb32", "ldursb", fmt,
                Integer.toHexString(0b00_111_0_00_11_0_000000000_00_00000_00000));
    }

    // ldursb 64 bit
    public void test_ldursb64(ArmAsmFieldType fmt) {
        testDecode("ldursb64", "ldursb", fmt,
                Integer.toHexString(0b00_111_0_00_10_0_000000000_00_00000_00000));
    }

    // ldursh 32 bit
    public void test_ldursh32(ArmAsmFieldType fmt) {
        testDecode("ldursh32", "ldursh", fmt,
                Integer.toHexString(0b01_111_0_00_11_0_000000000_00_00000_00000));
    }

    // ldursh 64 bit
    public void test_ldursh64(ArmAsmFieldType fmt) {
        testDecode("ldursh64", "ldursh", fmt,
                Integer.toHexString(0b01_111_0_00_10_0_000000000_00_00000_00000));
    }

    // stur 32 bit
    public void test_stur32(ArmAsmFieldType fmt) {
        testDecode("stur_32", "stur", fmt,
                Integer.toHexString(0b10_111_0_00_00_0_000000000_00_00000_00000));
    }

    // stur 64 bit
    public void test_stur64(ArmAsmFieldType fmt) {
        testDecode("stur_64", "stur", fmt,
                Integer.toHexString(0b11_111_0_00_00_0_000000000_00_00000_00000));
    }

    // ldur 32 bit
    public void test_ldur32(ArmAsmFieldType fmt) {
        testDecode("ldur_32", "ldur", fmt,
                Integer.toHexString(0b10_111_0_00_01_0_000000000_00_00000_00000));
    }

    // ldur 64 bit
    public void test_ldur64(ArmAsmFieldType fmt) {
        testDecode("ldur_64", "ldur", fmt,
                Integer.toHexString(0b11_111_0_00_01_0_000000000_00_00000_00000));
    }

    @Test
    public void test_load_store_pair_imm_unscaled_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT2;
        test_ldursb32(fmt);
        test_ldursb64(fmt);
        test_ldursh32(fmt);
        test_ldursh64(fmt);
        test_stur32(fmt);
        test_stur64(fmt);
        test_ldur32(fmt);
        test_ldur64(fmt);
    }

    // stur simd 8 bit
    public void test_stursimd8(ArmAsmFieldType fmt) {
        testDecode("stur_8_simd", "stur", fmt,
                Integer.toHexString(0b00_111_1_00_00_0_000000000_00_00000_00000));
    }

    // stur simd 16 bit
    public void test_stursimd16(ArmAsmFieldType fmt) {
        testDecode("stur_16_simd", "stur", fmt,
                Integer.toHexString(0b01_111_1_00_00_0_000000000_00_00000_00000));
    }

    // stur simd 32 bit
    public void test_stursimd32(ArmAsmFieldType fmt) {
        testDecode("stur_32_simd", "stur", fmt,
                Integer.toHexString(0b10_111_1_00_00_0_000000000_00_00000_00000));
    }

    // stur simd 64 bit
    public void test_stursimd64(ArmAsmFieldType fmt) {
        testDecode("stur_64_simd", "stur", fmt,
                Integer.toHexString(0b11_111_1_00_00_0_000000000_00_00000_00000));
    }

    // stur simd 128 bit
    public void test_stursimd128(ArmAsmFieldType fmt) {
        testDecode("stur_128_simd", "stur", fmt,
                Integer.toHexString(0b00_111_1_00_10_0_000000000_00_00000_00000));
    }

    // ldur simd 8 bit
    public void test_ldursimd8(ArmAsmFieldType fmt) {
        testDecode("ldur_8_simd", "ldur", fmt,
                Integer.toHexString(0b00_111_1_00_01_0_000000000_00_00000_00000));
    }

    // ldur simd 16 bit
    public void test_ldursimd16(ArmAsmFieldType fmt) {
        testDecode("ldur_16_simd", "ldur", fmt,
                Integer.toHexString(0b01_111_1_00_01_0_000000000_00_00000_00000));
    }

    // ldur simd 32 bit
    public void test_ldursimd32(ArmAsmFieldType fmt) {
        testDecode("ldur_32_simd", "ldur", fmt,
                Integer.toHexString(0b10_111_1_00_01_0_000000000_00_00000_00000));
    }

    // ldur simd 64 bit
    public void test_ldursimd64(ArmAsmFieldType fmt) {
        testDecode("ldur_64_simd", "ldur", fmt,
                Integer.toHexString(0b11_111_1_00_01_0_000000000_00_00000_00000));
    }

    // ldur simd 128 bit
    public void test_ldursimd128(ArmAsmFieldType fmt) {
        testDecode("ldur_128_simd", "ldur", fmt,
                Integer.toHexString(0b00_111_1_00_11_0_000000000_00_00000_00000));
    }

    @Test
    public void test_load_store_pair_imm_unscaled_fmt3() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT3;
        test_stursimd8(fmt);
        test_stursimd16(fmt);
        test_stursimd32(fmt);
        test_stursimd64(fmt);
        test_stursimd128(fmt);
        test_ldursimd8(fmt);
        test_ldursimd16(fmt);
        test_ldursimd32(fmt);
        test_ldursimd64(fmt);
        test_ldursimd128(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_PAIR_UNPRIV (C4-286) ////////////////////////////////////////
    // sttrb
    public void test_sttrb(ArmAsmFieldType fmt) {
        testDecode("sttrb", "sttrb", fmt,
                Integer.toHexString(0b00_111_0_00_00_0_000000000_10_00000_00000));
    }

    // ldtrb
    public void test_ldtrb(ArmAsmFieldType fmt) {
        testDecode("ldtrb", "ldtrb", fmt,
                Integer.toHexString(0b00_111_0_00_01_0_000000000_10_00000_00000));
    }

    // sttrh
    public void test_sttrh(ArmAsmFieldType fmt) {
        testDecode("sttrh", "sttrh", fmt,
                Integer.toHexString(0b01_111_0_00_00_0_000000000_10_00000_00000));
    }

    // ldtrh
    public void test_ldtrh(ArmAsmFieldType fmt) {
        testDecode("ldtrh", "ldtrh", fmt,
                Integer.toHexString(0b01_111_0_00_01_0_000000000_10_00000_00000));
    }

    // ldtrsw
    public void test_ldtrsw(ArmAsmFieldType fmt) {
        testDecode("ldtrsw", "ldtrsw", fmt,
                Integer.toHexString(0b10_111_000_10_0_000000000_10_00000_00000));
    }

    @Test
    public void test_load_store_pair_unpriv_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT1;
        test_sttrb(fmt);
        test_ldtrb(fmt);
        test_sttrh(fmt);
        test_ldtrh(fmt);
        test_ldtrsw(fmt);
    }

    // ldtrsb 32 bit
    public void test_ldtrsb32(ArmAsmFieldType fmt) {
        testDecode("ldtrsb32", "ldtrsb", fmt,
                Integer.toHexString(0b00_111_0_00_11_0_000000000_10_00000_00000));
    }

    // ldtrsb 64 bit
    public void test_ldtrsb64(ArmAsmFieldType fmt) {
        testDecode("ldtrsb64", "ldtrsb", fmt,
                Integer.toHexString(0b00_111_0_00_10_0_000000000_10_00000_00000));
    }

    // ldtrsh 32 bit
    public void test_ldtrsh32(ArmAsmFieldType fmt) {
        testDecode("ldtrsh32", "ldtrsh", fmt,
                Integer.toHexString(0b01_111_0_00_11_0_000000000_10_00000_00000));
    }

    // ldtrsh 64 bit
    public void test_ldtrsh64(ArmAsmFieldType fmt) {
        testDecode("ldtrsh64", "ldtrsh", fmt,
                Integer.toHexString(0b01_111_0_00_10_0_000000000_10_00000_00000));
    }

    // sttr 32 bit
    public void test_sttr32(ArmAsmFieldType fmt) {
        testDecode("sttr_32", "sttr", fmt,
                Integer.toHexString(0b10_111_0_00_00_0_000000000_10_00000_00000));
    }

    // sttr 64 bit
    public void test_sttr64(ArmAsmFieldType fmt) {
        testDecode("sttr_64", "sttr", fmt,
                Integer.toHexString(0b11_111_0_00_00_0_000000000_10_00000_00000));
    }

    // ldtr 32 bit
    public void test_ldtr32(ArmAsmFieldType fmt) {
        testDecode("ldtr_32", "ldtr", fmt,
                Integer.toHexString(0b10_111_0_00_01_0_000000000_10_00000_00000));
    }

    // ldtr 64 bit
    public void test_ldtr64(ArmAsmFieldType fmt) {
        testDecode("ldtr_64", "ldtr", fmt,
                Integer.toHexString(0b11_111_0_00_01_0_000000000_10_00000_00000));
    }

    @Test
    public void test_load_store_pair_unpriv_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_STORE_PAIR_UNPRIV_UNSCALED_FMT2;
        test_ldtrsb32(fmt);
        test_ldtrsb64(fmt);
        test_ldtrsh32(fmt);
        test_ldtrsh64(fmt);
        test_sttr32(fmt);
        test_sttr64(fmt);
        test_ldtr32(fmt);
        test_ldtr64(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_REG_IMM_PREPOST_FMT1 (C4-284 to C4-285 and C4-286 to C4-287)
    // strb
    public void test_strb_pre(ArmAsmFieldType fmt) {
        testDecode("strb_pre", "strb", fmt,
                Integer.toHexString(0b00_111_0_00_00_0_000000000_11_00000_00000));
    }

    // ldrb
    public void test_ldrb_pre(ArmAsmFieldType fmt) {
        testDecode("ldrb_pre", "ldrb", fmt,
                Integer.toHexString(0b00_111_0_00_01_0_000000000_11_00000_00000));
    }

    // strh
    public void test_strh_pre(ArmAsmFieldType fmt) {
        testDecode("strh_pre", "strh", fmt,
                Integer.toHexString(0b01_111_0_00_00_0_000000000_11_00000_00000));
    }

    // ldrh
    public void test_ldrh_pre(ArmAsmFieldType fmt) {
        testDecode("ldrh_pre", "ldrh", fmt,
                Integer.toHexString(0b01_111_0_00_01_0_000000000_11_00000_00000));
    }

    // ldrsw
    public void test_ldrsw_pre(ArmAsmFieldType fmt) {
        testDecode("ldrsw_pre", "ldrsw", fmt,
                Integer.toHexString(0b10_111_000_10_0_000000000_11_00000_00000));
    }

    @Test
    public void test_load_store_reg_imm_pre_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT1;
        test_strb_pre(fmt);
        test_ldrb_pre(fmt);
        test_strh_pre(fmt);
        test_ldrh_pre(fmt);
        test_ldrsw_pre(fmt);
    }

    // ldrsb 32 bit
    public void test_ldrsb32_pre(ArmAsmFieldType fmt) {
        testDecode("ldrsb32_pre", "ldrsb", fmt,
                Integer.toHexString(0b00_111_0_00_11_0_000000000_11_00000_00000));
    }

    // ldrsb 64 bit
    public void test_ldrsb64_pre(ArmAsmFieldType fmt) {
        testDecode("ldrsb64_pre", "ldrsb", fmt,
                Integer.toHexString(0b00_111_0_00_10_0_000000000_11_00000_00000));
    }

    // ldrsh 32 bit
    public void test_ldrsh32_pre(ArmAsmFieldType fmt) {
        testDecode("ldrsh32_pre", "ldrsh", fmt,
                Integer.toHexString(0b01_111_0_00_11_0_000000000_11_00000_00000));
    }

    // ldrsh 64 bit
    public void test_ldrsh64_pre(ArmAsmFieldType fmt) {
        testDecode("ldrsh64_pre", "ldrsh", fmt,
                Integer.toHexString(0b01_111_0_00_10_0_000000000_11_00000_00000));
    }

    // str 32 bit
    public void test_str32_pre(ArmAsmFieldType fmt) {
        testDecode("str_32_pre", "str", fmt,
                Integer.toHexString(0b10_111_0_00_00_0_000000000_11_00000_00000));
    }

    // str 64 bit
    public void test_str64_pre(ArmAsmFieldType fmt) {
        testDecode("str_64_pre", "str", fmt,
                Integer.toHexString(0b11_111_0_00_00_0_000000000_11_00000_00000));
    }

    // ldr 32 bit
    public void test_ldr32_pre(ArmAsmFieldType fmt) {
        testDecode("ldr_32_pre", "ldr", fmt,
                Integer.toHexString(0b10_111_0_00_01_0_000000000_11_00000_00000));
    }

    // ldr 64 bit
    public void test_ldr64_pre(ArmAsmFieldType fmt) {
        testDecode("ldr_64_pre", "ldr", fmt,
                Integer.toHexString(0b11_111_0_00_01_0_000000000_11_00000_00000));
    }

    @Test
    public void test_load_store_reg_imm_pre_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT2;
        test_ldrsb32_pre(fmt);
        test_ldrsb64_pre(fmt);
        test_ldrsh32_pre(fmt);
        test_ldrsh64_pre(fmt);
        test_str32_pre(fmt);
        test_str64_pre(fmt);
        test_ldr32_pre(fmt);
        test_ldr64_pre(fmt);
    }

    // str simd 8 bit
    public void test_strsimd8_pre(ArmAsmFieldType fmt) {
        testDecode("str_8_simd_pre", "str", fmt,
                Integer.toHexString(0b00_111_1_00_00_0_000000000_11_00000_00000));
    }

    // str simd 16 bit
    public void test_strsimd16_pre(ArmAsmFieldType fmt) {
        testDecode("str_16_simd_pre", "str", fmt,
                Integer.toHexString(0b01_111_1_00_00_0_000000000_11_00000_00000));
    }

    // str simd 32 bit
    public void test_strsimd32_pre(ArmAsmFieldType fmt) {
        testDecode("str_32_simd_pre", "str", fmt,
                Integer.toHexString(0b10_111_1_00_00_0_000000000_11_00000_00000));
    }

    // str simd 64 bit
    public void test_strsimd64_pre(ArmAsmFieldType fmt) {
        testDecode("str_64_simd_pre", "str", fmt,
                Integer.toHexString(0b11_111_1_00_00_0_000000000_11_00000_00000));
    }

    // str simd 128 bit
    public void test_strsimd128_pre(ArmAsmFieldType fmt) {
        testDecode("str_128_simd_pre", "str", fmt,
                Integer.toHexString(0b00_111_1_00_10_0_000000000_11_00000_00000));
    }

    // ldr simd 8 bit
    public void test_ldrsimd8_pre(ArmAsmFieldType fmt) {
        testDecode("ldr_8_simd_pre", "ldr", fmt,
                Integer.toHexString(0b00_111_1_00_01_0_000000000_11_00000_00000));
    }

    // ldr simd 16 bit
    public void test_ldrsimd16_pre(ArmAsmFieldType fmt) {
        testDecode("ldr_16_simd_pre", "ldr", fmt,
                Integer.toHexString(0b01_111_1_00_01_0_000000000_11_00000_00000));
    }

    // ldr simd 32 bit
    public void test_ldrsimd32_pre(ArmAsmFieldType fmt) {
        testDecode("ldr_32_simd_pre", "ldr", fmt,
                Integer.toHexString(0b10_111_1_00_01_0_000000000_11_00000_00000));
    }

    // ldr simd 64 bit
    public void test_ldrsimd64_pre(ArmAsmFieldType fmt) {
        testDecode("ldr_64_simd_pre", "ldr", fmt,
                Integer.toHexString(0b11_111_1_00_01_0_000000000_11_00000_00000));
    }

    // ldr simd 128 bit
    public void test_ldrsimd128_pre(ArmAsmFieldType fmt) {
        testDecode("ldr_128_simd_pre", "ldr", fmt,
                Integer.toHexString(0b00_111_1_00_11_0_000000000_11_00000_00000));
    }

    @Test
    public void test_load_store_reg_imm_pre_fmt3() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT3;
        test_strsimd8_pre(fmt);
        test_strsimd16_pre(fmt);
        test_strsimd32_pre(fmt);
        test_strsimd64_pre(fmt);
        test_strsimd128_pre(fmt);
        test_ldrsimd8_pre(fmt);
        test_ldrsimd16_pre(fmt);
        test_ldrsimd32_pre(fmt);
        test_ldrsimd64_pre(fmt);
        test_ldrsimd128_pre(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_REG_IMM_PREPOST_FMT1 (C4-284 to C4-285 and C4-286 to C4-287)
    // strb
    public void test_strb_post(ArmAsmFieldType fmt) {
        testDecode("strb_post", "strb", fmt,
                Integer.toHexString(0b00_111_0_00_00_0_000000000_01_00000_00000));
    }

    // ldrb
    public void test_ldrb_post(ArmAsmFieldType fmt) {
        testDecode("ldrb_post", "ldrb", fmt,
                Integer.toHexString(0b00_111_0_00_01_0_000000000_01_00000_00000));
    }

    // strh
    public void test_strh_post(ArmAsmFieldType fmt) {
        testDecode("strh_post", "strh", fmt,
                Integer.toHexString(0b01_111_0_00_00_0_000000000_01_00000_00000));
    }

    // ldrh
    public void test_ldrh_post(ArmAsmFieldType fmt) {
        testDecode("ldrh_post", "ldrh", fmt,
                Integer.toHexString(0b01_111_0_00_01_0_000000000_01_00000_00000));
    }

    // ldrsw
    public void test_ldrsw_post(ArmAsmFieldType fmt) {
        testDecode("ldrsw_post", "ldrsw", fmt,
                Integer.toHexString(0b10_111_000_10_0_000000000_01_00000_00000));
    }

    @Test
    public void test_load_store_reg_imm_post_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT1;
        test_strb_post(fmt);
        test_ldrb_post(fmt);
        test_strh_post(fmt);
        test_ldrh_post(fmt);
        test_ldrsw_post(fmt);
    }

    // ldrsb 32 bit
    public void test_ldrsb32_post(ArmAsmFieldType fmt) {
        testDecode("ldrsb32_post", "ldrsb", fmt,
                Integer.toHexString(0b00_111_0_00_11_0_000000000_01_00000_00000));
    }

    // ldrsb 64 bit
    public void test_ldrsb64_post(ArmAsmFieldType fmt) {
        testDecode("ldrsb64_post", "ldrsb", fmt,
                Integer.toHexString(0b00_111_0_00_10_0_000000000_01_00000_00000));
    }

    // ldrsh 32 bit
    public void test_ldrsh32_post(ArmAsmFieldType fmt) {
        testDecode("ldrsh32_post", "ldrsh", fmt,
                Integer.toHexString(0b01_111_0_00_11_0_000000000_01_00000_00000));
    }

    // ldrsh 64 bit
    public void test_ldrsh64_post(ArmAsmFieldType fmt) {
        testDecode("ldrsh64_post", "ldrsh", fmt,
                Integer.toHexString(0b01_111_0_00_10_0_000000000_01_00000_00000));
    }

    // str 32 bit
    public void test_str32_post(ArmAsmFieldType fmt) {
        testDecode("str_32_post", "str", fmt,
                Integer.toHexString(0b10_111_0_00_00_0_000000000_01_00000_00000));
    }

    // str 64 bit
    public void test_str64_post(ArmAsmFieldType fmt) {
        testDecode("str_64_post", "str", fmt,
                Integer.toHexString(0b11_111_0_00_00_0_000000000_01_00000_00000));
    }

    // ldr 32 bit
    public void test_ldr32_post(ArmAsmFieldType fmt) {
        testDecode("ldr_32_post", "ldr", fmt,
                Integer.toHexString(0b10_111_0_00_01_0_000000000_01_00000_00000));
    }

    // ldr 64 bit
    public void test_ldr64_post(ArmAsmFieldType fmt) {
        testDecode("ldr_64_post", "ldr", fmt,
                Integer.toHexString(0b11_111_0_00_01_0_000000000_01_00000_00000));
    }

    // str simd 8 bit
    public void test_strsimd8_post(ArmAsmFieldType fmt) {
        testDecode("str_8_simd_post", "str", fmt,
                Integer.toHexString(0b00_111_1_00_00_0_000000000_01_00000_00000));
    }

    @Test
    public void test_load_store_reg_imm_post_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT2;
        test_ldrsb32_post(fmt);
        test_ldrsb64_post(fmt);
        test_ldrsh32_post(fmt);
        test_ldrsh64_post(fmt);
        test_str32_post(fmt);
        test_str64_post(fmt);
        test_ldr32_post(fmt);
        test_ldr64_post(fmt);
    }

    // str simd 16 bit
    public void test_strsimd16_post(ArmAsmFieldType fmt) {
        testDecode("str_16_simd_post", "str", fmt,
                Integer.toHexString(0b01_111_1_00_00_0_000000000_01_00000_00000));
    }

    // str simd 32 bit
    public void test_strsimd32_post(ArmAsmFieldType fmt) {
        testDecode("str_32_simd_post", "str", fmt,
                Integer.toHexString(0b10_111_1_00_00_0_000000000_01_00000_00000));
    }

    // str simd 64 bit
    public void test_strsimd64_post(ArmAsmFieldType fmt) {
        testDecode("str_64_simd_post", "str", fmt,
                Integer.toHexString(0b11_111_1_00_00_0_000000000_01_00000_00000));
    }

    // str simd 128 bit
    public void test_strsimd128_post(ArmAsmFieldType fmt) {
        testDecode("str_128_simd_post", "str", fmt,
                Integer.toHexString(0b00_111_1_00_10_0_000000000_01_00000_00000));
    }

    // ldr simd 8 bit
    public void test_ldrsimd8_post(ArmAsmFieldType fmt) {
        testDecode("ldr_8_simd_post", "ldr", fmt,
                Integer.toHexString(0b00_111_1_00_01_0_000000000_01_00000_00000));
    }

    // ldr simd 16 bit
    public void test_ldrsimd16_post(ArmAsmFieldType fmt) {
        testDecode("ldr_16_simd_post", "ldr", fmt,
                Integer.toHexString(0b01_111_1_00_01_0_000000000_01_00000_00000));
    }

    // ldr simd 32 bit
    public void test_ldrsimd32_post(ArmAsmFieldType fmt) {
        testDecode("ldr_32_simd_post", "ldr", fmt,
                Integer.toHexString(0b10_111_1_00_01_0_000000000_01_00000_00000));
    }

    // ldr simd 64 bit
    public void test_ldrsimd64_post(ArmAsmFieldType fmt) {
        testDecode("ldr_64_simd_post", "ldr", fmt,
                Integer.toHexString(0b11_111_1_00_01_0_000000000_01_00000_00000));
    }

    // ldr simd 128 bit
    public void test_ldrsimd128_post(ArmAsmFieldType fmt) {
        testDecode("ldr_128_simd_post", "ldr", fmt,
                Integer.toHexString(0b00_111_1_00_11_0_000000000_01_00000_00000));
    }

    @Test
    public void test_load_store_reg_imm_post_fmt3() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_IMM_PREPOST_FMT3;
        test_strsimd8_post(fmt);
        test_strsimd16_post(fmt);
        test_strsimd32_post(fmt);
        test_strsimd64_post(fmt);
        test_strsimd128_post(fmt);
        test_ldrsimd8_post(fmt);
        test_ldrsimd16_post(fmt);
        test_ldrsimd32_post(fmt);
        test_ldrsimd64_post(fmt);
        test_ldrsimd128_post(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_REG_OFF (C4-295 to C4-297) //////////////////////////////////
    // strb_reg_off
    public void test_strb_reg_off(ArmAsmFieldType fmt) {
        testDecode("strb_reg_off", "strb", fmt,
                Integer.toHexString(0b00_111_0_00_00_1_00000_000_0_10_00000_00000));
    }

    // ldrb_reg_off
    public void test_ldrb_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldrb_reg_off", "ldrb", fmt,
                Integer.toHexString(0b00_111_0_00_01_1_00000_000_0_10_00000_00000));
    }

    // strh_reg_off
    public void test_strh_reg_off(ArmAsmFieldType fmt) {
        testDecode("strh_reg_off", "strh", fmt,
                Integer.toHexString(0b01_111_0_00_00_1_00000_000_0_10_00000_00000));
    }

    // ldrh_reg_off
    public void test_ldrh_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldrh_reg_off", "ldrh", fmt,
                Integer.toHexString(0b01_111_0_00_01_1_00000_000_0_10_00000_00000));
    }

    // ldrsw_reg_off
    public void test_ldrsw_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldrsw_reg_off", "ldrsw", fmt,
                Integer.toHexString(0b10_111_0_00_10_1_00000_000_0_10_00000_00000));
    }

    // prfm_reg_off
    public void test_prfm_reg_off(ArmAsmFieldType fmt) {
        testDecode("prfm_reg_off", "prfm", fmt,
                Integer.toHexString(0b11_111_0_00_10_1_00000_000_0_10_00000_00000));
    }

    @Test
    public void test_load_store_reg_off_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT1;
        test_strb_reg_off(fmt);
        test_ldrb_reg_off(fmt);
        test_strh_reg_off(fmt);
        test_ldrh_reg_off(fmt);
        test_ldrsw_reg_off(fmt);
        test_prfm_reg_off(fmt);
    }

    // ldrsb32_reg_off
    public void test_ldrsb32_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldrsb32_reg_off", "ldrsb", fmt,
                Integer.toHexString(0b00_111_0_00_11_1_00000_000_0_10_00000_00000));
    }

    // ldrsb64_reg_off
    public void test_ldrsb64_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldrsb64_reg_off", "ldrsb", fmt,
                Integer.toHexString(0b00_111_0_00_10_1_00000_000_0_10_00000_00000));
    }

    // ldrsh32_reg_off
    public void test_ldrsh32_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldrsh32_reg_off", "ldrsh", fmt,
                Integer.toHexString(0b01_111_0_00_11_1_00000_000_0_10_00000_00000));
    }

    // ldrsh64_reg_off
    public void test_ldrsh64_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldrsh64_reg_off", "ldrsh", fmt,
                Integer.toHexString(0b01_111_0_00_10_1_00000_000_0_10_00000_00000));
    }

    // str 32 bit reg_off
    public void test_str32_reg_off(ArmAsmFieldType fmt) {
        testDecode("str_32_reg_off", "str", fmt,
                Integer.toHexString(0b10_111_0_00_00_1_000000000_10_00000_00000));
    }

    // str 64 reg_off
    public void test_str64_reg_off(ArmAsmFieldType fmt) {
        testDecode("str_64_reg_off", "str", fmt,
                Integer.toHexString(0b11_111_0_00_00_1_000000000_10_00000_00000));
    }

    // ldr 32 reg_off
    public void test_ldr32_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldr_32_reg_off", "ldr", fmt,
                Integer.toHexString(0b10_111_0_00_01_1_000000000_10_00000_00000));
    }

    // ldr 64 reg_off
    public void test_ldr64_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldr_64_reg_off", "ldr", fmt,
                Integer.toHexString(0b11_111_0_00_01_1_000000000_10_00000_00000));
    }

    @Test
    public void test_load_store_reg_off_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT2;
        test_ldrsb32_reg_off(fmt);
        test_ldrsb64_reg_off(fmt);
        test_ldrsh32_reg_off(fmt);
        test_ldrsh64_reg_off(fmt);
        test_str32_reg_off(fmt);
        test_str64_reg_off(fmt);
        test_ldr32_reg_off(fmt);
        test_ldr64_reg_off(fmt);
    }

    // str simd 8 bit reg_off
    public void test_strsimd8_reg_off(ArmAsmFieldType fmt) {
        testDecode("str_8_simd_reg_off", "str", fmt,
                Integer.toHexString(0b00_111_1_00_00_1_000000000_10_00000_00000));
    }

    // str simd 16 bit reg_off
    public void test_strsimd16_reg_off(ArmAsmFieldType fmt) {
        testDecode("str_16_simd_reg_off", "str", fmt,
                Integer.toHexString(0b01_111_1_00_00_1_000000000_10_00000_00000));
    }

    // str simd 32 bit reg_off
    public void test_strsimd32_reg_off(ArmAsmFieldType fmt) {
        testDecode("str_32_simd_reg_off", "str", fmt,
                Integer.toHexString(0b10_111_1_00_00_1_000000000_10_00000_00000));
    }

    // str simd 64 bit reg_off
    public void test_strsimd64_reg_off(ArmAsmFieldType fmt) {
        testDecode("str_64_simd_reg_off", "str", fmt,
                Integer.toHexString(0b11_111_1_00_00_1_000000000_10_00000_00000));
    }

    // str simd 128 bit reg_off
    public void test_strsimd128_reg_off(ArmAsmFieldType fmt) {
        testDecode("str_128_simd_reg_off", "str", fmt,
                Integer.toHexString(0b00_111_1_00_10_1_000000000_10_00000_00000));
    }

    // ldr simd 8 bit reg_off
    public void test_ldrsimd8_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldr_8_simd_reg_off", "ldr", fmt,
                Integer.toHexString(0b00_111_1_00_01_1_000000000_10_00000_00000));
    }

    // ldr simd 16 bit reg_off
    public void test_ldrsimd16_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldr_16_simd_reg_off", "ldr", fmt,
                Integer.toHexString(0b01_111_1_00_01_1_000000000_10_00000_00000));
    }

    // ldr simd 32 bit reg_off
    public void test_ldrsimd32_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldr_32_simd_reg_off", "ldr", fmt,
                Integer.toHexString(0b10_111_1_00_01_1_000000000_10_00000_00000));
    }

    // ldr simd 64 bit reg_off
    public void test_ldrsimd64_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldr_64_simd_reg_off", "ldr", fmt,
                Integer.toHexString(0b11_111_1_00_01_1_000000000_10_00000_00000));
    }

    // ldr simd 128 bit reg_off
    public void test_ldrsimd128_reg_off(ArmAsmFieldType fmt) {
        testDecode("ldr_128_simd_reg_off", "ldr", fmt,
                Integer.toHexString(0b00_111_1_00_11_1_000000000_10_00000_00000));
    }

    @Test
    public void test_load_store_reg_off_fmt3() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_OFF_FMT3;
        test_strsimd8_reg_off(fmt);
        test_strsimd16_reg_off(fmt);
        test_strsimd32_reg_off(fmt);
        test_strsimd64_reg_off(fmt);
        test_strsimd128_reg_off(fmt);
        test_ldrsimd8_reg_off(fmt);
        test_ldrsimd16_reg_off(fmt);
        test_ldrsimd32_reg_off(fmt);
        test_ldrsimd64_reg_off(fmt);
        test_ldrsimd128_reg_off(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD_STORE_REG_UIMM ////////////////////////////////////////////////////
    // strb_uimm
    public void test_strb_uimm(ArmAsmFieldType fmt) {
        testDecode("strb_uimm", "strb", fmt,
                Integer.toHexString(0b00_111_0_01_00_0000000000000000000000));
    }

    // ldrb_uimm
    public void test_ldrb_uimm(ArmAsmFieldType fmt) {
        testDecode("ldrb_uimm", "ldrb", fmt,
                Integer.toHexString(0b00_111_0_01_01_0000000000000000000000));
    }

    // strh_uimm
    public void test_strh_uimm(ArmAsmFieldType fmt) {
        testDecode("strh_uimm", "strh", fmt,
                Integer.toHexString(0b01_111_0_01_00_0000000000000000000000));
    }

    // ldrh_uimm
    public void test_ldrh_uimm(ArmAsmFieldType fmt) {
        testDecode("ldrh_uimm", "ldrh", fmt,
                Integer.toHexString(0b01_111_0_01_01_0000000000000000000000));
    }

    // ldrsw_uimm
    public void test_ldrsw_uimm(ArmAsmFieldType fmt) {
        testDecode("ldrsw_uimm", "ldrsw", fmt,
                Integer.toHexString(0b10_111_0_01_10_0000000000000000000000));
    }

    // prfm_uimm
    public void test_prfm_uimm(ArmAsmFieldType fmt) {
        testDecode("prfm_uimm", "prfm", fmt,
                Integer.toHexString(0b11_111_0_01_10_0000000000000000000000));
    }

    @Test
    public void test_load_store_uimm_fmt1() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT1;
        test_strb_uimm(fmt);
        test_ldrb_uimm(fmt);
        test_strh_uimm(fmt);
        test_ldrh_uimm(fmt);
        test_ldrsw_uimm(fmt);
        test_prfm_uimm(fmt);
    }

    // ldrsb32_uimm
    public void test_ldrsb32_uimm(ArmAsmFieldType fmt) {
        testDecode("ldrsb32_uimm", "ldrsb", fmt,
                Integer.toHexString(0b00_111_0_01_11_0000000000000000000000));
    }

    // ldrsb64_uimm
    public void test_ldrsb64_uimm(ArmAsmFieldType fmt) {
        testDecode("ldrsb64_uimm", "ldrsb", fmt,
                Integer.toHexString(0b00_111_0_01_10_0000000000000000000000));
    }

    // ldrsh32_uimm
    public void test_ldrsh32_uimm(ArmAsmFieldType fmt) {
        testDecode("ldrsh32_uimm", "ldrsh", fmt,
                Integer.toHexString(0b01_111_0_01_11_0000000000000000000000));
    }

    // ldrsh64_uimm
    public void test_ldrsh64_uimm(ArmAsmFieldType fmt) {
        testDecode("ldrsh64_uimm", "ldrsh", fmt,
                Integer.toHexString(0b01_111_0_01_10_0000000000000000000000));
    }

    // str 32 bit reg_off
    public void test_str32_uimm(ArmAsmFieldType fmt) {
        testDecode("str_32_uimm", "str", fmt,
                Integer.toHexString(0b10_111_0_01_00_0000000000000000000000));
    }

    // str 64 reg_off
    public void test_str64_uimm(ArmAsmFieldType fmt) {
        testDecode("str_64_uimm", "str", fmt,
                Integer.toHexString(0b11_111_0_01_00_0000000000000000000000));
    }

    // ldr 32 reg_off
    public void test_ldr32_uimm(ArmAsmFieldType fmt) {
        testDecode("ldr_32_uimm", "ldr", fmt,
                Integer.toHexString(0b10_111_0_01_01_0000000000000000000000));
    }

    // ldr 64 reg_off
    public void test_ldr64_uimm(ArmAsmFieldType fmt) {
        testDecode("ldr_64_uimm", "ldr", fmt,
                Integer.toHexString(0b11_111_0_01_01_0000000000000000000000));
    }

    @Test
    public void test_load_store_uimm_fmt2() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT2;
        test_ldrsb32_uimm(fmt);
        test_ldrsb64_uimm(fmt);
        test_ldrsh32_uimm(fmt);
        test_ldrsh64_uimm(fmt);
        test_str32_uimm(fmt);
        test_str64_uimm(fmt);
        test_ldr32_uimm(fmt);
        test_ldr64_uimm(fmt);
    }

    // str simd 8 bit reg_off
    public void test_strsimd8_uimm(ArmAsmFieldType fmt) {
        testDecode("str_8_simd_uimm", "str", fmt,
                Integer.toHexString(0b00_111_1_01_00_0000000000000000000000));
    }

    // str simd 16 bit reg_off
    public void test_strsimd16_uimm(ArmAsmFieldType fmt) {
        testDecode("str_16_simd_uimm", "str", fmt,
                Integer.toHexString(0b01_111_1_01_00_0000000000000000000000));
    }

    // str simd 32 bit reg_off
    public void test_strsimd32_uimm(ArmAsmFieldType fmt) {
        testDecode("str_32_simd_uimm", "str", fmt,
                Integer.toHexString(0b10_111_1_01_00_0000000000000000000000));
    }

    // str simd 64 bit reg_off
    public void test_strsimd64_uimm(ArmAsmFieldType fmt) {
        testDecode("str_64_simd_uimm", "str", fmt,
                Integer.toHexString(0b11_111_1_01_00_0000000000000000000000));
    }

    // str simd 128 bit reg_off
    public void test_strsimd128_uimm(ArmAsmFieldType fmt) {
        testDecode("str_128_simd_uimm", "str", fmt,
                Integer.toHexString(0b00_111_1_01_10_0000000000000000000000));
    }

    // ldr simd 8 bit reg_off
    public void test_ldrsimd8_uimm(ArmAsmFieldType fmt) {
        testDecode("ldr_8_simd_uimm", "ldr", fmt,
                Integer.toHexString(0b00_111_1_01_01_0000000000000000000000));
    }

    // ldr simd 16 bit reg_off
    public void test_ldrsimd16_uimm(ArmAsmFieldType fmt) {
        testDecode("ldr_16_simd_uimm", "ldr", fmt,
                Integer.toHexString(0b01_111_1_01_01_0000000000000000000000));
    }

    // ldr simd 32 bit reg_off
    public void test_ldrsimd32_uimm(ArmAsmFieldType fmt) {
        testDecode("ldr_32_simd_uimm", "ldr", fmt,
                Integer.toHexString(0b10_111_1_01_01_0000000000000000000000));
    }

    // ldr simd 64 bit reg_off
    public void test_ldrsimd64_uimm(ArmAsmFieldType fmt) {
        testDecode("ldr_64_simd_uimm", "ldr", fmt,
                Integer.toHexString(0b11_111_1_01_01_0000000000000000000000));
    }

    // ldr simd 128 bit reg_off
    public void test_ldrsimd128_uimm(ArmAsmFieldType fmt) {
        testDecode("ldr_128_simd_uimm", "ldr", fmt,
                Integer.toHexString(0b00_111_1_01_11_0000000000000000000000));
    }

    @Test
    public void test_load_store_uimm_fmt3() {
        var fmt = ArmAsmFieldType.LOAD_STORE_REG_UIMM_FMT3;
        test_strsimd8_uimm(fmt);
        test_strsimd16_uimm(fmt);
        test_strsimd32_uimm(fmt);
        test_strsimd64_uimm(fmt);
        test_strsimd128_uimm(fmt);
        test_ldrsimd8_uimm(fmt);
        test_ldrsimd16_uimm(fmt);
        test_ldrsimd32_uimm(fmt);
        test_ldrsimd64_uimm(fmt);
        test_ldrsimd128_uimm(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // DPR_TWOSOURCE (C4-299) /////////////////////////////////////////////////
    // udiv32
    public void test_udiv32(ArmAsmFieldType fmt) {
        testDecode("udiv32", "udiv", fmt,
                Integer.toHexString(0b0_00_11010110_00000_000010_00000_00000));
    }

    // udiv64
    public void test_udiv64(ArmAsmFieldType fmt) {
        testDecode("udiv64", "udiv", fmt,
                Integer.toHexString(0b1_00_11010110_00000_000010_00000_00000));
    }

    // sdiv32
    public void test_sdiv32(ArmAsmFieldType fmt) {
        testDecode("sdiv32", "sdiv", fmt,
                Integer.toHexString(0b0_00_11010110_00000_000011_00000_00000));
    }

    // sdiv64
    public void test_sdiv64(ArmAsmFieldType fmt) {
        testDecode("sdiv64", "sdiv", fmt,
                Integer.toHexString(0b1_00_11010110_00000_000011_00000_00000));
    }

    // lslv32
    public void test_lslv32(ArmAsmFieldType fmt) {
        testDecode("lslv32", "lslv", fmt,
                Integer.toHexString(0b0_00_11010110_00000_001000_00000_00000));
    }

    // lslv64
    public void test_lslv64(ArmAsmFieldType fmt) {
        testDecode("lslv64", "lslv", fmt,
                Integer.toHexString(0b1_00_11010110_00000_001000_00000_00000));
    }

    // lsrv32
    public void test_lsrv32(ArmAsmFieldType fmt) {
        testDecode("lsrv32", "lsrv", fmt,
                Integer.toHexString(0b0_00_11010110_00000_001001_00000_00000));
    }

    // lsrv64
    public void test_lsrv64(ArmAsmFieldType fmt) {
        testDecode("lsrv64", "lsrv", fmt,
                Integer.toHexString(0b1_00_11010110_00000_001001_00000_00000));
    }

    // asrv32
    public void test_asrv32(ArmAsmFieldType fmt) {
        testDecode("asrv32", "asrv", fmt,
                Integer.toHexString(0b0_00_11010110_00000_001010_00000_00000));
    }

    // asrv64
    public void test_asrv64(ArmAsmFieldType fmt) {
        testDecode("asrv64", "asrv", fmt,
                Integer.toHexString(0b1_00_11010110_00000_001010_00000_00000));
    }

    // rorv32
    public void test_rorv32(ArmAsmFieldType fmt) {
        testDecode("rorv32", "rorv", fmt,
                Integer.toHexString(0b0_00_11010110_00000_001011_00000_00000));
    }

    // rorv64
    public void test_rorv64(ArmAsmFieldType fmt) {
        testDecode("rorv64", "rorv", fmt,
                Integer.toHexString(0b1_00_11010110_00000_001011_00000_00000));
    }

    // crc32b
    public void test_crc32b(ArmAsmFieldType fmt) {
        testDecode("crc32b", "crc32b", fmt,
                Integer.toHexString(0b0_00_11010110_00000_010000_00000_00000));
    }

    // crc32h
    public void test_crc32h(ArmAsmFieldType fmt) {
        testDecode("crc32h", "crc32h", fmt,
                Integer.toHexString(0b0_00_11010110_00000_010001_00000_00000));
    }

    // crc32w
    public void test_crc32w(ArmAsmFieldType fmt) {
        testDecode("crc32w", "crc32w", fmt,
                Integer.toHexString(0b0_00_11010110_00000_010010_00000_00000));
    }

    // crc32x
    public void test_crc32x(ArmAsmFieldType fmt) {
        testDecode("crc32x", "crc32x", fmt,
                Integer.toHexString(0b1_00_11010110_00000_010011_00000_00000));
    }

    // subp
    public void test_subp(ArmAsmFieldType fmt) {
        testDecode("subp", "subp", fmt,
                Integer.toHexString(0b1_00_11010110_00000_000000_00000_00000));
    }

    // irg
    public void test_irg(ArmAsmFieldType fmt) {
        testDecode("irg", "irg", fmt,
                Integer.toHexString(0b1_00_11010110_00000_000100_00000_00000));
    }

    // gmi
    public void test_gmi(ArmAsmFieldType fmt) {
        testDecode("gmi", "gmi", fmt,
                Integer.toHexString(0b1_00_11010110_00000_000101_00000_00000));
    }

    // pacga
    public void test_pacga(ArmAsmFieldType fmt) {
        testDecode("pacga", "pacga", fmt,
                Integer.toHexString(0b1_00_11010110_00000_001100_00000_00000));
    }

    // crc32cb
    public void test_crc32cb(ArmAsmFieldType fmt) {
        testDecode("crc32cb", "crc32cb", fmt,
                Integer.toHexString(0b0_00_11010110_00000_010100_00000_00000));
    }

    // crc32ch
    public void test_crc32ch(ArmAsmFieldType fmt) {
        testDecode("crc32ch", "crc32ch", fmt,
                Integer.toHexString(0b0_00_11010110_00000_010101_00000_00000));
    }

    // crc32cw
    public void test_crc32cw(ArmAsmFieldType fmt) {
        testDecode("crc32cw", "crc32cw", fmt,
                Integer.toHexString(0b0_00_11010110_00000_010110_00000_00000));
    }

    // crc32cx
    public void test_crc32cx(ArmAsmFieldType fmt) {
        testDecode("crc32cx", "crc32cx", fmt,
                Integer.toHexString(0b1_00_11010110_00000_010111_00000_00000));
    }

    // subps
    public void test_subps(ArmAsmFieldType fmt) {
        testDecode("subps", "subps", fmt,
                Integer.toHexString(0b1_01_11010110_00000_000000_00000_00000));
    }

    @Test
    public void test_DprTwoSource() {
        var fmt = ArmAsmFieldType.DPR_TWOSOURCE;
        test_udiv32(fmt);
        test_udiv64(fmt);
        test_sdiv32(fmt);
        test_sdiv64(fmt);
        test_lslv32(fmt);
        test_lslv64(fmt);
        test_lsrv32(fmt);
        test_lsrv64(fmt);
        test_asrv32(fmt);
        test_asrv64(fmt);
        test_rorv32(fmt);
        test_rorv64(fmt);
        test_crc32b(fmt);
        test_crc32h(fmt);
        test_crc32w(fmt);
        test_crc32x(fmt);
        test_subp(fmt);
        test_irg(fmt);
        test_gmi(fmt);
        test_pacga(fmt);
        test_crc32cb(fmt);
        test_crc32ch(fmt);
        test_crc32cw(fmt);
        test_crc32cx(fmt);
        test_subps(fmt);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // DPR_ONESOURCE (C4-301) /////////////////////////////////////////////////
    // rbit32
    public void test_rbit32(ArmAsmFieldType fmt) {
        testDecode("rbit32", "rbit", fmt,
                Integer.toHexString(0b0_10_11010110_00000_000000_00000_00000));
    }

    // rbit64
    public void test_rbit(ArmAsmFieldType fmt) {
        testDecode("rbit64", "rbit", fmt,
                Integer.toHexString(0b1_10_11010110_00000_000000_00000_00000));
    }

    // rev16 32 bit
    public void test_rev16_32(ArmAsmFieldType fmt) {
        testDecode("rev16_32", "rev16", fmt,
                Integer.toHexString(0b0_10_11010110_00000_000001_00000_00000));
    }

    // rev16 64 bit
    public void test_rev16_64(ArmAsmFieldType fmt) {
        testDecode("rev16_64", "rev16", fmt,
                Integer.toHexString(0b1_10_11010110_00000_000001_00000_00000));
    }

    // rev32
    public void test_rev32(ArmAsmFieldType fmt) {
        testDecode("rev32", "rev", fmt,
                Integer.toHexString(0b0_10_11010110_00000_000010_00000_00000));
    }

    // rev64
    public void test_rev64(ArmAsmFieldType fmt) {
        testDecode("rev64", "rev", fmt,
                Integer.toHexString(0b1_10_11010110_00000_000010_00000_00000));
    }

    // clz32
    public void test_clz32(ArmAsmFieldType fmt) {
        testDecode("clz32", "clz", fmt,
                Integer.toHexString(0b0_10_11010110_00000_000100_00000_00000));
    }

    // clz64
    public void test_clz64(ArmAsmFieldType fmt) {
        testDecode("clz64", "clz", fmt,
                Integer.toHexString(0b1_10_11010110_00000_000100_00000_00000));
    }

    // cls32
    public void test_cls32(ArmAsmFieldType fmt) {
        testDecode("cls32", "cls", fmt,
                Integer.toHexString(0b0_10_11010110_00000_000101_00000_00000));
    }

    // cls64
    public void test_cls64(ArmAsmFieldType fmt) {
        testDecode("cls64", "cls", fmt,
                Integer.toHexString(0b1_10_11010110_00000_000101_00000_00000));
    }

    @Test
    public void test_DprOneSource() {
        var fmt = ArmAsmFieldType.DPR_ONESOURCE;
        test_rbit32(fmt);
        test_rbit(fmt);
        test_rev16_32(fmt);
        test_rev16_64(fmt);
        test_rev32(fmt);
        test_rev64(fmt);
        test_clz32(fmt);
        test_clz64(fmt);
        test_cls32(fmt);
        test_cls64(fmt);
    }

    @Test
    public void testsingular() {
        testDecode("sbfm", "sbfm", ArmAsmFieldType.BITFIELD, "93407c05");

        // 0x1414:93407c05 unknown x5, x0, #0x0, #0xsp
    }

    ///////////////////////////////////////////////////////////////////////////
    private void testDecode(String testname,
            String expected, ArmAsmFieldType fmt, String binaryInstruction) {

        ArmInstruction testinst = ArmInstruction.newInstance("0", binaryInstruction);
        ArmInstructionData idata = testinst.getData();
        ArmAsmFieldData fieldData = testinst.getFieldData();

        var typecorrect = (fmt == testinst.getFieldData().getType());

        System.out.print(testname + "\tresolved to\t->\t" + testinst.getName() +
                "\t(SIMD: " + idata.isSimd() + ", width: " + idata.getBitWidth() + ")\t" +
                "type is correct: " + typecorrect + "\n");

        if (fmt != testinst.getFieldData().getType())
            System.out.print("\t\t" + fmt.toString() + "\tvs.\t" + testinst.getFieldData().getType() + "\n");

        assertEquals(expected, testinst.getName());
        assertEquals(fmt, testinst.getFieldData().getType());
    }
}
