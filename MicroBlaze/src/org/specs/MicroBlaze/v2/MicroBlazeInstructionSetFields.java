package org.specs.MicroBlaze.v2;

import pt.up.fe.specs.binarytranslation.InstructionSetFields;
import pt.up.fe.specs.binarytranslation.InstructionType;

public enum MicroBlazeInstructionSetFields implements InstructionSetFields {

    // TODO add fields: nr inputs, nr outputs, has delayslot (bool)

    // Addition/Subtraction Operations
    add(0b000000, 1, InstructionType.add),
    addc(0b000010, 1, InstructionType.add),
    addk(0b000100, 1, InstructionType.add),
    addkc(0b000110, 1, InstructionType.add),
    addi(0b001000, 1, InstructionType.add),
    addic(0b001010, 1, InstructionType.add),
    addik(0b001100, 1, InstructionType.add),
    addikc(0b001110, 1, InstructionType.add),
    rsub(0b000001, 1, InstructionType.sub),
    rsubc(0b000011, 1, InstructionType.sub),
    rsubk(0b000101, 1, InstructionType.sub),
    rsubkc(0b000111, 1, InstructionType.sub),
    rsubi(0b001001, 1, InstructionType.sub),
    rsubic(0b001011, 1, InstructionType.sub),
    rsubik(0b001101, 1, InstructionType.sub),
    rsubikc(0b001111, 1, InstructionType.sub),

    // Multiplication
    mul(0b010000, 3, InstructionType.mul),
    mulh(0b010000, 3, InstructionType.mul),
    mulhu(0b010000, 3, InstructionType.mul),
    mulhsu(0b010000, 3, InstructionType.mul),
    muli(0b011000, 3, InstructionType.mul),

    // Logical Operations
    and(0b100001, 1, InstructionType.logical),
    andi(0b101001, 1, InstructionType.logical),
    andn(0b100011, 1, InstructionType.logical),
    andni(0b101011, 1, InstructionType.logical),
    bsrl(0b010001, 1, InstructionType.logical),
    bsra(0b010001, 1, InstructionType.logical),
    bsll(0b010001, 1, InstructionType.logical),
    bsrli(0b011001, 1, InstructionType.logical),
    bsrai(0b011001, 1, InstructionType.logical),
    bslli(0b011001, 1, InstructionType.logical),
    or(0b100000, 1, InstructionType.logical),
    ori(0b101000, 1, InstructionType.logical),
    xor(0b100010, 1, InstructionType.logical),
    xori(0b101010, 1, InstructionType.logical),

    // Unary Logical Operations
    sra(0b100100, 1, InstructionType.cjump),
    src(0b100100, 1, InstructionType.cjump),
    srl(0b100100, 1, InstructionType.cjump),

    // Jump Operations (Branches)
    beq(0b100111, 1, InstructionType.cjump),
    beqd(0b100111, 1, InstructionType.cjump),
    beqi(0b101111, 1, InstructionType.cjump),
    beqid(0b101111, 1, InstructionType.cjump),
    bge(0b100111, 1, InstructionType.cjump),
    bged(0b100111, 1, InstructionType.cjump),
    bgei(0b101111, 1, InstructionType.cjump),
    bgeid(0b101111, 1, InstructionType.cjump),
    bgt(0b100111, 1, InstructionType.cjump),
    bgtd(0b100111, 1, InstructionType.cjump),
    bgti(0b101111, 1, InstructionType.cjump),
    bgtid(0b101111, 1, InstructionType.cjump),
    ble(0b100111, 1, InstructionType.cjump),
    bled(0b100111, 1, InstructionType.cjump),
    blei(0b101111, 1, InstructionType.cjump),
    bleid(0b101111, 1, InstructionType.cjump),
    blt(0b100111, 1, InstructionType.cjump),
    bltd(0b100111, 1, InstructionType.cjump),
    blti(0b101111, 1, InstructionType.cjump),
    bltid(0b101111, 1, InstructionType.cjump),
    bne(0b100111, 1, InstructionType.cjump),
    bned(0b100111, 1, InstructionType.cjump),
    bnei(0b101111, 1, InstructionType.cjump),
    bneid(0b101111, 1, InstructionType.cjump),
    // TODO check latencies

    // Unconditional Jumps
    br(0b100110, 1, InstructionType.ujump),
    bra(0b100110, 1, InstructionType.ujump),
    brd(0b100110, 1, InstructionType.ujump),
    brad(0b100110, 1, InstructionType.ujump),
    brld(0b100110, 1, InstructionType.ujump),
    brald(0b100110, 1, InstructionType.ujump),
    bri(0b101110, 1, InstructionType.ujump),
    brai(0b101110, 1, InstructionType.ujump),
    brid(0b101110, 1, InstructionType.ujump),
    braid(0b101110, 1, InstructionType.ujump),
    brlid(0b101110, 1, InstructionType.ujump),
    bralid(0b101110, 1, InstructionType.ujump),
    brk(0b100110, 1, InstructionType.ujump),
    brki(0b101110, 1, InstructionType.ujump),
    rtbd(0b101101, 1, InstructionType.ujump),
    rtid(0b101101, 1, InstructionType.ujump),
    rted(0b101101, 1, InstructionType.ujump),
    rtsd(0b101101, 1, InstructionType.ujump),
    // TODO check latencies

