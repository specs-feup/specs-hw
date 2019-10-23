package org.specs.MicroBlaze.asm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.instruction.MicroBlazeInstructionData;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldData;

public class MicroBlazeInstructionInstantiationTester {

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

    // rsub
    @Test
    public void test_rsub() {
        testDecode("rsub", "rsub", Integer.toHexString(0b000001_00000_00000_00000_00000000000));
    }

    // rsubc
    @Test
    public void test_rsubc() {
        testDecode("rsubc", "rsubc", Integer.toHexString(0b000011_00000_00000_00000_00000000000));
    }

    // rsubk
    @Test
    public void test_rsubk() {
        testDecode("rsubk", "rsubk", Integer.toHexString(0b000101_00000_00000_00000_00000000000));
    }

    // rsubkc
    @Test
    public void test_rsubkc() {
        testDecode("rsubkc", "rsubkc", Integer.toHexString(0b000111_00000_00000_00000_00000000000));
    }

    // cmp
    @Test
    public void test_cmp() {
        testDecode("cmp", "cmp", Integer.toHexString(0b00101_00000_00000_00000_00000000001));
    }

    // cmpu
    @Test
    public void test_cmpu() {
        testDecode("cmpu", "cmpu", Integer.toHexString(0b00101_00000_00000_00000_00000000011));
    }

    // mul
    @Test
    public void test_mul() {
        testDecode("mul", "mul", Integer.toHexString(0b010000_00000_00000_00000_00000000000));
    }

    // mulh
    @Test
    public void test_mulh() {
        testDecode("mulh", "mulh", Integer.toHexString(0b010000_00000_00000_00000_00000000001));
    }

    // mulhu
    @Test
    public void test_mulhu() {
        testDecode("mulhu", "mulhu", Integer.toHexString(0b010000_00000_00000_00000_00000000011));
    }

    // mulhsu
    @Test
    public void test_mulhsu() {
        testDecode("mulhsu", "mulhsu", Integer.toHexString(0b010000_00000_00000_00000_00000000010));
    }

    // bsrl
    @Test
    public void test_bsrl() {
        testDecode("bsrl", "bsrl", Integer.toHexString(0b010001_00000_00000_00000_00000000000));
    }

    // bsra
    @Test
    public void test_bsra() {
        testDecode("bsra", "bsra", Integer.toHexString(0b010001_00000_00000_00000_01000000000));
    }

    // bsll
    @Test
    public void test_bsll() {
        testDecode("bsll", "bsll", Integer.toHexString(0b010001_00000_00000_00000_10000000000));
    }

    // idiv
    @Test
    public void test_idiv() {
        testDecode("idiv", "idiv", Integer.toHexString(0b010010_00000_00000_00000_00000000000));
    }

    // idivu
    @Test
    public void test_idivu() {
        testDecode("idivu", "idivu", Integer.toHexString(0b010010_00000_00000_00000_00000000010));
    }

    // fadd
    @Test
    public void test_fadd() {
        testDecode("fadd", "fadd", Integer.toHexString(0b010110_00000_00000_00000_00000000000));
    }

    // frsub
    @Test
    public void test_frsub() {
        testDecode("frsub", "frsub", Integer.toHexString(0b010110_00000_00000_00000_00010000000));
    }

    // fmul
    @Test
    public void test_fmul() {
        testDecode("fmul", "fmul", Integer.toHexString(0b010110_00000_00000_00000_00100000000));
    }

    // fdiv
    @Test
    public void test_fdiv() {
        testDecode("fdiv", "fdiv", Integer.toHexString(0b010110_00000_00000_00000_00110000000));
    }

    // fcmp_un
    @Test
    public void test_fcmp_un() {
        testDecode("fcmp_un", "fcmp.un", Integer.toHexString(0b010110_00000_00000_00000_0100_000_0000));
    }

    // fcmp_lt
    @Test
    public void test_fcmp_lt() {
        testDecode("fcmp_lt", "fcmp.lt", Integer.toHexString(0b010110_00000_00000_00000_0100_001_0000));
    }

    // fcmp_eq
    @Test
    public void test_fcmp_eq() {
        testDecode("fcmp_eq", "fcmp.eq", Integer.toHexString(0b010110_00000_00000_00000_0100_010_0000));
    }

