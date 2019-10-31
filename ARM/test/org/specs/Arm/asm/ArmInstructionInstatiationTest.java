package org.specs.Arm.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.Arm.instruction.ArmInstruction;
import org.specs.Arm.instruction.ArmInstructionData;
import org.specs.Arm.parsing.ArmAsmFieldData;

public class ArmInstructionInstatiationTest {

    /*
     * Instantiate one of every single instruction in the ISA as specified by the manual:
     * https://static.docs.arm.com/ddi0487/ea/DDI0487E_a_armv8_arm.pdf
     * To see if they get correctly identified into the proper mnemonic
     */

    // DPI_PCREL //////////////////////////////////////////////////////////
    @Test
    public void test_Adr() {
        testDecode("adr", "adr", Integer.toHexString(0b000_10000_0000000000000000000_00000));
    }

    @Test
    public void test_Adrp() {
        testDecode("adrp", "adrp", Integer.toHexString(0b100_10000_0000000000000000000_00000));
    }

    @Test
    public void test__DPI_PCREL() {
        test_Adr();
        test_Adrp();
    }

    // DPI_ADDSUBIMM //////////////////////////////////////////////////////
    // 32 bit add
    @Test
    public void test_AddImm32() {
        testDecode("add_imm_32", "add", Integer.toHexString(0b000_100010_0_000000000000_00000_00000));
    }

    // 32 bit add w/set flag
    @Test
    public void test_AddsImm32() {
        testDecode("adds_imm_32", "adds", Integer.toHexString(0b001_100010_0_000000000000_00000_00000));
    }

    // 32 bit sub
    @Test
    public void test_SubImm32() {
        testDecode("sub_imm_32", "sub", Integer.toHexString(0b010_100010_0_000000000000_00000_00000));
    }

    // 32 bit sub w/set flag
    @Test
    public void test_SubsImm32() {
        testDecode("subs_imm_32", "subs", Integer.toHexString(0b011_100010_0_000000000000_00000_00000));
    }

    // 64 bit add
    @Test
    public void test_AddImm64() {
        testDecode("add_imm_64", "add", Integer.toHexString(0b100_100010_0_000000000000_00000_00000));
    }

    // 64 bit add w/set flag
    @Test
    public void test_AddsImm64() {
        testDecode("adds_imm_64", "adds", Integer.toHexString(0b101_100010_0_000000000000_00000_00000));
    }

    // 64 bit sub
    @Test
    public void test_SubImm64() {
        testDecode("sub_imm_64", "sub", Integer.toHexString(0b110_100010_0_000000000000_00000_00000));
    }

    // 64 bit sub w/set flag
    @Test
    public void test_SubsImm64() {
        testDecode("subs_imm_64", "subs", Integer.toHexString(0b111_100010_0_000000000000_00000_00000));
    }

    @Test
    public void test__DPI_ADDSUBIMM() {
        test_AddImm32();
        test_AddsImm32();
        test_SubImm32();
        test_SubsImm32();
        test_AddImm64();
        test_AddsImm64();
        test_SubImm64();
        test_SubsImm64();
    }

    // DPI_ADDSUBIMM_TAGS /////////////////////////////////////////////////
    // add with tags
    @Test
    public void test_Addg() {
        testDecode("addg", "addg", Integer.toHexString(0b100_100011_0_000000000000_00000_00000));
    }

    // sub with tags
    @Test
    public void test_Subg() {
        testDecode("subg", "subg", Integer.toHexString(0b110_100011_0_000000000000_00000_00000));
    }

    @Test
    public void test__DPI_ADDSUBIMM_TAGS() {
        test_Addg();
        test_Subg();
    }

    // LOGICAL ////////////////////////////////////////////////////////////
    // logical and immediate 32 bits
    @Test
    public void test_AndImm32() {
        testDecode("and_imm_32", "and", Integer.toHexString(0b000_100100_0_000000000000_00000_00000));
    }

    // logical orr immediate 32 bits
    @Test
    public void test_OrrImm32() {
        testDecode("orr_imm_32", "orr", Integer.toHexString(0b001_100100_0_000000000000_00000_00000));
    }

    // logical eor immediate 32 bits
    @Test
    public void test_EorImm32() {
        testDecode("eor_imm_32", "eor", Integer.toHexString(0b010_100100_0_000000000000_00000_00000));
    }

    // logical ands immediate 32 bits
    @Test
    public void test_AndsImm32() {
        testDecode("ands_imm_32", "ands", Integer.toHexString(0b011_100100_0_000000000000_00000_00000));
    }

    // logical and immediate 64 bits
    @Test
    public void test_AndImm64() {
        testDecode("and_imm_64", "and", Integer.toHexString(0b100_100100_0_000000000000_00000_00000));
    }

    // logical orr immediate 64 bits
    @Test
    public void test_OrrImm64() {
        testDecode("orr_imm_64", "orr", Integer.toHexString(0b101_100100_0_000000000000_00000_00000));
    }

    // logical eor immediate 64 bits
    @Test
    public void test_EorImm64() {
        testDecode("eor_imm_64", "eor", Integer.toHexString(0b110_100100_0_000000000000_00000_00000));
    }

    // logical ands immediate 64 bits
    @Test
    public void test_AndsImm64() {
        testDecode("ands_imm_64", "ands", Integer.toHexString(0b111_100100_0_000000000000_00000_00000));
    }

    @Test
    public void test__LOGICAL() {
        test_AndImm32();
        test_OrrImm32();
        test_EorImm32();
        test_AndsImm32();
        test_AndImm64();
        test_OrrImm64();
        test_EorImm64();
        test_AndsImm64();
    }

    // MOVEW //////////////////////////////////////////////////////////////////
    // moven 32 bit
    @Test
    public void test_Movn32() {
        testDecode("movn_imm_32", "movn", Integer.toHexString(0b000_100101_0_000000000000_00000_00000));
    }

    // movez 32 bit
    @Test
    public void test_Movz32() {
        testDecode("movz_imm_32", "movz", Integer.toHexString(0b010_100101_0_000000000000_00000_00000));
    }

    // movek 32 bit
    @Test
    public void test_Movk32() {
        testDecode("movk_imm_32", "movk", Integer.toHexString(0b011_100101_0_000000000000_00000_00000));
    }

    // moven 64 bit
    @Test
    public void test_Movn64() {
        testDecode("movn_imm_64", "movn", Integer.toHexString(0b100_100101_0_000000000000_00000_00000));
    }

    // movez 64 bit
    @Test
    public void test_Movz64() {
        testDecode("movz_imm_64", "movz", Integer.toHexString(0b110_100101_0_000000000000_00000_00000));
    }

    // movek 64 bit
    @Test
    public void test_Movk64() {
        testDecode("movk_imm_64", "movk", Integer.toHexString(0b111_100101_0_000000000000_00000_00000));
    }

    // BITFIELD ///////////////////////////////////////////////////////////////
    // sbfm 32 bit
    @Test
    public void test_Sbfm32() {
        testDecode("sbfm_imm_32", "sbfm", Integer.toHexString(0b000_100110_0_000000000000_00000_00000));
    }

    // bfm 32 bit
    @Test
    public void test_Bfm32() {
        testDecode("bfm_imm_32", "bfm", Integer.toHexString(0b001_100110_0_000000000000_00000_00000));
    }

    // ubfm 32 bit
    @Test
    public void test_Ubfm32() {
        testDecode("ubfm_imm_32", "ubfm", Integer.toHexString(0b010_100110_0_000000000000_00000_00000));
    }

    // sbfm 64 bit
    @Test
    public void test_Sbfm64() {
        testDecode("sbfm_imm_64", "sbfm", Integer.toHexString(0b100_100110_0_000000000000_00000_00000));
    }

    // bfm 64 bit
    @Test
    public void test_Bfm64() {
        testDecode("bfm_imm_64", "bfm", Integer.toHexString(0b101_100110_0_000000000000_00000_00000));
    }