    // Floating Point
    fadd(0b010110, 6, InstructionType.floatingpoint),
    frsub(0b010110, 6, InstructionType.floatingpoint),
    fmul(0b010110, 6, InstructionType.floatingpoint),
    fdiv(0b010110, 30, InstructionType.floatingpoint),
    fcmp_un(0b010110, 3, InstructionType.floatingpoint),
    fcmp_lt(0b010110, 3, InstructionType.floatingpoint),
    fcmp_eq(0b010110, 3, InstructionType.floatingpoint),
    fcmp_le(0b010110, 3, InstructionType.floatingpoint),
    fcmp_gt(0b010110, 3, InstructionType.floatingpoint),
    fcmp_ne(0b010110, 3, InstructionType.floatingpoint),
    fcmp_ge(0b010110, 3, InstructionType.floatingpoint),
    flt(0b010110, 6, InstructionType.floatingpoint),
    fint(0b010110, 5, InstructionType.floatingpoint),
    fsqrt(0b010110, 29, InstructionType.floatingpoint),

    // Store
    sb(0b110100, 2, InstructionType.store),
    sbr(0b110100, 2, InstructionType.store),
    sbi(0b111100, 2, InstructionType.store),
    sh(0b110101, 2, InstructionType.store),
    shr(0b110101, 2, InstructionType.store),
    shi(0b111101, 2, InstructionType.store),
    sw(0b110110, 2, InstructionType.store),
    swr(0b110110, 2, InstructionType.store),
    swi(0b111110, 2, InstructionType.store),
    swx(0b110110, 2, InstructionType.store),

    // Load
    lbu(0b110000, 2, InstructionType.load),
    lbur(0b110000, 2, InstructionType.load),
    lbui(0b111000, 2, InstructionType.load),
    lhu(0b110001, 2, InstructionType.load),
    lhur(0b110001, 2, InstructionType.load),
    lhui(0b111001, 2, InstructionType.load),
    lw(0b110010, 2, InstructionType.load),
    lwr(0b110010, 2, InstructionType.load),
    lwi(0b111010, 2, InstructionType.load),
    lwx(0b110010, 2, InstructionType.load),

    // Others
    idiv(0b010010, 34, InstructionType.other),
    idivu(0b010010, 34, InstructionType.other),
    cmp(0b000101, 1, InstructionType.other),
    cmpu(0b000101, 1, InstructionType.other),
    pcmpbf(0b100000, 1, InstructionType.other),
    pcmpeq(0b100010, 1, InstructionType.other),
    pcmpne(0b100011, 1, InstructionType.other),
    mbar(0b101110, 1, InstructionType.other),
    mfs(0b100101, 1, InstructionType.other),
    msrclr(0b100101, 1, InstructionType.other),
    msrset(0b100101, 1, InstructionType.other),
    mts(0b100101, 1, InstructionType.other),
    imm(0b101100, 1, InstructionType.other),
    clz(0b100100, 1, InstructionType.other),
    get(0b011011, 1, InstructionType.other),
    getd(0b010011, 1, InstructionType.other),
    put(0b011011, 2, InstructionType.other),
    putd(0b010011, 2, InstructionType.other),
    sext16(0b100100, 1, InstructionType.other),
    sext8(0b100100, 1, InstructionType.other),
    swapb(0b100100, 1, InstructionType.other),
    swaph(0b100100, 1, InstructionType.other),
    wdc(0b100100, 1, InstructionType.other),
    wdc_flush(0b100100, 1, InstructionType.other),
    wdc_clear(0b100100, 1, InstructionType.other),
    wdc_ext_flush(0b100100, 1, InstructionType.other),
    wdc_ext_clear(0b100100, 1, InstructionType.other),
    wic(0b100100, 1, InstructionType.other);

    /*
     * Instruction property fields
     */
    // private final int opcodebitmask = 0b11111100000000000000000000000000;
    private final String instructionName;
    private final int opcode;
    private final int latency;
    private final InstructionType genericType;
    private final int nrInputs = 2; // TODO fix
    private final int nrOutputs = 1; // TODO fix
    private final boolean hasDelay = false;

    /*
     * Constructor
     */
    private MicroBlazeInstructionSetFields(int opcode, int latency, InstructionType tp) {
        this.instructionName = name();
        this.opcode = opcode;
        this.latency = latency;
        this.genericType = tp;
    }

    /*
     * Private helper method too look up the list
     */
    private int getLatency() {
        return this.latency;
    }

    /*
     * Private helper method too look up the list
     */
    private int getOpCode() {
        return this.opcode;
    }

    /*
     * Private helper method too look up the list
     */
    private InstructionType getGenericType() {
        return this.genericType;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static InstructionType getGenericType(int fullopcode) {
        int opcode = (fullopcode) >> (32 - 6);
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getGenericType();
        }
        return InstructionType.unknownType;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static int getLatency(int fullopcode) {
        int opcode = (fullopcode) >> (32 - 6);
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getLatency();
        }
        return -1; // TODO replace with exception
    }
}
