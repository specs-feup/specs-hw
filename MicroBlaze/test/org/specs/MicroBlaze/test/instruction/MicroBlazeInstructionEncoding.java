package org.specs.MicroBlaze.test.instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.test.instruction.InstructionEncoding;

/**
 * This enum lists the encodings for all the instructions in the MicroBlaze 32 bit ISA. Only the bits required to define
 * the instruction are set, and all other fields are 0. This enum can be used to power other JUnit tests by doing
 * exhaustive tests on all the ISA encodings.
 * 
 * @author nuno
 *
 */
public enum MicroBlazeInstructionEncoding implements InstructionEncoding {

    // SPECIAL //////////////////////////////////////////////////////////
    mts(Integer.toHexString(0b100101_00_000_00000_11_00000000000000)),
    mtse(Integer.toHexString(0b100101_01_000_00000_11_00000000000000)),
    mfs(Integer.toHexString(0b100101_00000_00000_10_00000000000000)),
    mfse(Integer.toHexString(0b100101_00000_01000_10_00000000000000)),
    msrclr(Integer.toHexString(0b100101_00000_100010_000000000000000)),
    msrset(Integer.toHexString(0b100101_00000_100000_000000000000000)),

    // MBAR ///////////////////////////////////////////////////////////////////
    mbar(Integer.toHexString(0b101110_00000_00010_0000000000000100)),

    // UBRANCH ////////////////////////////////////////////////////////////////
    br(Integer.toHexString(0b100110_00000_000_00_00000_00000000000)),
    bra(Integer.toHexString(0b100110_00000_010_00_00000_00000000000)),
    brd(Integer.toHexString(0b100110_00000_100_00_00000_00000000000)),
    brad(Integer.toHexString(0b100110_00000_110_00_00000_00000000000)),

    // ULBRANCH ///////////////////////////////////////////////////////////////
    brld(Integer.toHexString(0b100110_00000_101_00_00000_00000000000)),
    brald(Integer.toHexString(0b100110_00000_111_00_00000_00000000000)),
    brk(Integer.toHexString(0b100110_00000_011_00_00000_00000000000)),

    // UIBRANCH ///////////////////////////////////////////////////////////////
    bri(Integer.toHexString(0b101110_00000_000_00_00000_00000000000)),
    brai(Integer.toHexString(0b101110_00000_010_00_00000_00000000000)),
    brid(Integer.toHexString(0b101110_00000_100_00_00000_00000000000)),
    braid(Integer.toHexString(0b101110_00000_110_00_00000_00000000000)),

    // UILBRANCH //////////////////////////////////////////////////////////////
    brlid(Integer.toHexString(0b101110_00000_101_00_00000_00000000000)),
    bralid(Integer.toHexString(0b101110_00000_111_00_00000_00000000000)),
    brki(Integer.toHexString(0b101110_00000_011_00_00000_00000000000)),

    // CBRANCH ////////////////////////////////////////////////////////////////
    beq(Integer.toHexString(0b100111_0_0000_00000_00000_00000000000)),
    beqd(Integer.toHexString(0b100111_1_0000_00000_00000_00000000000)),
    bge(Integer.toHexString(0b100111_00101_00000_00000_00000000000)),
    bged(Integer.toHexString(0b100111_10101_00000_00000_00000000000)),
    bgt(Integer.toHexString(0b100111_00100_00000_00000_00000000000)),
    bgtd(Integer.toHexString(0b100111_10100_00000_00000_00000000000)),
    ble(Integer.toHexString(0b100111_00011_00000_00000_00000000000)),
    bled(Integer.toHexString(0b100111_10011_00000_00000_00000000000)),
    blt(Integer.toHexString(0b100111_00010_00000_00000_00000000000)),
    bltd(Integer.toHexString(0b100111_10010_00000_00000_00000000000)),
    bne(Integer.toHexString(0b100111_00001_00000_00000_00000000000)),
    bned(Integer.toHexString(0b100111_10001_00000_00000_00000000000)),