    // ubfm 64 bit
    @Test
    public void test_Ubfm64() {
        testDecode("ubfm_imm_64", "ubfm", Integer.toHexString(0b110_100110_0_000000000000_00000_00000));
    }

    // EXTRACT ////////////////////////////////////////////////////////////////
    // extr 32 bit
    @Test
    public void test_Extr32() {
        testDecode("extr_imm_32", "extr", Integer.toHexString(0b000_100111_0_000000000000_00000_00000));
    }

    // extr 64 bit
    @Test
    public void test_Extr64() {
        testDecode("extr_imm_64", "extr", Integer.toHexString(0b100_100111_1_000000000000_00000_00000));
    }

    // CONDITIONALBRANCH //////////////////////////////////////////////////////
    // b.eq
    @Test
    public void test_Beq() {
        testDecode("b.eq", "b.eq", Integer.toHexString(0b0101_0100_0000000000000000000_0_0000));
    }

    // b.ne
    @Test
    public void test_Bne() {
        testDecode("b.ne", "b.ne", Integer.toHexString(0b0101_0100_0000000000000000000_0_0001));
    }

    // b.cs
    @Test
    public void test_Bcs() {
        testDecode("b.cs", "b.cs", Integer.toHexString(0b0101_0100_0000000000000000000_0_0010));
    }

    // b.cc
    @Test
    public void test_Bcc() {
        testDecode("b.cc", "b.cc", Integer.toHexString(0b0101_0100_0000000000000000000_0_0011));
    }

    // b.mn
    @Test
    public void test_Bmn() {
        testDecode("b.mn", "b.mn", Integer.toHexString(0b0101_0100_0000000000000000000_0_0100));
    }

    // b.pl
    @Test
    public void test_Bpl() {
        testDecode("b.pl", "b.pl", Integer.toHexString(0b0101_0100_0000000000000000000_0_0101));
    }

    // b.vs
    @Test
    public void test_Bvs() {
        testDecode("b.vs", "b.vs", Integer.toHexString(0b0101_0100_0000000000000000000_0_0110));
    }

    // b.vc
    @Test
    public void test_Bvc() {
        testDecode("b.vc", "b.vc", Integer.toHexString(0b0101_0100_0000000000000000000_0_0111));
    }

    // b.hi
    @Test
    public void test_Bhi() {
        testDecode("b.hi", "b.hi", Integer.toHexString(0b0101_0100_0000000000000000000_0_1000));
    }

    // b.ls
    @Test
    public void test_Bls() {
        testDecode("b.ls", "b.ls", Integer.toHexString(0b0101_0100_0000000000000000000_0_1001));
    }

    // b.ge
    @Test
    public void test_Bge() {
        testDecode("b.ge", "b.ge", Integer.toHexString(0b0101_0100_0000000000000000000_0_1010));
    }

    // b.lt
    @Test
    public void test_Blt() {
        testDecode("b.lt", "b.lt", Integer.toHexString(0b0101_0100_0000000000000000000_0_1011));
    }

    // b.gt
    @Test
    public void test_Blgt() {
        testDecode("b.gt", "b.gt", Integer.toHexString(0b0101_0100_0000000000000000000_0_1100));
    }

    // b.le
    @Test
    public void test_Ble() {
        testDecode("b.le", "b.le", Integer.toHexString(0b0101_0100_0000000000000000000_0_1101));
    }

    // b.al
    @Test
    public void test_Bal() {
        testDecode("b.al", "b.al", Integer.toHexString(0b0101_0100_0000000000000000000_0_1110));

        // test with positive relative target (+12) (0x0C)
        testDecode("240", "b.al", "b.al", Integer.toHexString(0b0101_0100_0000000000000001100_0_1110));

        // test with negative relative target (-36) (0x24)
        testDecode("434", "b.al", "b.al", Integer.toHexString(0b0101_0100_1111111111111011100_0_1110));
    }

    // b.nvb
    @Test
    public void test_Bnvb() {
        testDecode("b.nvb", "b.nvb", Integer.toHexString(0b0101_0100_0000000000000000000_0_1111));
    }

    // EXCEPTION //////////////////////////////////////////////////////////////
    // svc
    @Test
    public void test_Svc() {
        testDecode("svc", "svc", Integer.toHexString(0b1101_0100_000_0000000000000000_000_01));
    }

    // hvc
    @Test
    public void test_Hvc() {
        testDecode("hvc", "hvc", Integer.toHexString(0b1101_0100_000_0000000000000000_000_10));
    }

    // smc
    @Test
    public void test_Smc() {
        testDecode("smc", "smc", Integer.toHexString(0b1101_0100_000_0000000000000000_000_11));
    }

    // brk
    @Test
    public void test_Brk() {
        testDecode("brk", "brk", Integer.toHexString(0b1101_0100_001_0000000000000000_000_00));
    }

    // hlt
    @Test
    public void test_Hlt() {
        testDecode("hlt", "hlt", Integer.toHexString(0b1101_0100_010_0000000000000000_000_00));
    }

    // dcps1
    @Test
    public void test_Dcps1() {
        testDecode("dcps1", "dcps1", Integer.toHexString(0b1101_0100_101_0000000000000000_000_01));
    }

    // dcps2
    @Test
    public void test_Dcps2() {
        testDecode("dcps2", "dcps2", Integer.toHexString(0b1101_0100_101_0000000000000000_000_10));
    }

    // dcps3
    @Test
    public void test_Dcps3() {
        testDecode("dcps3", "dcps3", Integer.toHexString(0b1101_0100_101_0000000000000000_000_11));
    }

    // UNCONDITIONAL BRANCH (IMMEDIATE) ///////////////////////////////////////
    // br
    @Test
    public void test_Br() {
        testDecode("br", "br", Integer.toHexString(0b1101_011_0000_11111_000000_00000_00000));
    }

    // braaz
    @Test
    public void test_Braaz() {
        testDecode("braaz", "braaz", Integer.toHexString(0b1101_011_0000_11111_000010_00000_00000));
    }

    // braa
    @Test
    public void test_Braa() {
        testDecode("braa", "braa", Integer.toHexString(0b1101_011_1000_11111_000010_00000_00000));
    }

    // brabz
    @Test
    public void test_Brabz() {
        testDecode("brabz", "brabz", Integer.toHexString(0b1101_011_0000_11111_000011_00000_00000));
    }

    // brab
    @Test
    public void test_Brab() {
        testDecode("brab", "brab", Integer.toHexString(0b1101_011_1000_11111_000011_00000_00000));
    }

    // blraaz
    @Test
    public void test_Blraaz() {
        testDecode("blraaz", "blraaz", Integer.toHexString(0b1101_011_0001_11111_000010_00000_00000));
    }

    // braa
    @Test
    public void test_Blraa() {
        testDecode("blraa", "blraa", Integer.toHexString(0b1101_011_1001_11111_000010_00000_00000));
    }

    // blrabz
    @Test
    public void test_Blrabz() {
        testDecode("blrabz", "blrabz", Integer.toHexString(0b1101_011_0001_11111_000011_00000_00000));
    }

    // blrab
    @Test
    public void test_Blrab() {
        testDecode("blrab", "blrab", Integer.toHexString(0b1101_011_1001_11111_000011_00000_00000));
    }

    // UNCONDITIONAL BRANCH (IMMEDIATE) (C4-264 to C4-265) ////////////////////
    // b
    @Test
    public void test_B() {
        testDecode("b", "b", Integer.toHexString(0b0_00101_00000000000000000000000000));
    }

    // COMPARE AND BRANCH (MMEDIATE) (C4-265) /////////////////////////////////
    // cbz 32 bits
    @Test
    public void test_Cbz32() {
        testDecode("cbz32", "cbz", Integer.toHexString(0b0_011010_0_000000000000000000000000));
    }

    // cbz 64 bits
    @Test
    public void test_Cbz64() {
        testDecode("cbz64", "cbz", Integer.toHexString(0b1_011010_0_000000000000000000000000));
    }

