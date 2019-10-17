package org.specs.MicroBlaze.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.MicroBlaze.isa.MicroBlazeAsmInstructionData;
import org.specs.MicroBlaze.isa.MicroBlazeInstruction;
import org.specs.MicroBlaze.isa.MicroBlazeInstructionData;

public class MicroBLazeInstructionInstantiationTest {

    // SPECIAL //////////////////////////////////////////////////////////

    // mts
    @Test
    public void test_mts() {
        testDecode("mts", "mts", Integer.toHexString(0b100101_00_000_00000_11_00000000000000));
    }

    // mtse
    @Test
    public void test_mtse() {
        testDecode("mtse", "mtse", Integer.toHexString(0b100101_01_000_00000_11_00000000000000));
    }

    // mfs
    @Test
    public void test_mfs() {
        testDecode("mfs", "mfs", Integer.toHexString(0b100101_00000_00000_10_00000000000000));
    }

    // mfse
    @Test
    public void test_mfse() {
        testDecode("mfse", "mfse", Integer.toHexString(0b100101_00000_01000_10_00000000000000));
    }

    // msrclr
    @Test
    public void test_msrclr() {
        testDecode("msrclr", "msrclr", Integer.toHexString(0b100101_00000_100010_000000000000000));
    }

    // msrset
    @Test
    public void test_msrset() {
        testDecode("msrset", "msrset", Integer.toHexString(0b100101_00000_100000_000000000000000));
    }

    // MBAR ///////////////////////////////////////////////////////////////////

    // mbar
    @Test
    public void test_mbar() {
        testDecode("mbar", "mbar", Integer.toHexString(0b101110_00000_00010_0000000000000100));
    }

    // UBRANCH ////////////////////////////////////////////////////////////////

    // br
    @Test
    public void test_br() {
        testDecode("br", "br", Integer.toHexString(0b100110_00000_000_00_00000_00000000000));
    }

    // bra
    @Test
    public void test_bra() {
        testDecode("bra", "bra", Integer.toHexString(0b100110_00000_010_00_00000_00000000000));
    }

    // brd
    @Test
    public void test_brd() {
        testDecode("brd", "brd", Integer.toHexString(0b100110_00000_100_00_00000_00000000000));
    }

    // brad
    @Test
    public void test_brad() {
        testDecode("brad", "brad", Integer.toHexString(0b100110_00000_110_00_00000_00000000000));
    }

    // ULBRANCH ///////////////////////////////////////////////////////////////

    // brld
    @Test
    public void test_brld() {
        testDecode("brld", "brld", Integer.toHexString(0b100110_00000_101_00_00000_00000000000));
    }

    // brald
    @Test
    public void test_brald() {
        testDecode("brald", "brald", Integer.toHexString(0b100110_00000_111_00_00000_00000000000));
    }

    // brk
    @Test
    public void test_brk() {
        testDecode("brk", "brk", Integer.toHexString(0b100110_00000_011_00_00000_00000000000));
    }

    // UIBRANCH ///////////////////////////////////////////////////////////////

    // bri
    @Test
    public void test_bri() {
        testDecode("bri", "bri", Integer.toHexString(0b101110_00000_000_00_00000_00000000000));
    }

    // brai
    @Test
    public void test_brai() {
        testDecode("brai", "brai", Integer.toHexString(0b101110_00000_010_00_00000_00000000000));
    }

    // brid
    @Test
    public void test_brid() {
        testDecode("brid", "brid", Integer.toHexString(0b101110_00000_100_00_00000_00000000000));
    }

    // braid
    @Test
    public void test_braid() {
        testDecode("braid", "braid", Integer.toHexString(0b101110_00000_110_00_00000_00000000000));
    }

    // CBRANCH ////////////////////////////////////////////////////////////////

    // beq
    @Test
    public void test_beq() {
        testDecode("beq", "beq", Integer.toHexString(0b100111_0_0000_00000_00000_00000000000));
    }