    // fcmp_le
    @Test
    public void test_fcmp_le() {
        testDecode("fcmp_le", "fcmp.le", Integer.toHexString(0b010110_00000_00000_00000_0100_011_0000));
    }

    // fcmp_gt
    @Test
    public void test_fcmp_gt() {
        testDecode("fcmp_gt", "fcmp.gt", Integer.toHexString(0b010110_00000_00000_00000_0100_100_0000));
    }

    // fcmp_ne
    @Test
    public void test_fcmp_ne() {
        testDecode("fcmp_ne", "fcmp.ne", Integer.toHexString(0b010110_00000_00000_00000_0100_101_0000));
    }

    // fcmp_ge
    @Test
    public void test_fcmp_ge() {
        testDecode("fcmp_ge", "fcmp.ge", Integer.toHexString(0b010110_00000_00000_00000_0100_110_0000));
    }

    // flt
    @Test
    public void test_flt() {
        testDecode("flt", "flt", Integer.toHexString(0b010110_00000_00000_00000_01010000000));
    }

    // fint
    @Test
    public void test_fint() {
        testDecode("fint", "fint", Integer.toHexString(0b010110_00000_00000_00000_01100000000));
    }

    // fsqrt
    @Test
    public void test_fsqrt() {
        testDecode("fsqrt", "fsqrt", Integer.toHexString(0b010110_00000_00000_00000_01110000000));
    }

    // or
    @Test
    public void test_or() {
        testDecode("or", "or", Integer.toHexString(0b100000_00000_00000_00000_00000000000));
    }

    // pcmpbf
    @Test
    public void test_pcmpbf() {
        testDecode("pcmpbf", "pcmpbf", Integer.toHexString(0b100000_00000_00000_00000_10000000000));
    }

    // and
    @Test
    public void test_and() {
        testDecode("and", "and", Integer.toHexString(0b100001_00000_00000_00000_00000000000));
    }

    // xor
    @Test
    public void test_xor() {
        testDecode("xor", "xor", Integer.toHexString(0b100010_00000_00000_00000_00000000000));
    }

    // pcmpeq
    @Test
    public void test_pcmpeq() {
        testDecode("pcmpeq", "pcmpeq", Integer.toHexString(0b100010_00000_00000_00000_10000000000));
    }

    // andn
    @Test
    public void test_andn() {
        testDecode("andn", "andn", Integer.toHexString(0b100011_00000_00000_00000_00000000000));
    }

    // pcmpne
    @Test
    public void test_pcmpne() {
        testDecode("pcmpne", "pcmpne", Integer.toHexString(0b100011_00000_00000_00000_10000000000));
    }

    // sra
    @Test
    public void test_sra() {
        testDecode("sra", "sra", Integer.toHexString(0b100100_00000_00000_0000000000000001));
    }

    // src
    @Test
    public void test_src() {
        testDecode("src", "src", Integer.toHexString(0b100100_00000_00000_0000000000100001));
    }

    // srl
    @Test
    public void test_srl() {
        testDecode("srl", "srl", Integer.toHexString(0b100100_00000_00000_0000000001000001));
    }

    // sext8
    @Test
    public void test_sext8() {
        testDecode("sext8", "sext8", Integer.toHexString(0b100100_00000_00000_0000000001100000));
    }

    // sext16
    @Test
    public void test_sext16() {
        testDecode("sext16", "sext16", Integer.toHexString(0b100100_00000_00000_0000000001100001));
    }

    // clz
    @Test
    public void test_clz() {
        testDecode("clz", "clz", Integer.toHexString(0b100100_00000_00000_0000000011100000));
    }

    // swapb
    @Test
    public void test_swapb() {
        testDecode("swapb", "swapb", Integer.toHexString(0b100100_00000_00000_0000000111100000));
    }

    // swaph
    @Test
    public void test_swaph() {
        testDecode("swaph", "swaph", Integer.toHexString(0b100100_00000_00000_0000000111100010));
    }

    // wic
    @Test
    public void test_wic() {
        testDecode("wic", "wic", Integer.toHexString(0b100100_00000_00000_0000000001101000));
    }