    // cbnz 32 bits
    @Test
    public void test_Cbnz32() {
        testDecode("cbnz32", "cbnz", Integer.toHexString(0b0_011010_1_000000000000000000000000));
    }

    // cbnz 64 bits
    @Test
    public void test_Cbnz64() {
        testDecode("cbnz64", "cbnz", Integer.toHexString(0b1_011010_1_000000000000000000000000));
    }

    // TEST_AND_BRANCH (C4-265) ///////////////////////////////////////////////
    // tbz
    @Test
    public void test_Tbz() {
        testDecode("tbz", "tbz", Integer.toHexString(0b0_011011_0_000000000000000000000000));
    }

    // tbnz
    @Test
    public void test_Tbnz() {
        testDecode("tbnz", "tbnz", Integer.toHexString(0b0_011011_1_000000000000000000000000));
    }

    // LOAD_REG_LITERAL (C4-280) //////////////////////////////////////////////
    // ldr literal 32 bits
    @Test
    public void test_ldr32literal() {
        testDecode("ldr_32_literal", "ldr", Integer.toHexString(0b00_011_000_0000000000000000000_00000));
    }

    // ldr literal 64 bits
    @Test
    public void test_ldr64literal() {
        testDecode("ldr_64_literal", "ldr", Integer.toHexString(0b01_011_000_0000000000000000000_00000));
    }

    // ldr literal simd 32 bits
    @Test
    public void test_ldr32literalsimd() {
        testDecode("ldr_32_literal_simd", "ldr", Integer.toHexString(0b00_011_100_0000000000000000000_00000));
    }

    // ldr literal simd 64 bits
    @Test
    public void test_ldr64literalsimd() {
        testDecode("ldr_64_literal_simd", "ldr", Integer.toHexString(0b01_011_100_0000000000000000000_00000));
    }

    // ldr literal simd 128 bits
    @Test
    public void test_ldr128literalsimd() {
        testDecode("ldr_128_literal_simd", "ldr", Integer.toHexString(0b10_011_100_0000000000000000000_00000));
    }

    // LOAD_STORE_PAIR (LOAD_STORE_NOALLOC) (C4-280 to C4-281) ////////////////
    // stnp 32
    @Test
    public void test_stnp32() {
        testDecode("stnp_32", "stnp", Integer.toHexString(0b00_101_0_000_0_0000000_00000_00000_00000));
    }

    // stnp 64
    @Test
    public void test_stnp64() {
        testDecode("stnp_64", "stnp", Integer.toHexString(0b10_101_0_000_0_0000000_00000_00000_00000));
    }

    // stnp 32 simd
    @Test
    public void test_stnp32simd() {
        testDecode("stnp_32_simd", "stnp", Integer.toHexString(0b00_101_1_000_0_0000000_00000_00000_00000));
    }

    // stnp 64 simd
    @Test
    public void test_stnp64simd() {
        testDecode("stnp_64_simd", "stnp", Integer.toHexString(0b01_101_1_000_0_0000000_00000_00000_00000));
    }

    // stnp 128 simd
    @Test
    public void test_stnp128simd() {
        testDecode("stnp_128_simd", "stnp", Integer.toHexString(0b10_101_1_000_0_0000000_00000_00000_00000));
    }

    // ldnp 32
    @Test
    public void test_ldnp32() {
        testDecode("ldnp_32", "ldnp", Integer.toHexString(0b00_101_0_000_1_0000000_00000_00000_00000));
    }

    // ldnp 64
    @Test
    public void test_ldnp64() {
        testDecode("ldnp_64", "ldnp", Integer.toHexString(0b10_101_0_000_1_0000000_00000_00000_00000));
    }

    // ldnp 32 simd
    @Test
    public void test_ldnp32simd() {
        testDecode("ldnp_32_simd", "ldnp", Integer.toHexString(0b00_101_1_000_1_0000000_00000_00000_00000));
    }

    // ldnp 64 simd
    @Test
    public void test_ldnp64simd() {
        testDecode("ldnp_64_simd", "ldnp", Integer.toHexString(0b01_101_1_000_1_0000000_00000_00000_00000));
    }

    // ldnp 128 simd
    @Test
    public void test_ldnp128simd() {
        testDecode("ldnp_128_simd", "ldnp", Integer.toHexString(0b00_101_0_000_1_0000000_00000_00000_00000));
    }

    // LOAD_STORE_PAIR (POST INDEXED) (C4-281 to C4-282) //////////////////////
    // stp 32
    @Test
    public void test_stp32post() {
        testDecode("stp_32_post", "stp", Integer.toHexString(0b00_101_0_001_0_0000000_00000_00000_00000));
    }

    // stp 64
    @Test
    public void test_stp64post() {
        testDecode("stp_64_post", "stp", Integer.toHexString(0b10_101_0_001_0_0000000_00000_00000_00000));
    }

    // stp 32 simd
    @Test
    public void test_stp32simdpost() {
        testDecode("stp_32_simd_post", "stp", Integer.toHexString(0b00_101_1_001_0_0000000_00000_00000_00000));
    }

    // stp 64 simd
    @Test
    public void test_stp64simdpost() {
        testDecode("stp_64_simd_post", "stp", Integer.toHexString(0b01_101_1_001_0_0000000_00000_00000_00000));
    }

    // stp 128 simd
    @Test
    public void test_stp128simdpost() {
        testDecode("stp_128_simd_post", "stp", Integer.toHexString(0b10_101_1_001_0_0000000_00000_00000_00000));
    }

    // ldp 32
    @Test
    public void test_ldp32post() {
        testDecode("ldp_32_post", "ldp", Integer.toHexString(0b00_101_0_001_1_0000000_00000_00000_00000));
    }

    // ldp 64
    @Test
    public void test_ldp64post() {
        testDecode("ldp_64_post", "ldp", Integer.toHexString(0b10_101_0_001_1_0000000_00000_00000_00000));
    }

    // ldp 32 simd
    @Test
    public void test_ldp32simdpost() {
        testDecode("ldp_32_simd_post", "ldp", Integer.toHexString(0b00_101_1_001_1_0000000_00000_00000_00000));
    }

    // ldp 64 simd
    @Test
    public void test_ldp64simdpost() {
        testDecode("ldp_64_simd_post", "ldp", Integer.toHexString(0b01_101_1_001_1_0000000_00000_00000_00000));
    }

    // ldp 128 simd
    @Test
    public void test_ldp128simdpost() {
        testDecode("ldp_128_simd_post", "ldp", Integer.toHexString(0b00_101_0_001_1_0000000_00000_00000_00000));
    }

    // LOAD_STORE_PAIR (PRE INDEXED) (C4-281 to C4-282) //////////////////////
    // stp 32
    @Test
    public void test_stp32pre() {
        testDecode("stp_32_pre", "stp", Integer.toHexString(0b00_101_0_011_0_0000000_00000_00000_00000));
    }

    // stp 64
    @Test
    public void test_stp64pre() {
        testDecode("stp_64_pre", "stp", Integer.toHexString(0b10_101_0_011_0_0000000_00000_00000_00000));
    }

    // stp 32 simd
    @Test
    public void test_stp32simdpre() {
        testDecode("stp_32_simd_pre", "stp", Integer.toHexString(0b00_101_1_011_0_0000000_00000_00000_00000));
    }

    // stp 64 simd
    @Test
    public void test_stp64simdpre() {
        testDecode("stp_64_simd_pre", "stp", Integer.toHexString(0b01_101_1_011_0_0000000_00000_00000_00000));
    }

    // stp 128 simd
    @Test
    public void test_stp128simdpre() {
        testDecode("stp_128_simd_pre", "stp", Integer.toHexString(0b10_101_1_011_0_0000000_00000_00000_00000));
    }

    // ldp 32
    @Test
    public void test_ldp32pre() {
        testDecode("ldp_32_pre", "ldp", Integer.toHexString(0b00_101_0_011_1_0000000_00000_00000_00000));
    }

    // ldp 64
    @Test
    public void test_ldp64pre() {
        testDecode("ldp_64_pre", "ldp", Integer.toHexString(0b10_101_0_011_1_0000000_00000_00000_00000));
    }