    // CIBRANCH ////////////////////////////////////////////////////////////////
    beqi(Integer.toHexString(0b101111_0_0000_00000_00000_00000000000)),
    beqid(Integer.toHexString(0b101111_1_0000_00000_00000_00000000000)),
    bgei(Integer.toHexString(0b101111_00101_00000_00000_00000000000)),
    bgeid(Integer.toHexString(0b101111_10101_00000_00000_00000000000)),
    bgti(Integer.toHexString(0b101111_00100_00000_00000_00000000000)),
    bgtid(Integer.toHexString(0b101111_10100_00000_00000_00000000000)),
    blei(Integer.toHexString(0b101111_00011_00000_00000_00000000000)),
    bleid(Integer.toHexString(0b101111_10011_00000_00000_00000000000)),
    blti(Integer.toHexString(0b101111_00010_00000_00000_00000000000)),
    bltid(Integer.toHexString(0b101111_10010_00000_00000_00000000000)),
    bnei(Integer.toHexString(0b101111_00001_00000_00000_00000000000)),
    bneid(Integer.toHexString(0b101111_10001_00000_00000_00000000000)),

    // RETURN /////////////////////////////////////////////////////////////////
    rtbd(Integer.toHexString(0b101101_10010_00000_0000000000000000)),
    rtid(Integer.toHexString(0b101101_10001_00000_0000000000000000)),
    rted(Integer.toHexString(0b101101_10100_00000_0000000000000000)),
    rtsd(Integer.toHexString(0b101101_10000_00000_0000000000000000)),

    // IBARREL ////////////////////////////////////////////////////////////////
    bsrli(Integer.toHexString(0b011001_00000_00000_00000_00_0000_00000)),
    bsrai(Integer.toHexString(0b011001_00000_00000_00000_01_0000_00000)),
    bslli(Integer.toHexString(0b011001_00000_00000_00000_10_0000_00000)),
    bsefi(Integer.toHexString(0b011001_00000_00000_01_000_00000_0_00000)),
    bsifi(Integer.toHexString(0b011001_00000_00000_10_000_00000_0_00000)),

    // STREAM
    put(Integer.toHexString(0b011011_00000_00000_1_0000_0000000_0000)),
    get(Integer.toHexString(0b011011_00000_00000_0_0000_0000000_0000)),

    // DSTREAM
    putd(Integer.toHexString(0b010011_00000_00000_1_0000_0000000_0000)),
    getd(Integer.toHexString(0b010011_00000_00000_0_0000_0000000_0000)),

    // IMM ////////////////////////////////////////////////////////////////////
    imm(Integer.toHexString(0b101100_00000_00000_0000000000000000)),