    // wdc
    @Test
    public void test_wdc() {
        testDecode("wdc", "wdc", Integer.toHexString(0b100100_00000_00000_00000_00001100100));
    }

    // wdc_flush
    @Test
    public void test_wdc_flush() {
        testDecode("wdc_flush", "wdc.flush", Integer.toHexString(0b100100_00000_00000_00000_00001110100));
    }

    // wdc_clear
    @Test
    public void test_wdc_clear() {
        testDecode("wdc_clear", "wdc.clear", Integer.toHexString(0b100100_00000_00000_00000_00001100110));
    }

    // wdc_clear_ea
    @Test
    public void test_wdc_clear_ea() {
        testDecode("wdc_clear_ea", "wdc.clear.ea", Integer.toHexString(0b100100_00000_00000_00000_00011100110));
    }

    // wdc_ext_flush
    @Test
    public void test_wdc_ext_flush() {
        testDecode("wdc_ext_flush", "wdc.ext.flush", Integer.toHexString(0b100100_00000_00000_00000_10001110110));
    }

    // wdc_ext_clear
    @Test
    public void test_wdc_ext_clear() {
        testDecode("wdc_ext_clear", "wdc.ext.clear", Integer.toHexString(0b100100_00000_00000_00000_10001100110));
    }

    // lbu
    @Test
    public void test_lbu() {
        testDecode("lbu", "lbu", Integer.toHexString(0b110000_00000_00000_00000_00000000000));
    }

    // lbur
    @Test
    public void test_lbur() {
        testDecode("lbur", "lbur", Integer.toHexString(0b110000_00000_00000_00000_01000000000));
    }

    // lbuea
    @Test
    public void test_lbuea() {
        testDecode("lbuea", "lbuea", Integer.toHexString(0b110000_00000_00000_00000_00010000000));
    }

    // lhu
    @Test
    public void test_lhu() {
        testDecode("lhu", "lhu", Integer.toHexString(0b110001_00000_00000_00000_00000000000));
    }

    // lhur
    @Test
    public void test_lhur() {
        testDecode("lhur", "lhur", Integer.toHexString(0b110001_00000_00000_00000_01000000000));
    }

    // lhuea
    @Test
    public void test_lhuea() {
        testDecode("lhuea", "lhuea", Integer.toHexString(0b110001_00000_00000_00000_00010000000));
    }

    // lw
    @Test
    public void test_lw() {
        testDecode("lw", "lw", Integer.toHexString(0b110010_00000_00000_00000_00000000000));
    }

    // lwr
    @Test
    public void test_lwr() {
        testDecode("lwr", "lwr", Integer.toHexString(0b110010_00000_00000_00000_01000000000));
    }

    // lwx
    @Test
    public void test_lwx() {
        testDecode("lwx", "lwx", Integer.toHexString(0b110010_00000_00000_00000_10000000000));
    }

    // lwea
    @Test
    public void test_lwea() {
        testDecode("lwea", "lwea", Integer.toHexString(0b110010_00000_00000_00000_00010000000));
    }

    // sb
    @Test
    public void test_sb() {
        testDecode("sb", "sb", Integer.toHexString(0b110100_00000_00000_00000_00000000000));
    }

    // sbr
    @Test
    public void test_sbr() {
        testDecode("sbr", "sbr", Integer.toHexString(0b110100_00000_00000_00000_01000000000));
    }

    // sbea
    @Test
    public void test_sbea() {
        testDecode("sbea", "sbea", Integer.toHexString(0b110100_00000_00000_00000_00010000000));
    }

    // sh
    @Test
    public void test_sh() {
        testDecode("sh", "sh", Integer.toHexString(0b110101_00000_00000_00000_00000000000));
    }

    // shr
    @Test
    public void test_shr() {
        testDecode("shr", "shr", Integer.toHexString(0b110101_00000_00000_00000_01000000000));
    }

    // shea
    @Test
    public void test_shea() {
        testDecode("shea", "shea", Integer.toHexString(0b110101_00000_00000_00000_00010000000));
    }

    // sw
    @Test
    public void test_sw() {
        testDecode("sw", "sw", Integer.toHexString(0b110110_00000_00000_00000_00000000000));
    }