    // ldp 32 simd
    @Test
    public void test_ldp32simdpre() {
        testDecode("ldp_32_simd_pre", "ldp", Integer.toHexString(0b00_101_1_011_1_0000000_00000_00000_00000));
    }

    // ldp 64 simd
    @Test
    public void test_ldp64simdpre() {
        testDecode("ldp_64_simd_pre", "ldp", Integer.toHexString(0b01_101_1_011_1_0000000_00000_00000_00000));
    }

    // ldp 128 simd
    @Test
    public void test_ldp128simdpre() {
        testDecode("ldp_128_simd_pre", "ldp", Integer.toHexString(0b00_101_0_011_1_0000000_00000_00000_00000));
    }

    // LOAD_STORE_PAIR_OFFSET (C4-281 to C4-282) //////////////////////
    // stp 32
    @Test
    public void test_stp32off() {
        testDecode("stp_32_off", "stp", Integer.toHexString(0b00_101_0_010_0_0000000_00000_00000_00000));
    }

    // stp 64
    @Test
    public void test_stp64off() {
        testDecode("stp_64_off", "stp", Integer.toHexString(0b10_101_0_010_0_0000000_00000_00000_00000));
    }

    // stp 32 simd
    @Test
    public void test_stp32simdoff() {
        testDecode("stp_32_simd_off", "stp", Integer.toHexString(0b00_101_1_010_0_0000000_00000_00000_00000));
    }

    // stp 64 simd
    @Test
    public void test_stp64simdoff() {
        testDecode("stp_64_simd_off", "stp", Integer.toHexString(0b01_101_1_010_0_0000000_00000_00000_00000));
    }

    // stp 128 simd
    @Test
    public void test_stp128simdoff() {
        testDecode("stp_128_simd_off", "stp", Integer.toHexString(0b10_101_1_010_0_0000000_00000_00000_00000));
    }

    // ldp 32
    @Test
    public void test_ldp32off() {
        testDecode("ldp_32_off", "ldp", Integer.toHexString(0b00_101_0_010_1_0000000_00000_00000_00000));
    }

    // ldp 64
    @Test
    public void test_ldp64off() {
        testDecode("ldp_64_off", "ldp", Integer.toHexString(0b10_101_0_010_1_0000000_00000_00000_00000));
    }

    // ldp 32 simd
    @Test
    public void test_ldp32simdoff() {
        testDecode("ldp_32_simd_off", "ldp", Integer.toHexString(0b00_101_1_010_1_0000000_00000_00000_00000));
    }

    // ldp 64 simd
    @Test
    public void test_ldp64simdoff() {
        testDecode("ldp_64_simd_off", "ldp", Integer.toHexString(0b01_101_1_010_1_0000000_00000_00000_00000));
    }

    // ldp 128 simd
    @Test
    public void test_ldp128simdoff() {
        testDecode("ldp_128_simd_off", "ldp", Integer.toHexString(0b00_101_0_010_1_0000000_00000_00000_00000));
    }

    // LOAD_STORE_PAIR_IMM_UNSCALED (C4-283 to C4-284) ///////////////////
    // sturb
    @Test
    public void test_sturb() {
        testDecode("sturb", "sturb", Integer.toHexString(0b00_111_0_00_00_0_000000000_00_00000_00000));
    }

    // ldurb
    @Test
    public void test_ldurb() {
        testDecode("ldurb", "ldurb", Integer.toHexString(0b00_111_0_00_01_0_000000000_00_00000_00000));
    }

    // sturh
    @Test
    public void test_sturh() {
        testDecode("sturh", "sturh", Integer.toHexString(0b01_111_0_00_00_0_000000000_00_00000_00000));
    }

    // ldurh
    @Test
    public void test_ldurh() {
        testDecode("ldurh", "ldurh", Integer.toHexString(0b01_111_0_00_01_0_000000000_00_00000_00000));
    }

    // ldursb 32 bit
    @Test
    public void test_ldursb32() {
        testDecode("ldursb32", "ldursb", Integer.toHexString(0b00_111_0_00_11_0_000000000_00_00000_00000));
    }

    // ldursb 64 bit
    @Test
    public void test_ldursb64() {
        testDecode("ldursb64", "ldursb", Integer.toHexString(0b00_111_0_00_10_0_000000000_00_00000_00000));
    }

    // ldursh 32 bit
    @Test
    public void test_ldursh32() {
        testDecode("ldursh32", "ldursh", Integer.toHexString(0b01_111_0_00_11_0_000000000_00_00000_00000));
    }

    // ldursh 64 bit
    @Test
    public void test_ldursh64() {
        testDecode("ldursh64", "ldursh", Integer.toHexString(0b01_111_0_00_10_0_000000000_00_00000_00000));
    }

    // stur 32 bit
    @Test
    public void test_stur32() {
        testDecode("stur_32", "stur", Integer.toHexString(0b10_111_0_00_00_0_000000000_00_00000_00000));
    }

    // stur 64 bit
    @Test
    public void test_stur64() {
        testDecode("stur_64", "stur", Integer.toHexString(0b11_111_0_00_00_0_000000000_00_00000_00000));
    }

    // ldur 32 bit
    @Test
    public void test_ldur32() {
        testDecode("ldur_32", "ldur", Integer.toHexString(0b10_111_0_00_01_0_000000000_00_00000_00000));
    }

    // ldur 64 bit
    @Test
    public void test_ldur64() {
        testDecode("ldur_64", "ldur", Integer.toHexString(0b11_111_0_00_01_0_000000000_00_00000_00000));
    }

    // stur simd 8 bit
    @Test
    public void test_stursimd8() {
        testDecode("stur_8_simd", "stur", Integer.toHexString(0b00_111_1_00_00_0_000000000_00_00000_00000));
    }

    // stur simd 16 bit
    @Test
    public void test_stursimd16() {
        testDecode("stur_16_simd", "stur", Integer.toHexString(0b01_111_1_00_00_0_000000000_00_00000_00000));
    }

    // stur simd 32 bit
    @Test
    public void test_stursimd32() {
        testDecode("stur_32_simd", "stur", Integer.toHexString(0b10_111_1_00_00_0_000000000_00_00000_00000));
    }

    // stur simd 64 bit
    @Test
    public void test_stursimd64() {
        testDecode("stur_64_simd", "stur", Integer.toHexString(0b11_111_1_00_00_0_000000000_00_00000_00000));
    }

    // stur simd 128 bit
    @Test
    public void test_stursimd128() {
        testDecode("stur_128_simd", "stur", Integer.toHexString(0b00_111_1_00_10_0_000000000_00_00000_00000));
    }

    // ldur simd 8 bit
    @Test
    public void test_ldursimd8() {
        testDecode("ldur_8_simd", "ldur", Integer.toHexString(0b00_111_1_00_01_0_000000000_00_00000_00000));
    }

    // ldur simd 16 bit
    @Test
    public void test_ldursimd16() {
        testDecode("ldur_16_simd", "ldur", Integer.toHexString(0b01_111_1_00_01_0_000000000_00_00000_00000));
    }

    // ldur simd 32 bit
    @Test
    public void test_ldursimd32() {
        testDecode("ldur_32_simd", "ldur", Integer.toHexString(0b10_111_1_00_01_0_000000000_00_00000_00000));
    }

    // ldur simd 64 bit
    @Test
    public void test_ldursimd64() {
        testDecode("ldur_64_simd", "ldur", Integer.toHexString(0b11_111_1_00_01_0_000000000_00_00000_00000));
    }

    // ldur simd 128 bit
    @Test
    public void test_ldursimd128() {
        testDecode("ldur_128_simd", "ldur", Integer.toHexString(0b00_111_1_00_11_0_000000000_00_00000_00000));
    }

    // LOAD_STORE_PAIR_UNPRIV (C4-286) ////////////////////////////////////////
    // sttrb
    @Test
    public void test_sttrb() {
        testDecode("sttrb", "sttrb", Integer.toHexString(0b00_111_0_00_00_0_000000000_10_00000_00000));
    }

