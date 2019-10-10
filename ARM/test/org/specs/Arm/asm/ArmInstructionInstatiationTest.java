package org.specs.Arm.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.Arm.isa.ArmInstruction;

public class ArmInstructionInstatiationTest {

    /*
     * Instantiate one of every single instruction in the ISA as specified by the manual:
     * https://static.docs.arm.com/ddi0487/ea/DDI0487E_a_armv8_arm.pdf
     * To see if they get correctly identified into the proper mnemonic
     */

    // DPI_PCREL //////////////////////////////////////////////////////////
    @Test
    public void testAdr() {
        testDecode("adr", "adr", Integer.toHexString(0b000_10000_0000000000000000000_00000));
    }

    @Test
    public void testAdrp() {
        testDecode("adrp", "adrp", Integer.toHexString(0b100_10000_0000000000000000000_00000));
    }

    // DPI_ADDSUBIMM //////////////////////////////////////////////////////
    // 32 bit add
    @Test
    public void testAddImm32() {
        testDecode("add_imm_32", "add", Integer.toHexString(0b000_100010_0_000000000000_00000_00000));
    }

    // 32 bit add w/set flag
    @Test
    public void testAddsImm32() {
        testDecode("adds_imm_32", "adds", Integer.toHexString(0b001_100010_0_000000000000_00000_00000));
    }

    // 32 bit sub
    @Test
    public void testSubImm32() {
        testDecode("sub_imm_32", "sub", Integer.toHexString(0b010_100010_0_000000000000_00000_00000));
    }

    // 32 bit sub w/set flag
    @Test
    public void testSubsImm32() {
        testDecode("subs_imm_32", "subs", Integer.toHexString(0b011_100010_0_000000000000_00000_00000));
    }

    // 64 bit add
    @Test
    public void testAddImm64() {
        testDecode("add_imm_64", "add", Integer.toHexString(0b100_100010_0_000000000000_00000_00000));
    }

    // 64 bit add w/set flag
    @Test
    public void testAddsImm64() {
        testDecode("adds_imm_64", "adds", Integer.toHexString(0b101_100010_0_000000000000_00000_00000));
    }

    // 64 bit sub
    @Test
    public void testSubImm64() {
        testDecode("sub_imm_64", "sub", Integer.toHexString(0b110_100010_0_000000000000_00000_00000));
    }

    // 64 bit sub w/set flag
    @Test
    public void testSubsImm64() {
        testDecode("subs_imm_64", "subs", Integer.toHexString(0b111_100010_0_000000000000_00000_00000));
    }

    // DPI_ADDSUBIMM_TAGS /////////////////////////////////////////////////
    // add with tags
    @Test
    public void testAddg() {
        testDecode("addg", "addg", Integer.toHexString(0b100_100011_0_000000000000_00000_00000));
    }

    // sub with tags
    @Test
    public void testSubg() {
        testDecode("subg", "subg", Integer.toHexString(0b110_100011_0_000000000000_00000_00000));
    }

    // LOGICAL ////////////////////////////////////////////////////////////
    // logical and immediate 32 bits
    @Test
    public void testAndImm32() {
        testDecode("and_imm_32", "and", Integer.toHexString(0b000_100100_0_000000000000_00000_00000));
    }

    // logical orr immediate 32 bits
    @Test
    public void testOrrImm32() {
        testDecode("orr_imm_32", "orr", Integer.toHexString(0b001_100100_0_000000000000_00000_00000));
    }

    // logical eor immediate 32 bits
    @Test
    public void testEorImm32() {
        testDecode("eor_imm_32", "eor", Integer.toHexString(0b010_100100_0_000000000000_00000_00000));
    }

    // logical ands immediate 32 bits
    @Test
    public void testAndsImm32() {
        testDecode("ands_imm_32", "ands", Integer.toHexString(0b011_100100_0_000000000000_00000_00000));
    }

    // logical and immediate 64 bits
    @Test
    public void testAndImm64() {
        testDecode("and_imm_64", "and", Integer.toHexString(0b100_100100_0_000000000000_00000_00000));
    }