    // swr
    @Test
    public void test_swr() {
        testDecode("swr", "swr", Integer.toHexString(0b110110_00000_00000_00000_01000000000));
    }

    // swx
    @Test
    public void test_swx() {
        testDecode("swx", "swx", Integer.toHexString(0b110110_00000_00000_00000_10000000000));
    }

    // swea
    @Test
    public void test_swea() {
        testDecode("swea", "swea", Integer.toHexString(0b110110_00000_00000_00000_00010000000));
    }

    // TYPE_B /////////////////////////////////////////////////////////////////

    // addi
    @Test
    public void test_addi() {
        testDecode("addi", "addi", Integer.toHexString(0b001000_00000_00000_0000000000000000));
    }

    // addic
    @Test
    public void test_addic() {
        testDecode("addic", "addic", Integer.toHexString(0b001010_00000_00000_0000000000000000));
    }

    // addik
    @Test
    public void test_addik() {
        testDecode("addik", "addik", Integer.toHexString(0b001100_00000_00000_0000000000000000));
    }

    // addikc
    @Test
    public void test_addikc() {
        testDecode("addikc", "addikc", Integer.toHexString(0b001110_00000_00000_0000000000000000));
    }

    // rsubi
    @Test
    public void test_rsubi() {
        testDecode("rsubi", "rsubi", Integer.toHexString(0b001001_00000_00000_0000000000000000));
    }

    // rsubic
    @Test
    public void test_rsubic() {
        testDecode("rsubic", "rsubic", Integer.toHexString(0b001011_00000_00000_0000000000000000));
    }

    // rsubik
    @Test
    public void test_rsubik() {
        testDecode("rsubik", "rsubik", Integer.toHexString(0b001101_00000_00000_0000000000000000));
    }

    // rsubikc
    @Test
    public void test_rsubikc() {
        testDecode("rsubikc", "rsubikc", Integer.toHexString(0b001111_00000_00000_0000000000000000));
    }

    // muli
    @Test
    public void test_muli() {
        testDecode("muli", "muli", Integer.toHexString(0b011000_00000_00000_0000000000000000));
    }

    // ori
    @Test
    public void test_ori() {
        testDecode("ori", "ori", Integer.toHexString(0b101000_00000_00000_0000000000000000));
    }

    // andi
    @Test
    public void test_andi() {
        testDecode("andi", "andi", Integer.toHexString(0b101001_00000_00000_0000000000000000));
    }

    // xori
    @Test
    public void test_xori() {
        testDecode("xori", "xori", Integer.toHexString(0b101010_00000_00000_0000000000000000));
    }

    // andni
    @Test
    public void test_andni() {
        testDecode("andni", "andni", Integer.toHexString(0b101011_00000_00000_0000000000000000));
    }

    // lbui
    @Test
    public void test_lbui() {
        testDecode("lbui", "lbui", Integer.toHexString(0b111000_00000_00000_0000000000000000));
    }

    // lhui
    @Test
    public void test_lhui() {
        testDecode("lhui", "lhui", Integer.toHexString(0b111001_00000_00000_0000000000000000));
    }

    // lwi
    @Test
    public void test_lwi() {
        testDecode("lwi", "lwi", Integer.toHexString(0b111010_00000_00000_0000000000000000));
    }

    // sbi
    @Test
    public void test_sbi() {
        testDecode("sbi", "sbi", Integer.toHexString(0b111100_00000_00000_0000000000000000));
    }

    // shi
    @Test
    public void test_shi() {
        testDecode("shi", "shi", Integer.toHexString(0b111101_00000_00000_0000000000000000));
    }

    // swi
    @Test
    public void test_swi() {
        testDecode("swi", "swi", Integer.toHexString(0b111110_00000_00000_0000000000000000));
    }

    private void testDecode(String addr, String name, String expected, String binaryInstruction) {
        MicroBlazeInstruction testinst = MicroBlazeInstruction.newInstance(addr, binaryInstruction);
        MicroBlazeInstructionData idata = testinst.getData();
        MicroBlazeAsmFieldData fieldData = testinst.getFieldData();

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