    // ldtrb
    @Test
    public void test_ldtrb() {
        testDecode("ldtrb", "ldtrb", Integer.toHexString(0b00_111_0_00_01_0_000000000_10_00000_00000));
    }

    // sttrh
    @Test
    public void test_sttrh() {
        testDecode("sttrh", "sttrh", Integer.toHexString(0b01_111_0_00_00_0_000000000_10_00000_00000));
    }

    // ldtrh
    @Test
    public void test_ldtrh() {
        testDecode("ldtrh", "ldtrh", Integer.toHexString(0b01_111_0_00_01_0_000000000_10_00000_00000));
    }

    // ldtrsb 32 bit
    @Test
    public void test_ldtrsb32() {
        testDecode("ldtrsb32", "ldtrsb", Integer.toHexString(0b00_111_0_00_11_0_000000000_10_00000_00000));
    }

    // ldtrsb 64 bit
    @Test
    public void test_ldtrsb64() {
        testDecode("ldtrsb64", "ldtrsb", Integer.toHexString(0b00_111_0_00_10_0_000000000_10_00000_00000));
    }

    // ldtrsh 32 bit
    @Test
    public void test_ldtrsh32() {
        testDecode("ldtrsh32", "ldtrsh", Integer.toHexString(0b01_111_0_00_11_0_000000000_10_00000_00000));
    }

    // ldtrsh 64 bit
    @Test
    public void test_ldtrsh64() {
        testDecode("ldtrsh64", "ldtrsh", Integer.toHexString(0b01_111_0_00_10_0_000000000_10_00000_00000));
    }

    // sttr 32 bit
    @Test
    public void test_sttr32() {
        testDecode("sttr_32", "sttr", Integer.toHexString(0b10_111_0_00_00_0_000000000_10_00000_00000));
    }

    // sttr 64 bit
    @Test
    public void test_sttr64() {
        testDecode("sttr_64", "sttr", Integer.toHexString(0b11_111_0_00_00_0_000000000_10_00000_00000));
    }

    // ldtr 32 bit
    @Test
    public void test_ldtr32() {
        testDecode("ldtr_32", "ldtr", Integer.toHexString(0b10_111_0_00_01_0_000000000_10_00000_00000));
    }

    // ldtr 64 bit
    @Test
    public void test_ldtr64() {
        testDecode("ldtr_64", "ldtr", Integer.toHexString(0b11_111_0_00_01_0_000000000_10_00000_00000));
    }

    // LOAD_STORE_PAIR_IMM_PREPOST_FMT1 (C4-284 to C4-285 and C4-286 to C4-287)
    // strb
    @Test
    public void test_strb_pre() {
        testDecode("strb_pre", "strb", Integer.toHexString(0b00_111_0_00_00_0_000000000_11_00000_00000));
    }

    // ldrb
    @Test
    public void test_ldrb_pre() {
        testDecode("ldrb_pre", "ldrb", Integer.toHexString(0b00_111_0_00_01_0_000000000_11_00000_00000));
    }

    // strh
    @Test
    public void test_strh_pre() {
        testDecode("strh_pre", "strh", Integer.toHexString(0b01_111_0_00_00_0_000000000_11_00000_00000));
    }

    // ldrh
    @Test
    public void test_ldrh_pre() {
        testDecode("ldrh_pre", "ldrh", Integer.toHexString(0b01_111_0_00_01_0_000000000_11_00000_00000));
    }

    // ldrsb 32 bit
    @Test
    public void test_ldrsb32_pre() {
        testDecode("ldrsb32_pre", "ldrsb", Integer.toHexString(0b00_111_0_00_11_0_000000000_11_00000_00000));
    }

    // ldrsb 64 bit
    @Test
    public void test_ldrsb64_pre() {
        testDecode("ldrsb64_pre", "ldrsb", Integer.toHexString(0b00_111_0_00_10_0_000000000_11_00000_00000));
    }

    // ldrsh 32 bit
    @Test
    public void test_ldrsh32_pre() {
        testDecode("ldrsh32_pre", "ldrsh", Integer.toHexString(0b01_111_0_00_11_0_000000000_11_00000_00000));
    }

    // ldrsh 64 bit
    @Test
    public void test_ldrsh64_pre() {
        testDecode("ldrsh64_pre", "ldrsh", Integer.toHexString(0b01_111_0_00_10_0_000000000_11_00000_00000));
    }

    // str 32 bit
    @Test
    public void test_str32_pre() {
        testDecode("str_32_pre", "str", Integer.toHexString(0b10_111_0_00_00_0_000000000_11_00000_00000));
    }

    // str 64 bit
    @Test
    public void test_str64_pre() {
        testDecode("str_64_pre", "str", Integer.toHexString(0b11_111_0_00_00_0_000000000_11_00000_00000));
    }

    // ldr 32 bit
    @Test
    public void test_ldr32_pre() {
        testDecode("ldr_32_pre", "ldr", Integer.toHexString(0b10_111_0_00_01_0_000000000_11_00000_00000));
    }

    // ldr 64 bit
    @Test
    public void test_ldr64_pre() {
        testDecode("ldr_64_pre", "ldr", Integer.toHexString(0b11_111_0_00_01_0_000000000_11_00000_00000));
    }

    // str simd 8 bit
    @Test
    public void test_strsimd8_pre() {
        testDecode("str_8_simd_pre", "str", Integer.toHexString(0b00_111_1_00_00_0_000000000_11_00000_00000));
    }

    // str simd 16 bit
    @Test
    public void test_strsimd16_pre() {
        testDecode("str_16_simd_pre", "str", Integer.toHexString(0b01_111_1_00_00_0_000000000_11_00000_00000));
    }

    // str simd 32 bit
    @Test
    public void test_strsimd32_pre() {
        testDecode("str_32_simd_pre", "str", Integer.toHexString(0b10_111_1_00_00_0_000000000_11_00000_00000));
    }

    // str simd 64 bit
    @Test
    public void test_strsimd64_pre() {
        testDecode("str_64_simd_pre", "str", Integer.toHexString(0b11_111_1_00_00_0_000000000_11_00000_00000));
    }

    // str simd 128 bit
    @Test
    public void test_strsimd128_pre() {
        testDecode("str_128_simd_pre", "str", Integer.toHexString(0b00_111_1_00_10_0_000000000_11_00000_00000));
    }

    // ldr simd 8 bit
    @Test
    public void test_ldrsimd8_pre() {
        testDecode("ldr_8_simd_pre", "ldr", Integer.toHexString(0b00_111_1_00_01_0_000000000_11_00000_00000));
    }

    // ldr simd 16 bit
    @Test
    public void test_ldrsimd16_pre() {
        testDecode("ldr_16_simd_pre", "ldr", Integer.toHexString(0b01_111_1_00_01_0_000000000_11_00000_00000));
    }

    // ldr simd 32 bit
    @Test
    public void test_ldrsimd32_pre() {
        testDecode("ldr_32_simd_pre", "ldr", Integer.toHexString(0b10_111_1_00_01_0_000000000_11_00000_00000));
    }

    // ldr simd 64 bit
    @Test
    public void test_ldrsimd64_pre() {
        testDecode("ldr_64_simd_pre", "ldr", Integer.toHexString(0b11_111_1_00_01_0_000000000_11_00000_00000));
    }

    // ldr simd 128 bit
    @Test
    public void test_ldrsimd128_pre() {
        testDecode("ldr_128_simd_pre", "ldr", Integer.toHexString(0b00_111_1_00_11_0_000000000_11_00000_00000));
    }

    // LOAD_STORE_PAIR_IMM_PREPOST_FMT1 (C4-284 to C4-285 and C4-286 to C4-287)
    // strb
    @Test
    public void test_strb_post() {
        testDecode("strb_post", "strb", Integer.toHexString(0b00_111_0_00_00_0_000000000_01_00000_00000));
    }

