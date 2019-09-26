package org.specs.MicroBlaze.v2;

import static pt.up.fe.specs.binarytranslation.InstructionType.G_ADD;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_AJUMP;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_CJUMP;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_FLOAT;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_IJUMP;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_LOAD;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_LOGICAL;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_MUL;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_OTHER;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_RJUMP;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_STORE;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_SUB;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_UJUMP;
import static pt.up.fe.specs.binarytranslation.InstructionType.G_UNARY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.InstructionType;

public enum MicroBlazeInstructionSetFields {

    // TODO add fields: nr inputs, nr outputs, has delay slot (bool)

    // Addition/Subtraction Operations
    add(0b0000_0000_0000_0000, 1, 0, G_ADD),
    addc(0b0000_1000_0000_0000, 1, 0, G_ADD),
    addk(0b0001_0000_0000_0000, 1, 0, G_ADD),
    addkc(0b0001_1000_0000_0000, 1, 0, G_ADD),
    addi(0b0010_0000_0000_0000, 1, 0, G_ADD),
    addic(0b0010_1000_0000_0000, 1, 0, G_ADD),
    addik(0b0011_0000_0000_0000, 1, 0, G_ADD),
    addikc(0b0011_1000_0000_0000, 1, 0, G_ADD),
    rsub(0b0000_0100_0000_0000, 1, 0, G_SUB),
    rsubc(0b0000_1100_0000_0000, 1, 0, G_SUB),
    rsubk(0b0001_0100_0000_0000, 1, 0, G_SUB),
    rsubkc(0b0001_1100_0000_0000, 1, 0, G_SUB),
    rsubi(0b0010_0100_0000_0000, 1, 0, G_SUB),
    rsubic(0b0010_1100_0000_0000, 1, 0, G_SUB),
    rsubik(0b0011_0100_0000_0000, 1, 0, G_SUB),
    rsubikc(0b0011_1100_0000_0000, 1, 0, G_SUB),

    // Multiplication
    mul(0b0100_0000_0000_0000, 3, 0, G_MUL),
    mulh(0b0100_0000_0000_0000, 3, 0, G_MUL),
    mulhu(0b0100_0000_0000_0000, 3, 0, G_MUL),
    mulhsu(0b0100_0000_0000_0000, 3, 0, G_MUL),
    muli(0b0110_0000_0000_0000, 3, 0, G_MUL),
    // TODO mulh has instruction bit on bit 31...

    // Logical Operations
    and(0b1000_0100_0000_0000, 1, 0, G_LOGICAL),
    andi(0b1010_0100_0000_0000, 1, 0, G_LOGICAL),
    andn(0b1000_1100_0000_0000, 1, 0, G_LOGICAL),
    andni(0b1010_1100_0000_0000, 1, 0, G_LOGICAL),
    bsrl(0b0100_0100_0000_0000, 1, 0, G_LOGICAL),
    bsra(0b0100_0100_0000_0000, 1, 0, G_LOGICAL),
    bsll(0b0100_0100_0000_0000, 1, 0, G_LOGICAL),
    bsrli(0b0110_0100_0000_0000, 1, 0, G_LOGICAL),
    bsrai(0b0110_0100_0000_0000, 1, 0, G_LOGICAL),
    bslli(0b0110_0100_0000_0000, 1, 0, G_LOGICAL),
    or(0b1000_0000_0000_0000, 1, 0, G_LOGICAL),
    ori(0b1010_0000_0000_0000, 1, 0, G_LOGICAL),
    xor(0b1000_1000_0000_0000, 1, 0, G_LOGICAL),
    xori(0b1010_1000_0000_0000, 1, 0, G_LOGICAL),
    // TODO shifts have instruction bits on bits 21 and 22...

    // Unary Logical Operations
    sra(0b1001_0000_0000_0000, 1, 0, G_UNARY, G_LOGICAL),
    src(0b1001_0000_0000_0000, 1, 0, G_UNARY, G_LOGICAL),
    srl(0b1001_0000_0000_0000, 1, 0, G_UNARY, G_LOGICAL),
    // TODO shifts have instruction bits on bits 26...