    // TYPE_A /////////////////////////////////////////////////////////////////
    add(Integer.toHexString(0b000000_00000_00000_00000_00000000000)),
    addc(Integer.toHexString(0b000010_00000_00000_00000_00000000000)),
    addk(Integer.toHexString(0b000100_00000_00000_00000_00000000000)),
    addkc(Integer.toHexString(0b000110_00000_00000_00000_00000000000)),
    rsub(Integer.toHexString(0b000001_00000_00000_00000_00000000000)),
    rsubc(Integer.toHexString(0b000011_00000_00000_00000_00000000000)),
    rsubk(Integer.toHexString(0b000101_00000_00000_00000_00000000000)),
    rsubkc(Integer.toHexString(0b000111_00000_00000_00000_00000000000)),
    cmp(Integer.toHexString(0b00101_00000_00000_00000_00000000001)),
    cmpu(Integer.toHexString(0b00101_00000_00000_00000_00000000011)),
    mul(Integer.toHexString(0b010000_00000_00000_00000_00000000000)),
    mulh(Integer.toHexString(0b010000_00000_00000_00000_00000000001)),
    mulhu(Integer.toHexString(0b010000_00000_00000_00000_00000000011)),
    mulhsu(Integer.toHexString(0b010000_00000_00000_00000_00000000010)),
    bsrl(Integer.toHexString(0b010001_00000_00000_00000_00000000000)),
    bsra(Integer.toHexString(0b010001_00000_00000_00000_01000000000)),
    bsll(Integer.toHexString(0b010001_00000_00000_00000_10000000000)),
    idiv(Integer.toHexString(0b010010_00000_00000_00000_00000000000)),
    idivu(Integer.toHexString(0b010010_00000_00000_00000_00000000010)),
    fadd(Integer.toHexString(0b010110_00000_00000_00000_00000000000)),
    frsub(Integer.toHexString(0b010110_00000_00000_00000_00010000000)),
    fmul(Integer.toHexString(0b010110_00000_00000_00000_00100000000)),
    fdiv(Integer.toHexString(0b010110_00000_00000_00000_00110000000)),
    fcmp_un(Integer.toHexString(0b010110_00000_00000_00000_0100_000_0000)),
    fcmp_lt(Integer.toHexString(0b010110_00000_00000_00000_0100_001_0000)),
    fcmp_eq(Integer.toHexString(0b010110_00000_00000_00000_0100_010_0000)),
    fcmp_le(Integer.toHexString(0b010110_00000_00000_00000_0100_011_0000)),
    fcmp_gt(Integer.toHexString(0b010110_00000_00000_00000_0100_100_0000)),
    fcmp_ne(Integer.toHexString(0b010110_00000_00000_00000_0100_101_0000)),
    fcmp_ge(Integer.toHexString(0b010110_00000_00000_00000_0100_110_0000)),
    flt(Integer.toHexString(0b010110_00000_00000_00000_01010000000)),
    fint(Integer.toHexString(0b010110_00000_00000_00000_01100000000)),
    fsqrt(Integer.toHexString(0b010110_00000_00000_00000_01110000000)),
    or(Integer.toHexString(0b100000_00000_00000_00000_00000000000)),
    pcmpbf(Integer.toHexString(0b100000_00000_00000_00000_10000000000)),
    and(Integer.toHexString(0b100001_00000_00000_00000_00000000000)),
    xor(Integer.toHexString(0b100010_00000_00000_00000_00000000000)),
    pcmpeq(Integer.toHexString(0b100010_00000_00000_00000_10000000000)),
    andn(Integer.toHexString(0b100011_00000_00000_00000_00000000000)),
    pcmpne(Integer.toHexString(0b100011_00000_00000_00000_10000000000)),
    sra(Integer.toHexString(0b100100_00000_00000_0000000000000001)),
    src(Integer.toHexString(0b100100_00000_00000_0000000000100001)),
    srl(Integer.toHexString(0b100100_00000_00000_0000000001000001)),
    sext8(Integer.toHexString(0b100100_00000_00000_0000000001100000)),
    sext16(Integer.toHexString(0b100100_00000_00000_0000000001100001)),
    clz(Integer.toHexString(0b100100_00000_00000_0000000011100000)),
    swapb(Integer.toHexString(0b100100_00000_00000_0000000111100000)),
    swaph(Integer.toHexString(0b100100_00000_00000_0000000111100010)),
    wic(Integer.toHexString(0b100100_00000_00000_0000000001101000)),
    wdc(Integer.toHexString(0b100100_00000_00000_00000_00001100100)),
    wdc_flush("wdc.flush", Integer.toHexString(0b100100_00000_00000_00000_00001110100)),
    wdc_clear("wdc.clear", Integer.toHexString(0b100100_00000_00000_00000_00001100110)),
    wdc_clear_ea("wdc.clear.ea", Integer.toHexString(0b100100_00000_00000_00000_00011100110)),
    wdc_ext_flush("wdc.ext.flush", Integer.toHexString(0b100100_00000_00000_00000_10001110110)),
    wdc_ext_clear("wdc.ext.clear", Integer.toHexString(0b100100_00000_00000_00000_10001100110)),
    lbu(Integer.toHexString(0b110000_00000_00000_00000_00000000000)),
    lbur(Integer.toHexString(0b110000_00000_00000_00000_01000000000)),
    lbuea(Integer.toHexString(0b110000_00000_00000_00000_00010000000)),
    lhu(Integer.toHexString(0b110001_00000_00000_00000_00000000000)),
    lhur(Integer.toHexString(0b110001_00000_00000_00000_01000000000)),
    lhuea(Integer.toHexString(0b110001_00000_00000_00000_00010000000)),
    lw(Integer.toHexString(0b110010_00000_00000_00000_00000000000)),
    lwr(Integer.toHexString(0b110010_00000_00000_00000_01000000000)),
    lwx(Integer.toHexString(0b110010_00000_00000_00000_10000000000)),
    lwea(Integer.toHexString(0b110010_00000_00000_00000_00010000000)),
    sb(Integer.toHexString(0b110100_00000_00000_00000_00000000000)),
    sbr(Integer.toHexString(0b110100_00000_00000_00000_01000000000)),
    sbea(Integer.toHexString(0b110100_00000_00000_00000_00010000000)),
    sh(Integer.toHexString(0b110101_00000_00000_00000_00000000000)),
    shr(Integer.toHexString(0b110101_00000_00000_00000_01000000000)),
    shea(Integer.toHexString(0b110101_00000_00000_00000_00010000000)),
    sw(Integer.toHexString(0b110110_00000_00000_00000_00000000000)),
    swr(Integer.toHexString(0b110110_00000_00000_00000_01000000000)),
    swx(Integer.toHexString(0b110110_00000_00000_00000_10000000000)),
    swea(Integer.toHexString(0b110110_00000_00000_00000_00010000000)),