    // ldrb
    @Test
    public void test_ldrb_post() {
        testDecode("ldrb_post", "ldrb", Integer.toHexString(0b00_111_0_00_01_0_000000000_01_00000_00000));
    }

    // strh
    @Test
    public void test_strh_post() {
        testDecode("strh_post", "strh", Integer.toHexString(0b01_111_0_00_00_0_000000000_01_00000_00000));
    }

    // ldrh
    @Test
    public void test_ldrh_post() {
        testDecode("ldrh_post", "ldrh", Integer.toHexString(0b01_111_0_00_01_0_000000000_01_00000_00000));
    }

    // ldrsb 32 bit
    @Test
    public void test_ldrsb32_post() {
        testDecode("ldrsb32_post", "ldrsb", Integer.toHexString(0b00_111_0_00_11_0_000000000_01_00000_00000));
    }

    // ldrsb 64 bit
    @Test
    public void test_ldrsb64_post() {
        testDecode("ldrsb64_post", "ldrsb", Integer.toHexString(0b00_111_0_00_10_0_000000000_01_00000_00000));
    }

    // ldrsh 32 bit
    @Test
    public void test_ldrsh32_post() {
        testDecode("ldrsh32_post", "ldrsh", Integer.toHexString(0b01_111_0_00_11_0_000000000_01_00000_00000));
    }

    // ldrsh 64 bit
    @Test
    public void test_ldrsh64_post() {
        testDecode("ldrsh64_post", "ldrsh", Integer.toHexString(0b01_111_0_00_10_0_000000000_01_00000_00000));
    }

    // str 32 bit
    @Test
    public void test_str32_post() {
        testDecode("str_32_post", "str", Integer.toHexString(0b10_111_0_00_00_0_000000000_01_00000_00000));
    }

    // str 64 bit
    @Test
    public void test_str64_post() {
        testDecode("str_64_post", "str", Integer.toHexString(0b11_111_0_00_00_0_000000000_01_00000_00000));
    }

    // ldr 32 bit
    @Test
    public void test_ldr32_post() {
        testDecode("ldr_32_post", "ldr", Integer.toHexString(0b10_111_0_00_01_0_000000000_01_00000_00000));
    }

    // ldr 64 bit
    @Test
    public void test_ldr64_post() {
        testDecode("ldr_64_post", "ldr", Integer.toHexString(0b11_111_0_00_01_0_000000000_01_00000_00000));
    }

    // str simd 8 bit
    @Test
    public void test_strsimd8_post() {
        testDecode("str_8_simd_post", "str", Integer.toHexString(0b00_111_1_00_00_0_000000000_01_00000_00000));
    }

    // str simd 16 bit
    @Test
    public void test_strsimd16_post() {
        testDecode("str_16_simd_post", "str", Integer.toHexString(0b01_111_1_00_00_0_000000000_01_00000_00000));
    }

    // str simd 32 bit
    @Test
    public void test_strsimd32_post() {
        testDecode("str_32_simd_post", "str", Integer.toHexString(0b10_111_1_00_00_0_000000000_01_00000_00000));
    }

    // str simd 64 bit
    @Test
    public void test_strsimd64_post() {
        testDecode("str_64_simd_post", "str", Integer.toHexString(0b11_111_1_00_00_0_000000000_01_00000_00000));
    }

    // str simd 128 bit
    @Test
    public void test_strsimd128_post() {
        testDecode("str_128_simd_post", "str", Integer.toHexString(0b00_111_1_00_10_0_000000000_01_00000_00000));
    }

    // ldr simd 8 bit
    @Test
    public void test_ldrsimd8_post() {
        testDecode("ldr_8_simd_post", "ldr", Integer.toHexString(0b00_111_1_00_01_0_000000000_01_00000_00000));
    }

    // ldr simd 16 bit
    @Test
    public void test_ldrsimd16_post() {
        testDecode("ldr_16_simd_post", "ldr", Integer.toHexString(0b01_111_1_00_01_0_000000000_01_00000_00000));
    }

    // ldr simd 32 bit
    @Test
    public void test_ldrsimd32_post() {
        testDecode("ldr_32_simd_post", "ldr", Integer.toHexString(0b10_111_1_00_01_0_000000000_01_00000_00000));
    }

    // ldr simd 64 bit
    @Test
    public void test_ldrsimd64_post() {
        testDecode("ldr_64_simd_post", "ldr", Integer.toHexString(0b11_111_1_00_01_0_000000000_01_00000_00000));
    }

    // ldr simd 128 bit
    @Test
    public void test_ldrsimd128_post() {
        testDecode("ldr_128_simd_post", "ldr", Integer.toHexString(0b00_111_1_00_11_0_000000000_01_00000_00000));
    }

    // LOAD_STORE_REG_OFF (C4-295 to C4-297) //////////////////////////////////
    // strb_reg_off
    @Test
    public void test_strb_reg_off() {
        testDecode("strb_reg_off", "strb", Integer.toHexString(0b00_111_0_00_00_1_00000_000_0_10_00000_00000));
    }

    // ldrb_reg_off
    @Test
    public void test_ldrb_reg_off() {
        testDecode("ldrb_reg_off", "ldrb", Integer.toHexString(0b00_111_0_00_01_1_00000_000_0_10_00000_00000));
    }

    // strh_reg_off
    @Test
    public void test_strh_reg_off() {
        testDecode("strh_reg_off", "strh", Integer.toHexString(0b01_111_0_00_00_1_00000_000_0_10_00000_00000));
    }

    // ldrh_reg_off
    @Test
    public void test_ldrh_reg_off() {
        testDecode("ldrh_reg_off", "ldrh", Integer.toHexString(0b01_111_0_00_01_1_00000_000_0_10_00000_00000));
    }

    // ldrsw

    // prfm

    // ldrsb32_reg_off
    @Test
    public void test_ldrsb32_reg_off() {
        testDecode("ldrsb32_reg_off", "ldrsb", Integer.toHexString(0b00_111_0_00_11_1_00000_000_0_10_00000_00000));
    }

    // ldrsb64_reg_off
    @Test
    public void test_ldrsb64_reg_off() {
        testDecode("ldrsb64_reg_off", "ldrsb", Integer.toHexString(0b00_111_0_00_10_1_00000_000_0_10_00000_00000));
    }

    // ldrsh32_reg_off
    @Test
    public void test_ldrsh32_reg_off() {
        testDecode("ldrsh32_reg_off", "ldrsh", Integer.toHexString(0b01_111_0_00_11_1_00000_000_0_10_00000_00000));
    }

    // ldrsh64_reg_off
    @Test
    public void test_ldrsh64_reg_off() {
        testDecode("ldrsh64_reg_off", "ldrsh", Integer.toHexString(0b01_111_0_00_10_1_00000_000_0_10_00000_00000));
    }

    // str 32 bit reg_off
    @Test
    public void test_str32_reg_off() {
        testDecode("str_32_reg_off", "str", Integer.toHexString(0b10_111_0_00_00_1_000000000_10_00000_00000));
    }

    // str 64 reg_off
    @Test
    public void test_str64_reg_off() {
        testDecode("str_64_reg_off", "str", Integer.toHexString(0b11_111_0_00_00_1_000000000_10_00000_00000));
    }

    // ldr 32 reg_off
    @Test
    public void test_ldr32_reg_off() {
        testDecode("ldr_32_reg_off", "ldr", Integer.toHexString(0b10_111_0_00_01_1_000000000_10_00000_00000));
    }

    // ldr 64 reg_off
    @Test
    public void test_ldr64_reg_off() {
        testDecode("ldr_64_reg_off", "ldr", Integer.toHexString(0b11_111_0_00_01_1_000000000_10_00000_00000));
    }

    // str simd 8 bit reg_off
    @Test
    public void test_strsimd8_reg_off() {
        testDecode("str_8_simd_reg_off", "str", Integer.toHexString(0b00_111_1_00_00_1_000000000_10_00000_00000));
    }

    // str simd 16 bit reg_off
    @Test
    public void test_strsimd16_reg_off() {
        testDecode("str_16_simd_reg_off", "str", Integer.toHexString(0b01_111_1_00_00_1_000000000_10_00000_00000));
    }