    // Conditional Jumps
    beq(0b1001_1100_0000_0000, 1, 0, G_CJUMP, G_RJUMP),
    beqd(0b1001_1110_0000_0000, 1, 0, G_CJUMP, G_RJUMP),
    beqi(0b1011_1100_0000_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    beqid(0b1011_1110_0000_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bge(0b1001_1100_1010_0000, 1, 0, G_CJUMP, G_RJUMP),
    bged(0b1001_1111_1010_0000, 1, 1, G_CJUMP, G_RJUMP),
    bgei(0b1011_1100_1010_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bgeid(0b1011_1110_1010_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bgt(0b1001_1100_1000_0000, 1, 0, G_CJUMP, G_RJUMP),
    bgtd(0b1001_1110_1000_0000, 1, 1, G_CJUMP, G_RJUMP),
    bgti(0b1011_1100_1000_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bgtid(0b1011_1110_1000_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    ble(0b1001_1100_0110_0000, 1, 0, G_CJUMP, G_RJUMP),
    bled(0b1001_1110_0110_0000, 1, 1, G_CJUMP, G_RJUMP),
    blei(0b1011_1100_0110_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bleid(0b1011_1110_0110_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    blt(0b1001_1100_0100_0000, 1, 0, G_CJUMP, G_RJUMP),
    bltd(0b1001_1110_0100_0000, 1, 1, G_CJUMP, G_RJUMP),
    blti(0b1011_1100_0100_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bltid(0b1011_1110_0100_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bne(0b1001_1100_0010_0000, 1, 0, G_CJUMP, G_RJUMP),
    bned(0b1001_1110_0010_0000, 1, 0, G_CJUMP, G_RJUMP),
    bnei(0b1011_1100_0010_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bneid(0b1011_1110_0010_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    // TODO check latencies

    // Unconditional Jumps
    br(0b1001_1000_0000_0000, 1, 0, G_UJUMP, G_RJUMP),
    bra(0b1001_1000_0000_0000, 1, 0, G_UJUMP, G_AJUMP),
    brd(0b1001_1000_0000_0000, 1, 1, G_UJUMP, G_RJUMP),
    brad(0b1001_1000_0001_0000, 1, 1, G_UJUMP, G_AJUMP),
    brld(0b1001_1000_0000_0000, 1, 1, G_UJUMP, G_RJUMP),
    brald(0b1001_1000_0001_0000, 1, 1, G_UJUMP, G_AJUMP),
    bri(0b1011_1000_0000_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP),
    brai(0b1011_1000_0000_0000, 1, 0, G_UJUMP, G_AJUMP, G_IJUMP),
    brid(0b1011_1000_0001_0000, 1, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    braid(0b1011_1000_0001_0000, 1, 1, G_UJUMP, G_AJUMP, G_IJUMP),
    brlid(0b1011_1000_0001_0000, 1, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    bralid(0b1011_1000_0001_0000, 1, 1, G_UJUMP, G_AJUMP, G_IJUMP),
    brk(0b1001_1000_0000_0000, 1, 0, G_UJUMP, G_RJUMP),
    brki(0b1011_1000_0000_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP),
    rtbd(0b1011_0100_0000_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP),
    rtid(0b1011_0100_0000_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP),
    rted(0b1011_0100_0000_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP),
    rtsd(0b1011_0100_0000_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP),
    // TODO check latencies

    // Floating Point
    fadd(0b0101_1000_0000_0000, 6, 0, G_FLOAT, G_ADD),
    frsub(0b0101_10, 6, 0, G_FLOAT, G_SUB),
    fmul(0b0101_10, 6, 0, G_FLOAT, G_MUL),
    fdiv(0b0101_10, 30, 0, G_FLOAT),
    fcmp_un(0b0101_10, 3, 0, G_FLOAT),
    fcmp_lt(0b0101_10, 3, 0, G_FLOAT),
    fcmp_eq(0b0101_10, 3, 0, G_FLOAT),
    fcmp_le(0b0101_10, 3, 0, G_FLOAT),
    fcmp_gt(0b0101_10, 3, 0, G_FLOAT),
    fcmp_ne(0b0101_10, 3, 0, G_FLOAT),
    fcmp_ge(0b0101_10, 3, 0, G_FLOAT),
    flt(0b0101_10, 6, 0, G_FLOAT),
    fint(0b0101_10, 5, 0, G_FLOAT),
    fsqrt(0b0101_10, 29, 0, G_FLOAT),

    // Store
    sb(0b1101_00, 2, 0, G_STORE),
    sbr(0b1101_00, 2, 0, G_STORE),
    sbi(0b1111_00, 2, 0, G_STORE),
    sh(0b1101_01, 2, 0, G_STORE),
    shr(0b1101_01, 2, 0, G_STORE),
    shi(0b1111_01, 2, 0, G_STORE),
    sw(0b1101_10, 2, 0, G_STORE),
    swr(0b1101_10, 2, 0, G_STORE),
    swi(0b1111_10, 2, 0, G_STORE),
    swx(0b1101_10, 2, 0, G_STORE),

    // Load
    lbu(0b1100_00, 2, 0, G_LOAD),
    lbur(0b1100_00, 2, 0, G_LOAD),
    lbui(0b1110_00, 2, 0, G_LOAD),
    lhu(0b1100_01, 2, 0, G_LOAD),
    lhur(0b1100_01, 2, 0, G_LOAD),
    lhui(0b1110_01, 2, 0, G_LOAD),
    lw(0b1100_10, 2, 0, G_LOAD),
    lwr(0b1100_10, 2, 0, G_LOAD),
    lwi(0b1110_10, 2, 0, G_LOAD),
    lwx(0b1100_10, 2, 0, G_LOAD),

    // Others
    idiv(0b0100_1000_0000_0000, 34, 0, G_OTHER),
    idivu(0b0100_1000_0000_0000, 34, 0, G_OTHER),
    cmp(0b0001_000_0000_00001, 1, 0, G_OTHER),
    cmpu(0b0001_0100_0000_0000, 1, 0, G_OTHER),
    pcmpbf(0b1000_0000_0000_0000, 1, 0, G_OTHER),
    pcmpeq(0b1000_1000_0000_0000, 1, 0, G_OTHER),
    pcmpne(0b1000_1100_0000_0000, 1, 0, G_OTHER),
    mbar(0b1011_1000_0000_0000, 1, 0, G_OTHER),
    mfs(0b1001_0100_0000_0000, 1, 0, G_OTHER),
    msrclr(0b1001_0100_0000_0000, 1, 0, G_OTHER),
    msrset(0b1001_0100_0000_0000, 1, 0, G_OTHER),
    mts(0b1001_0100_0000_0000, 1, 0, G_OTHER),
    imm(0b1011_0000_0000_0000, 1, 0, G_OTHER),
    clz(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    get(0b0110_1100_0000_0000, 1, 0, G_OTHER),
    getd(0b0100_1100_0000_0000, 1, 0, G_OTHER),
    put(0b0110_1100_0000_0000, 2, 0, G_OTHER),
    putd(0b0100_1100_0000_0000, 2, 0, G_OTHER),
    sext16(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    sext8(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    swapb(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    swaph(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    wdc(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    wdc_flush(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    wdc_clear(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    wdc_ext_flush(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    wdc_ext_clear(0b1001_0000_0000_0000, 1, 0, G_OTHER),
    wic(0b1001_0000_0000_0000, 1, 0, G_OTHER);

    /*
     * Instruction property fields
     */
    // private final int opcodebitmask = 0b11111100000000000000000000000000;
    private final String instructionName;
    private final int opcode;
    private final int latency;
    private final int delay;
    private final List<InstructionType> genericType;

    // private final int nrInputs = 2; // TODO fix
    // private final int nrOutputs = 1; // TODO fix
    // private final boolean hasDelay = false;

    /*
     * Constructor
     */
    private MicroBlazeInstructionSetFields(int opcode, int latency, int delay, InstructionType... tp) {
        this.instructionName = name();
        this.opcode = opcode;
        this.latency = latency;
        this.delay = delay;
        this.genericType = Arrays.asList(tp);
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
    private int getDelay() {
        return this.delay;
    }

    /*
     * Private helper method too look up opcode in the list
     */
    private int getOpCode() {
        return this.opcode;
    }

    /*
     * Private helper method too look up type in the list
     */
    private List<InstructionType> getGenericType() {
        return this.genericType;
    }

    /*
     * Private helper method too look up name the list
     */
    private String getName() {
        return this.instructionName;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static String getName(long fullopcode) {
        long opcode = (fullopcode) >> (32 - 6);
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getName();
        }
        return "Unknown Instruction!";
        // TODO throw something here
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static List<InstructionType> getGenericType(long fullopcode) {
        long opcode = (fullopcode) >> (32 - 6);
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getGenericType();
        }
        // return InstructionType.unknownType;
        List<InstructionType> ret = new ArrayList<InstructionType>();
        ret.add(InstructionType.G_UNKN);
        return ret;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static int getLatency(long fullopcode) {
        long opcode = (fullopcode) >> (32 - 6);
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getLatency();
        }
        return -1; // TODO replace with exception
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static int getDelay(long fullopcode) {
        long opcode = (fullopcode) >> (32 - 6);
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getDelay();
        }
        return -1; // TODO replace with exception
    }
}