    // logical orr immediate 64 bits
    @Test
    public void testOrrImm64() {
        testDecode("orr_imm_64", "orr", Integer.toHexString(0b101_100100_0_000000000000_00000_00000));
    }

    // logical eor immediate 64 bits
    @Test
    public void testEorImm64() {
        testDecode("eor_imm_64", "eor", Integer.toHexString(0b110_100100_0_000000000000_00000_00000));
    }

    // logical ands immediate 64 bits
    @Test
    public void testAndsImm64() {
        testDecode("ands_imm_64", "ands", Integer.toHexString(0b111_100100_0_000000000000_00000_00000));
    }

    // MOVEW //////////////////////////////////////////////////////////////////
    // moven 32 bit
    @Test
    public void testMovn32() {
        testDecode("movn_imm_32", "movn", Integer.toHexString(0b000_100101_0_000000000000_00000_00000));
    }

    // movez 32 bit
    @Test
    public void testMovz32() {
        testDecode("movz_imm_32", "movz", Integer.toHexString(0b010_100101_0_000000000000_00000_00000));
    }

    // movek 32 bit
    @Test
    public void testMovk32() {
        testDecode("movk_imm_32", "movk", Integer.toHexString(0b011_100101_0_000000000000_00000_00000));
    }

    // moven 64 bit
    @Test
    public void testMovn64() {
        testDecode("movn_imm_64", "movn", Integer.toHexString(0b100_100101_0_000000000000_00000_00000));
    }

    // movez 64 bit
    @Test
    public void testMovz64() {
        testDecode("movz_imm_64", "movz", Integer.toHexString(0b110_100101_0_000000000000_00000_00000));
    }

    // movek 64 bit
    @Test
    public void testMovk64() {
        testDecode("movk_imm_64", "movk", Integer.toHexString(0b111_100101_0_000000000000_00000_00000));
    }

    // BITFIELD ///////////////////////////////////////////////////////////////
    // sbfm 32 bit
    @Test
    public void testSbfm32() {
        testDecode("sbfm_imm_32", "sbfm", Integer.toHexString(0b000_100110_0_000000000000_00000_00000));
    }

    // bfm 32 bit
    @Test
    public void testBfm32() {
        testDecode("bfm_imm_32", "bfm", Integer.toHexString(0b001_100110_0_000000000000_00000_00000));
    }

    // ubfm 32 bit
    @Test
    public void testUbfm32() {
        testDecode("ubfm_imm_32", "ubfm", Integer.toHexString(0b010_100110_0_000000000000_00000_00000));
    }

    // sbfm 64 bit
    @Test
    public void testSbfm64() {
        testDecode("sbfm_imm_64", "sbfm", Integer.toHexString(0b100_100110_0_000000000000_00000_00000));
    }

    // bfm 64 bit
    @Test
    public void testBfm64() {
        testDecode("bfm_imm_64", "bfm", Integer.toHexString(0b101_100110_0_000000000000_00000_00000));
    }

    // ubfm 64 bit
    @Test
    public void testUbfm64() {
        testDecode("ubfm_imm_64", "ubfm", Integer.toHexString(0b110_100110_0_000000000000_00000_00000));
    }

    // EXTRACT ////////////////////////////////////////////////////////////////
    // extr 32 bit
    @Test
    public void testExtr32() {
        testDecode("extr_imm_32", "extr", Integer.toHexString(0b000_100111_0_000000000000_00000_00000));
    }

    // extr 64 bit
    @Test
    public void testExtr64() {
        testDecode("extr_imm_64", "extr", Integer.toHexString(0b100_100111_1_000000000000_00000_00000));
    }