    // str simd 32 bit reg_off
    @Test
    public void test_strsimd32_reg_off() {
        testDecode("str_32_simd_reg_off", "str", Integer.toHexString(0b10_111_1_00_00_1_000000000_10_00000_00000));
    }

    // str simd 64 bit reg_off
    @Test
    public void test_strsimd64_reg_off() {
        testDecode("str_64_simd_reg_off", "str", Integer.toHexString(0b11_111_1_00_00_1_000000000_10_00000_00000));
    }

    // str simd 128 bit reg_off
    @Test
    public void test_strsimd128_reg_off() {
        testDecode("str_128_simd_reg_off", "str", Integer.toHexString(0b00_111_1_00_10_1_000000000_10_00000_00000));
    }

    // ldr simd 8 bit reg_off
    @Test
    public void test_ldrsimd8_reg_off() {
        testDecode("ldr_8_simd_reg_off", "ldr", Integer.toHexString(0b00_111_1_00_01_1_000000000_10_00000_00000));
    }

    // ldr simd 16 bit reg_off
    @Test
    public void test_ldrsimd16_reg_off() {
        testDecode("ldr_16_simd_reg_off", "ldr", Integer.toHexString(0b01_111_1_00_01_1_000000000_10_00000_00000));
    }

    // ldr simd 32 bit reg_off
    @Test
    public void test_ldrsimd32_reg_off() {
        testDecode("ldr_32_simd_reg_off", "ldr", Integer.toHexString(0b10_111_1_00_01_1_000000000_10_00000_00000));
    }

    // ldr simd 64 bit reg_off
    @Test
    public void test_ldrsimd64_reg_off() {
        testDecode("ldr_64_simd_reg_off", "ldr", Integer.toHexString(0b11_111_1_00_01_1_000000000_10_00000_00000));
    }

    // ldr simd 128 bit reg_off
    @Test
    public void test_ldrsimd128_reg_off() {
        testDecode("ldr_128_simd_reg_off", "ldr", Integer.toHexString(0b00_111_1_00_11_1_000000000_10_00000_00000));
    }

    // LOAD_STORE_REG_UIMM ////////////////////////////////////////////////////
    // strb_uimm
    @Test
    public void test_strb_uimm() {
        testDecode("strb_uimm", "strb", Integer.toHexString(0b00_111_0_01_00_0000000000000000000000));
    }

    // ldrb_uimm
    @Test
    public void test_ldrb_uimm() {
        testDecode("ldrb_uimm", "ldrb", Integer.toHexString(0b00_111_0_01_01_0000000000000000000000));
    }

    // strh_uimm
    @Test
    public void test_strh_uimm() {
        testDecode("strh_uimm", "strh", Integer.toHexString(0b01_111_0_01_00_0000000000000000000000));
    }

    // ldrh_uimm
    @Test
    public void test_ldrh_uimm() {
        testDecode("ldrh_uimm", "ldrh", Integer.toHexString(0b01_111_0_01_01_0000000000000000000000));
    }

    // ldrsw

    // prfm

    // ldrsb32_uimm
    @Test
    public void test_ldrsb32_uimm() {
        testDecode("ldrsb32_uimm", "ldrsb", Integer.toHexString(0b00_111_0_01_11_0000000000000000000000));
    }

    // ldrsb64_uimm
    @Test
    public void test_ldrsb64_uimm() {
        testDecode("ldrsb64_uimm", "ldrsb", Integer.toHexString(0b00_111_0_01_10_0000000000000000000000));
    }

    // ldrsh32_uimm
    @Test
    public void test_ldrsh32_uimm() {
        testDecode("ldrsh32_uimm", "ldrsh", Integer.toHexString(0b01_111_0_01_11_0000000000000000000000));
    }

    // ldrsh64_uimm
    @Test
    public void test_ldrsh64_uimm() {
        testDecode("ldrsh64_uimm", "ldrsh", Integer.toHexString(0b01_111_0_01_10_0000000000000000000000));
    }

    // str 32 bit reg_off
    @Test
    public void test_str32_uimm() {
        testDecode("str_32_uimm", "str", Integer.toHexString(0b10_111_0_01_00_0000000000000000000000));
    }

    // str 64 reg_off
    @Test
    public void test_str64_uimm() {
        testDecode("str_64_uimm", "str", Integer.toHexString(0b11_111_0_01_00_0000000000000000000000));
    }

    // ldr 32 reg_off
    @Test
    public void test_ldr32_uimm() {
        testDecode("ldr_32_uimm", "ldr", Integer.toHexString(0b10_111_0_01_01_0000000000000000000000));
    }

    // ldr 64 reg_off
    @Test
    public void test_ldr64_uimm() {
        testDecode("ldr_64_uimm", "ldr", Integer.toHexString(0b11_111_0_01_01_0000000000000000000000));
    }

    // str simd 8 bit reg_off
    @Test
    public void test_strsimd8_uimm() {
        testDecode("str_8_simd_uimm", "str", Integer.toHexString(0b00_111_1_01_00_0000000000000000000000));
    }

    // str simd 16 bit reg_off
    @Test
    public void test_strsimd16_uimm() {
        testDecode("str_16_simd_uimm", "str", Integer.toHexString(0b01_111_1_01_00_0000000000000000000000));
    }

    // str simd 32 bit reg_off
    @Test
    public void test_strsimd32_uimm() {
        testDecode("str_32_simd_uimm", "str", Integer.toHexString(0b10_111_1_01_00_0000000000000000000000));
    }

    // str simd 64 bit reg_off
    @Test
    public void test_strsimd64_uimm() {
        testDecode("str_64_simd_uimm", "str", Integer.toHexString(0b11_111_1_01_00_0000000000000000000000));
    }

    // str simd 128 bit reg_off
    @Test
    public void test_strsimd128_uimm() {
        testDecode("str_128_simd_uimm", "str", Integer.toHexString(0b00_111_1_01_10_0000000000000000000000));
    }

    // ldr simd 8 bit reg_off
    @Test
    public void test_ldrsimd8_uimm() {
        testDecode("ldr_8_simd_uimm", "ldr", Integer.toHexString(0b00_111_1_01_01_0000000000000000000000));
    }

    // ldr simd 16 bit reg_off
    @Test
    public void test_ldrsimd16_uimm() {
        testDecode("ldr_16_simd_uimm", "ldr", Integer.toHexString(0b01_111_1_01_01_0000000000000000000000));
    }

    // ldr simd 32 bit reg_off
    @Test
    public void test_ldrsimd32_uimm() {
        testDecode("ldr_32_simd_uimm", "ldr", Integer.toHexString(0b10_111_1_01_01_0000000000000000000000));
    }

    // ldr simd 64 bit reg_off
    @Test
    public void test_ldrsimd64_uimm() {
        testDecode("ldr_64_simd_uimm", "ldr", Integer.toHexString(0b11_111_1_01_01_0000000000000000000000));
    }

    // ldr simd 128 bit reg_off
    @Test
    public void test_ldrsimd128_uimm() {
        testDecode("ldr_128_simd_uimm", "ldr", Integer.toHexString(0b00_111_1_01_11_0000000000000000000000));
    }

    // DPR_TWOSOURCE (C4-299) /////////////////////////////////////////////////
    // udiv32
    @Test
    public void test_udiv32() {
        testDecode("udiv32", "udiv", Integer.toHexString(0b0_00_11010110_00000_000010_00000_00000));
    }

    // udiv64
    @Test
    public void test_udiv64() {
        testDecode("udiv64", "udiv", Integer.toHexString(0b1_00_11010110_00000_000010_00000_00000));
    }

    // sdiv32
    @Test
    public void test_sdiv32() {
        testDecode("sdiv32", "sdiv", Integer.toHexString(0b0_00_11010110_00000_000011_00000_00000));
    }

    // sdiv64
    @Test
    public void test_sdiv64() {
        testDecode("sdiv64", "sdiv", Integer.toHexString(0b1_00_11010110_00000_000011_00000_00000));
    }

