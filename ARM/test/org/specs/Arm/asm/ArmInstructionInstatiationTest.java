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
    public void testSbfm32() {op.getRepresentation()
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
    // b.
    @Test
    public void testB() {
        testDecode("bcond", "b", Integer.toHexString(0b0101_0100_000000000000000000_0_0000));
    }

    private void testDecode(String name, String expected, String binaryInstruction) {
        ArmInstruction testinst = new ArmInstruction("0", binaryInstruction);
        System.out.print(name + "\tresolved to\t->\t" + testinst.getName() +
                "\t(SIMD: " + testinst.isSimd() + ", width: " + testinst.getBitWidth() + ")\n");
        assertEquals(expected, testinst.getName());
    }
}