    // CONDITIONALBRANCH //////////////////////////////////////////////////////
    // b.eq
    @Test
    public void testBeq() {
        testDecode("b.eq", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_0000));
    }

    // b.ne
    @Test
    public void testBne() {
        testDecode("b.ne", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_0001));
    }

    // b.cs
    @Test
    public void testBcs() {
        testDecode("b.cs", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_0010));
    }

    // b.cc
    @Test
    public void testBcc() {
        testDecode("b.cc", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_0011));
    }

    // b.mn
    @Test
    public void testBmn() {
        testDecode("b.mn", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_0100));
    }

    // b.pl
    @Test
    public void testBpl() {
        testDecode("b.pl", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_0101));
    }

    // b.vs
    @Test
    public void testBvs() {
        testDecode("b.vs", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_0110));
    }

    // b.vc
    @Test
    public void testBvc() {
        testDecode("b.vc", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_0111));
    }

    // b.hi
    @Test
    public void testBhi() {
        testDecode("b.hi", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_1000));
    }

    // b.ls
    @Test
    public void testBls() {
        testDecode("b.ls", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_1001));
    }

    // b.ge
    @Test
    public void testBge() {
        testDecode("b.ge", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_1010));
    }

    // b.lt
    @Test
    public void testBlt() {
        testDecode("b.lt", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_1011));
    }

    // b.gt
    @Test
    public void testBlgt() {
        testDecode("b.gt", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_1100));
    }

    // b.le
    @Test
    public void testBle() {
        testDecode("b.le", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_1101));
    }

    // b.al
    @Test
    public void testBal() {
        testDecode("b.al", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_1110));
    }

    // b.nvb
    @Test
    public void testBnvb() {
        testDecode("b.nvb", "b", Integer.toHexString(0b0101_0100_0000000000000000000_0_1111));
    }

    // EXCEPTION //////////////////////////////////////////////////////////////
    // svc
    @Test
    public void testSvc() {
        testDecode("svc", "svc", Integer.toHexString(0b1101_0100_000_0000000000000000_000_01));
    }

    // hvc
    @Test
    public void testHvc() {
        testDecode("hvc", "hvc", Integer.toHexString(0b1101_0100_000_0000000000000000_000_10));
    }

    // smc
    @Test
    public void testSmc() {
        testDecode("smc", "smc", Integer.toHexString(0b1101_0100_000_0000000000000000_000_11));
    }

    // brk
    @Test
    public void testBrk() {
        testDecode("brk", "brk", Integer.toHexString(0b1101_0100_001_0000000000000000_000_00));
    }

    // hlt
    @Test
    public void testHlt() {
        testDecode("hlt", "hlt", Integer.toHexString(0b1101_0100_010_0000000000000000_000_00));
    }

    // dcps1
    @Test
    public void testDcps1() {
        testDecode("dcps1", "dcps1", Integer.toHexString(0b1101_0100_101_0000000000000000_000_01));
    }

    // dcps2
    @Test
    public void testDcps2() {
        testDecode("dcps2", "dcps2", Integer.toHexString(0b1101_0100_101_0000000000000000_000_10));
    }

    // dcps3
    @Test
    public void testDcps3() {
        testDecode("dcps3", "dcps3", Integer.toHexString(0b1101_0100_101_0000000000000000_000_11));
    }

    // UNCONDITIONAL BRANCH (IMMEDIATE) ///////////////////////////////////////
    // br
    @Test
    public void testBr() {
        testDecode("br", "br", Integer.toHexString(0b1101_011_0000_11111_000000_00000_00000));
    }

    // braaz
    @Test
    public void testBraaz() {
        testDecode("braaz", "braaz", Integer.toHexString(0b1101_011_0000_11111_000010_00000_00000));
    }

    // braa
    @Test
    public void testBraa() {
        testDecode("braa", "braa", Integer.toHexString(0b1101_011_1000_11111_000010_00000_00000));
    }

    // brabz
    @Test
    public void testBrabz() {
        testDecode("brabz", "brabz", Integer.toHexString(0b1101_011_0000_11111_000011_00000_00000));
    }

    // brab
    @Test
    public void testBrab() {
        testDecode("brab", "brab", Integer.toHexString(0b1101_011_1000_11111_000011_00000_00000));
    }

    // blraaz
    @Test
    public void testBlraaz() {
        testDecode("blraaz", "blraaz", Integer.toHexString(0b1101_011_0001_11111_000010_00000_00000));
    }

    // braa
    @Test
    public void testBlraa() {
        testDecode("blraa", "blraa", Integer.toHexString(0b1101_011_1001_11111_000010_00000_00000));
    }

    // blrabz
    @Test
    public void testBlrabz() {
        testDecode("blrabz", "blrabz", Integer.toHexString(0b1101_011_0001_11111_000011_00000_00000));
    }

    // blrab
    @Test
    public void testBlrab() {
        testDecode("blrab", "blrab", Integer.toHexString(0b1101_011_1001_11111_000011_00000_00000));
    }

    // UNCONDITIONAL BRANCH (IMMEDIATE) (C4-264 to C4-265) ////////////////////
    // b
    @Test
    public void testB() {
        testDecode("b", "b", Integer.toHexString(0b0_00101_00000000000000000000000000));
    }

    // COMPARE AND BRANCH (MMEDIATE) (C4-265) /////////////////////////////////
    // cbz 32 bits
    @Test
    public void testCbz32() {
        testDecode("cbz32", "cbz", Integer.toHexString(0b0_011010_0_000000000000000000000000));
    }

    // cbz 64 bits
    @Test
    public void testCbz64() {
        testDecode("cbz64", "cbz", Integer.toHexString(0b1_011010_0_000000000000000000000000));
    }

    // cbnz 32 bits
    @Test
    public void testCbnz32() {
        testDecode("cbnz32", "cbnz", Integer.toHexString(0b0_011010_1_000000000000000000000000));
    }

    // cbnz 64 bits
    @Test
    public void testCbnz64() {
        testDecode("cbnz64", "cbnz", Integer.toHexString(0b1_011010_1_000000000000000000000000));
    }

    // TEST_AND_BRANCH (C4-265) ///////////////////////////////////////////////
    // tbz
    @Test
    public void testTbz() {
        testDecode("tbz", "tbz", Integer.toHexString(0b0_011011_0_000000000000000000000000));
    }

    // tbnz
    @Test
    public void testTbnz() {
        testDecode("tbnz", "tbnz", Integer.toHexString(0b0_011011_1_000000000000000000000000));
    }

    // LOAD_REG_LITERAL (C4-280) //////////////////////////////////////////////
    // ldr literal 32 bits
    @Test
    public void testldr32literal() {
        testDecode("ldr_32_literal", "ldr", Integer.toHexString(0b00_011_000_0000000000000000000_00000));
    }

    // ldr literal 64 bits
    @Test
    public void testldr64literal() {
        testDecode("ldr_64_literal", "ldr", Integer.toHexString(0b01_011_000_0000000000000000000_00000));
    }

    // ldr literal simd 32 bits
    @Test
    public void testldr32literalsimd() {
        testDecode("ldr_32_literal_simd", "ldr", Integer.toHexString(0b00_011_100_0000000000000000000_00000));
    }

    // ldr literal simd 64 bits
    @Test
    public void testldr64literalsimd() {
        testDecode("ldr_64_literal_simd", "ldr", Integer.toHexString(0b01_011_100_0000000000000000000_00000));
    }

    // ldr literal simd 128 bits
    @Test
    public void testldr128literalsimd() {
        testDecode("ldr_128_literal_simd", "ldr", Integer.toHexString(0b10_011_100_0000000000000000000_00000));
    }

    // LOAD_STORE_PAIR (LOAD_STORE_NOALLOC) (C4-280 to C4-281) ////////////////
    // stnp 32
    @Test
    public void teststnp32() {
        testDecode("stnp_32", "stnp", Integer.toHexString(0b00_101_0_000_0_0000000_00000_00000_00000));
    }

    // stnp 64
    @Test
    public void teststnp64() {
        testDecode("stnp_64", "stnp", Integer.toHexString(0b10_101_0_000_0_0000000_00000_00000_00000));
    }

    // stnp 32 simd
    @Test
    public void teststnp32simd() {
        testDecode("stnp_32_simd", "stnp", Integer.toHexString(0b00_101_1_000_0_0000000_00000_00000_00000));
    }

    // stnp 64 simd
    @Test
    public void teststnp64simd() {
        testDecode("stnp_64_simd", "stnp", Integer.toHexString(0b01_101_1_000_0_0000000_00000_00000_00000));
    }

    // stnp 128 simd
    @Test
    public void teststnp128simd() {
        testDecode("stnp_128_simd", "stnp", Integer.toHexString(0b10_101_1_000_0_0000000_00000_00000_00000));
    }

    // ldnp 32
    @Test
    public void testldnp32() {
        testDecode("ldnp_32", "ldnp", Integer.toHexString(0b00_101_0_000_1_0000000_00000_00000_00000));
    }

    // ldnp 64
    @Test
    public void testldnp64() {
        testDecode("ldnp_64", "ldnp", Integer.toHexString(0b10_101_0_000_1_0000000_00000_00000_00000));
    }

    // ldnp 32 simd
    @Test
    public void testldnp32simd() {
        testDecode("ldnp_32_simd", "ldnp", Integer.toHexString(0b00_101_1_000_1_0000000_00000_00000_00000));
    }

    // ldnp 64 simd
    @Test
    public void testldnp64simd() {
        testDecode("ldnp_64_simd", "ldnp", Integer.toHexString(0b01_101_1_000_1_0000000_00000_00000_00000));
    }

    // ldnp 128 simd
    @Test
    public void testldnp128simd() {
        testDecode("ldnp_128_simd", "ldnp", Integer.toHexString(0b00_101_0_000_1_0000000_00000_00000_00000));
    }

    // LOAD_STORE_PAIR (POST INDEXED) (C4-281 to C4-282) //////////////////////
    // stp 32
    @Test
    public void teststp32() {
        testDecode("stp_32_post", "stp", Integer.toHexString(0b00_101_0_001_0_0000000_00000_00000_00000));
    }

    // stp 64
    @Test
    public void teststp64() {
        testDecode("stp_64_post", "stp", Integer.toHexString(0b10_101_0_001_0_0000000_00000_00000_00000));
    }

    // stp 32 simd
    @Test
    public void teststp32simd() {
        testDecode("stp_32_simd_post", "stp", Integer.toHexString(0b00_101_1_001_0_0000000_00000_00000_00000));
    }

    // stp 64 simd
    @Test
    public void teststp64simd() {
        testDecode("stp_64_simd_post", "stp", Integer.toHexString(0b01_101_1_001_0_0000000_00000_00000_00000));
    }

    // stp 128 simd
    @Test
    public void teststp128simd() {
        testDecode("stp_128_simd_post", "stp", Integer.toHexString(0b10_101_1_001_0_0000000_00000_00000_00000));
    }

    // ldp 32
    @Test
    public void testldp32() {
        testDecode("ldp_32_post", "ldp", Integer.toHexString(0b00_101_0_001_1_0000000_00000_00000_00000));
    }

    // ldp 64
    @Test
    public void testldp64() {
        testDecode("ldp_64_post", "ldp", Integer.toHexString(0b10_101_0_001_1_0000000_00000_00000_00000));
    }

    // ldp 32 simd
    @Test
    public void testldp32simd() {
        testDecode("ldp_32_simd_post", "ldp", Integer.toHexString(0b00_101_1_001_1_0000000_00000_00000_00000));
    }

    // ldp 64 simd
    @Test
    public void testldp64simd() {
        testDecode("ldp_64_simd_post", "ldp", Integer.toHexString(0b01_101_1_001_1_0000000_00000_00000_00000));
    }

    // ldp 128 simd
    @Test
    public void testldp128simd() {
        testDecode("ldp_128_simd_post", "ldp", Integer.toHexString(0b00_101_0_001_1_0000000_00000_00000_00000));
    }

    private void testDecode(String name, String expected, String binaryInstruction) {
        ArmInstruction testinst = new ArmInstruction("0", binaryInstruction);
        System.out.print(name + "\tresolved to\t->\t" + testinst.getName() +
                "\t(SIMD: " + testinst.isSimd() + ", width: " + testinst.getBitWidth() + ")\n");
        assertEquals(expected, testinst.getName());
    }
}