    // lslv32
    @Test
    public void test_lslv32() {
        testDecode("lslv32", "lslv", Integer.toHexString(0b0_00_11010110_00000_001000_00000_00000));
    }

    // lslv64
    @Test
    public void test_lslv64() {
        testDecode("lslv64", "lslv", Integer.toHexString(0b1_00_11010110_00000_001000_00000_00000));
    }

    // lsrv32
    @Test
    public void test_lsrv32() {
        testDecode("lsrv32", "lsrv", Integer.toHexString(0b0_00_11010110_00000_001001_00000_00000));
    }

    // lsrv64
    @Test
    public void test_lsrv64() {
        testDecode("lsrv64", "lsrv", Integer.toHexString(0b1_00_11010110_00000_001001_00000_00000));
    }

    // asrv32
    @Test
    public void test_asrv32() {
        testDecode("asrv32", "asrv", Integer.toHexString(0b0_00_11010110_00000_001010_00000_00000));
    }

    // asrv64
    @Test
    public void test_asrv64() {
        testDecode("asrv64", "asrv", Integer.toHexString(0b1_00_11010110_00000_001010_00000_00000));
    }

    // rorv32
    @Test
    public void test_rorv32() {
        testDecode("rorv32", "rorv", Integer.toHexString(0b0_00_11010110_00000_001011_00000_00000));
    }

    // rorv64
    @Test
    public void test_rorv64() {
        testDecode("rorv64", "rorv", Integer.toHexString(0b1_00_11010110_00000_001011_00000_00000));
    }

    // crc32b
    @Test
    public void test_crc32b() {
        testDecode("crc32b", "crc32b", Integer.toHexString(0b0_00_11010110_00000_010000_00000_00000));
    }

    // crc32h
    @Test
    public void test_crc32h() {
        testDecode("crc32h", "crc32h", Integer.toHexString(0b0_00_11010110_00000_010001_00000_00000));
    }

    // crc32w
    @Test
    public void test_crc32w() {
        testDecode("crc32w", "crc32w", Integer.toHexString(0b0_00_11010110_00000_010010_00000_00000));
    }

    // crc32x
    @Test
    public void test_crc32x() {
        testDecode("crc32x", "crc32x", Integer.toHexString(0b1_00_11010110_00000_010011_00000_00000));
    }

    // subp
    @Test
    public void test_subp() {
        testDecode("subp", "subp", Integer.toHexString(0b1_00_11010110_00000_000000_00000_00000));
    }

    // irg
    @Test
    public void test_irg() {
        testDecode("irg", "irg", Integer.toHexString(0b1_00_11010110_00000_000100_00000_00000));
    }

    // gmi
    @Test
    public void test_gmi() {
        testDecode("gmi", "gmi", Integer.toHexString(0b1_00_11010110_00000_000101_00000_00000));
    }

    // pacga
    @Test
    public void test_pacga() {
        testDecode("pacga", "pacga", Integer.toHexString(0b1_00_11010110_00000_001100_00000_00000));
    }

    // crc32cb
    @Test
    public void test_crc32cb() {
        testDecode("crc32cb", "crc32cb", Integer.toHexString(0b0_00_11010110_00000_010100_00000_00000));
    }

    // crc32ch
    @Test
    public void test_crc32ch() {
        testDecode("crc32ch", "crc32ch", Integer.toHexString(0b0_00_11010110_00000_010101_00000_00000));
    }

    // crc32cw
    @Test
    public void test_crc32cw() {
        testDecode("crc32cw", "crc32cw", Integer.toHexString(0b0_00_11010110_00000_010110_00000_00000));
    }

    // crc32cx
    @Test
    public void test_crc32cx() {
        testDecode("crc32cx", "crc32cx", Integer.toHexString(0b1_00_11010110_00000_010111_00000_00000));
    }

    // subps
    @Test
    public void test_subps() {
        testDecode("subps", "subps", Integer.toHexString(0b1_01_11010110_00000_000000_00000_00000));
    }

    // DPR_ONESOURCE (C4-301) /////////////////////////////////////////////////
    // rbit32
    @Test
    public void test_rbit32() {
        testDecode("rbit32", "rbit", Integer.toHexString(0b0_10_11010110_00000_000000_00000_00000));
    }

    // rbit64
    @Test
    public void test_rbit() {
        testDecode("rbit64", "rbit", Integer.toHexString(0b1_10_11010110_00000_000000_00000_00000));
    }

    // rev16 32 bit
    @Test
    public void test_rev16_32() {
        testDecode("rev16_32", "rev16", Integer.toHexString(0b0_10_11010110_00000_000001_00000_00000));
    }

    // rev16 64 bit
    @Test
    public void test_rev16_64() {
        testDecode("rev16_64", "rev16", Integer.toHexString(0b1_10_11010110_00000_000001_00000_00000));
    }

    // rev32
    @Test
    public void test_rev32() {
        testDecode("rev32", "rev", Integer.toHexString(0b0_10_11010110_00000_000010_00000_00000));
    }

    // rev64
    @Test
    public void test_rev64() {
        testDecode("rev64", "rev", Integer.toHexString(0b1_10_11010110_00000_000010_00000_00000));
    }

    // clz32
    @Test
    public void test_clz32() {
        testDecode("clz32", "clz", Integer.toHexString(0b0_10_11010110_00000_000100_00000_00000));
    }

    // clz64
    @Test
    public void test_clz64() {
        testDecode("clz64", "clz", Integer.toHexString(0b1_10_11010110_00000_000100_00000_00000));
    }

    // cls32
    @Test
    public void test_cls32() {
        testDecode("cls32", "cls", Integer.toHexString(0b0_10_11010110_00000_000101_00000_00000));
    }

    // cls64
    @Test
    public void test_cls64() {
        testDecode("cls64", "cls", Integer.toHexString(0b1_10_11010110_00000_000101_00000_00000));
    }

    // test "910003fd" --> should be a mov with 2 arguments "mov x29, sp"
    // NOTE: its not a bug, a "mov" with the sp (where Rn = Rd) is an alias of "add"
    @Test
    public void test_mov() {
        testDecode("mov", "mov", "910003fd");
    }

    // test 12001c21
    @Test
    public void test_and() {
        testDecode("and", "and", "12001c21");
    }

    /*
     * 0xa18:32150021    orr     w1,  w1,  #0xffffffff
        0xa1c:32160021   orr     w1,  w1,  #0xffffffff
        0xa20:321d0021   orr     w1,  w1,  #0xffffffff
        0xa24:321e0021   orr     w1,  w1,  #0xffffffff
        0xa28:321f0021   orr     w1,  w1,  #0xffffffff
     */
    @Test
    public void test_orrs() {
        testDecode("orr", "orr", "32150021");
        testDecode("orr", "orr", "32160021");
        testDecode("orr", "orr", "321d0021");
        testDecode("orr", "orr", "321e0021");
        testDecode("orr", "orr", "321f0021");
    }

    private void testDecode(String addr, String name, String expected, String binaryInstruction) {
        ArmInstruction testinst = ArmInstruction.newInstance(addr, binaryInstruction);
        ArmInstructionData idata = testinst.getData();
        ArmAsmFieldData fieldData = testinst.getFieldData();

        System.out.print(name + "\tresolved to\t->\t" + testinst.getRepresentation() +
                "\t(SIMD: " + idata.isSimd() + ", width: " + idata.getBitWidth() + ")\n");

        // System.out.print(name + "\tresolved to\t->\t" + testinst.getName() +
        // "\t(" + fieldData.getFields() + ")\n");

        // System.out.print(name + "\tresolved to\t->\t" + testinst.getName() + "target addr: 0x"
        // + Integer.toHexString(testinst.getBranchTarget().intValue()) + "\n");

        assertEquals(expected, testinst.getName());
    }

    private void testDecode(String name, String expected, String binaryInstruction) {
        testDecode("0", name, expected, binaryInstruction);
    }
}