    // beqd
    @Test
    public void test_beqd() {
        testDecode("beqd", "beqd", Integer.toHexString(0b100111_1_0000_00000_00000_00000000000));
    }

    // bge
    @Test
    public void test_bge() {
        testDecode("bge", "bge", Integer.toHexString(0b100111_00101_00000_00000_00000000000));
    }

    // bged
    @Test
    public void test_bged() {
        testDecode("bged", "bged", Integer.toHexString(0b100111_10101_00000_00000_00000000000));
    }

    // bgt
    @Test
    public void test_bgt() {
        testDecode("bgt", "bgt", Integer.toHexString(0b100111_00100_00000_00000_00000000000));
    }

    // bgtd
    @Test
    public void test_bgtd() {
        testDecode("bgtd", "bgtd", Integer.toHexString(0b100111_10100_00000_00000_00000000000));
    }

    // ble
    @Test
    public void test_ble() {
        testDecode("ble", "ble", Integer.toHexString(0b100111_00011_00000_00000_00000000000));
    }

    // bled
    @Test
    public void test_bled() {
        testDecode("bled", "bled", Integer.toHexString(0b100111_10011_00000_00000_00000000000));
    }

    // blt
    @Test
    public void test_blt() {
        testDecode("blt", "blt", Integer.toHexString(0b100111_00010_00000_00000_00000000000));
    }

    // bltd
    @Test
    public void test_bltd() {
        testDecode("bltd", "bltd", Integer.toHexString(0b100111_10010_00000_00000_00000000000));
    }

    // bne
    @Test
    public void test_bne() {
        testDecode("bne", "bne", Integer.toHexString(0b100111_00001_00000_00000_00000000000));
    }

    // bned
    @Test
    public void test_bned() {
        testDecode("bned", "bned", Integer.toHexString(0b100111_10001_00000_00000_00000000000));
    }

    // CIBRANCH ////////////////////////////////////////////////////////////////

    // beqi
    @Test
    public void test_beqi() {
        testDecode("beqi", "beqi", Integer.toHexString(0b101111_0_0000_00000_00000_00000000000));
    }

    // beqid
    @Test
    public void test_beqid() {
        testDecode("beqid", "beqid", Integer.toHexString(0b101111_1_0000_00000_00000_00000000000));
    }

    // bgei
    @Test
    public void test_bgei() {
        testDecode("bgei", "bgei", Integer.toHexString(0b101111_00101_00000_00000_00000000000));
    }

    // bgeid
    @Test
    public void test_bgeid() {
        testDecode("bgeid", "bgeid", Integer.toHexString(0b101111_10101_00000_00000_00000000000));
    }

    // bgti
    @Test
    public void test_bgti() {
        testDecode("bgti", "bgti", Integer.toHexString(0b101111_00100_00000_00000_00000000000));
    }

    // bgtid
    @Test
    public void test_bgtid() {
        testDecode("bgtid", "bgtid", Integer.toHexString(0b101111_10100_00000_00000_00000000000));
    }

    // blei
    @Test
    public void test_blei() {
        testDecode("blei", "blei", Integer.toHexString(0b101111_00011_00000_00000_00000000000));
    }

    // bleid
    @Test
    public void test_bleid() {
        testDecode("bleid", "bleid", Integer.toHexString(0b101111_10011_00000_00000_00000000000));
    }

    // blti
    @Test
    public void test_blti() {
        testDecode("blti", "blti", Integer.toHexString(0b101111_00010_00000_00000_00000000000));
    }

    // bltid
    @Test
    public void test_bltid() {
        testDecode("bltid", "bltid", Integer.toHexString(0b101111_10010_00000_00000_00000000000));
    }

    // bnei
    @Test
    public void test_bnei() {
        testDecode("bnei", "bnei", Integer.toHexString(0b101111_00001_00000_00000_00000000000));
    }

    // bneid
    @Test
    public void test_bneid() {
        testDecode("bneid", "bneid", Integer.toHexString(0b101111_10001_00000_00000_00000000000));
    }