    // TYPE_B /////////////////////////////////////////////////////////////////
    addi(Integer.toHexString(0b001000_00000_00000_0000000000000000)),
    addic(Integer.toHexString(0b001010_00000_00000_0000000000000000)),
    addik(Integer.toHexString(0b001100_00000_00000_0000000000000000)),
    addikc(Integer.toHexString(0b001110_00000_00000_0000000000000000)),
    rsubi(Integer.toHexString(0b001001_00000_00000_0000000000000000)),
    rsubic(Integer.toHexString(0b001011_00000_00000_0000000000000000)),
    rsubik(Integer.toHexString(0b001101_00000_00000_0000000000000000)),
    rsubikc(Integer.toHexString(0b001111_00000_00000_0000000000000000)),
    muli(Integer.toHexString(0b011000_00000_00000_0000000000000000)),
    ori(Integer.toHexString(0b101000_00000_00000_0000000000000000)),
    andi(Integer.toHexString(0b101001_00000_00000_0000000000000000)),
    xori(Integer.toHexString(0b101010_00000_00000_0000000000000000)),
    andni(Integer.toHexString(0b101011_00000_00000_0000000000000000)),
    lbui(Integer.toHexString(0b111000_00000_00000_0000000000000000)),
    lhui(Integer.toHexString(0b111001_00000_00000_0000000000000000)),
    lwi(Integer.toHexString(0b111010_00000_00000_0000000000000000)),
    sbi(Integer.toHexString(0b111100_00000_00000_0000000000000000)),
    shi(Integer.toHexString(0b111101_00000_00000_0000000000000000)),
    swi(Integer.toHexString(0b111110_00000_00000_0000000000000000)),

    unknown(Integer.toHexString(0b111111_11111_11111_1111111111111111));

    private String instname;
    private String code;

    private MicroBlazeInstructionEncoding(String code) {
        this.instname = name();
        this.code = code;
    }

    private MicroBlazeInstructionEncoding(String instname, String code) {
        this.instname = instname;
        this.code = code;
    }

    @Override
    public String getName() {
        return this.instname;
    }

    @Override
    public String getCode() {
        return code;
    }

    /*
     * Return a list of encodings for instructions that obey a certain predicate (e.g., name)
     */
    public static List<MicroBlazeInstructionEncoding> getCodes(Predicate<InstructionEncoding> predicate) {
        var list = new ArrayList<MicroBlazeInstructionEncoding>();
        for (var encode : MicroBlazeInstructionEncoding.values()) {
            if (predicate.test(encode))
                list.add(encode);
        }
        return list;
    }
}