    // RETURN /////////////////////////////////////////////////////////////////

    // rtbd
    @Test
    public void test_rtbd() {
        testDecode("rtbd", "rtbd", Integer.toHexString(0b101101_10010_00000_0000000000000000));
    }

    // rtid
    @Test
    public void test_rtid() {
        testDecode("rtid", "rtid", Integer.toHexString(0b101101_10001_00000_0000000000000000));
    }

    // rted
    @Test
    public void test_rted() {
        testDecode("rted", "rted", Integer.toHexString(0b101101_10100_00000_0000000000000000));
    }

    // rtsd
    @Test
    public void test_rtsd() {
        testDecode("rtsd", "rtsd", Integer.toHexString(0b101101_10000_00000_0000000000000000));
    }

    // IBARREL ////////////////////////////////////////////////////////////////

    // bsrli
    @Test
    public void test_bsrli() {
        testDecode("bsrli", "bsrli", Integer.toHexString(0b011001_00000_00000_00000_00_0000_00000));
    }

    // bsrai
    @Test
    public void test_bsrai() {
        testDecode("bsrai", "bsrai", Integer.toHexString(0b011001_00000_00000_00000_01_0000_00000));
    }

    // bslli
    @Test
    public void test_bslli() {
        testDecode("bslli", "bslli", Integer.toHexString(0b011001_00000_00000_00000_10_0000_00000));
    }

    // bsefi
    @Test
    public void test_bsefi() {
        testDecode("bsefi", "bsefi", Integer.toHexString(0b011001_00000_00000_01_000_00000_0_00000));
    }

    // bsifi
    @Test
    public void test_bsifi() {
        testDecode("bsifi", "bsifi", Integer.toHexString(0b011001_00000_00000_10_000_00000_0_00000));
    }

    // STREAM

    // put
    @Test
    public void test_put() {
        testDecode("put", "put", Integer.toHexString(0b011011_00000_00000_1_0000_0000000_0000));
    }

    // get
    @Test
    public void test_get() {
        testDecode("get", "get", Integer.toHexString(0b011011_00000_00000_0_0000_0000000_0000));
    }

    // DSTREAM

    // putd
    @Test
    public void test_putd() {
        testDecode("putd", "putd", Integer.toHexString(0b010011_00000_00000_1_0000_0000000_0000));
    }

    // getd
    @Test
    public void test_getd() {
        testDecode("getd", "getd", Integer.toHexString(0b010011_00000_00000_0_0000_0000000_0000));
    }

    // IMM ////////////////////////////////////////////////////////////////////

    // imm
    @Test
    public void test_imm() {
        testDecode("imm", "imm", Integer.toHexString(0b101100_00000_00000_0000000000000000));
    }

    // TYPE_A /////////////////////////////////////////////////////////////////

    // add
    @Test
    public void test_add() {
        testDecode("add", "add", Integer.toHexString(0b000000_00000_00000_00000_00000000000));
    }

    // addc
    @Test
    public void test_addc() {
        testDecode("addc", "addc", Integer.toHexString(0b000010_00000_00000_00000_00000000000));
    }

    // addk
    @Test
    public void test_addk() {
        testDecode("addk", "addk", Integer.toHexString(0b000100_00000_00000_00000_00000000000));
    }

    // addkc
    @Test
    public void test_addkc() {
        testDecode("addkc", "addkc", Integer.toHexString(0b000110_00000_00000_00000_00000000000));
    }

    private void testDecode(String addr, String name, String expected, String binaryInstruction) {
        MicroBlazeInstruction testinst = MicroBlazeInstruction.newInstance(addr, binaryInstruction);
        MicroBlazeInstructionData idata = testinst.getData();
        MicroBlazeAsmInstructionData fieldData = testinst.getFieldData();

        System.out.print(name + "\tresolved to\t->\t" + testinst.getName() +
                "\t(data width: " + idata.getBitWidth() + ")\n");

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
