package org.specs.MicroBlaze.v2;

import static pt.up.fe.specs.binarytranslation.InstructionType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.InstructionType;

public enum MicroBlazeInstructionSetFields {

    // TODO add fields: nr inputs, nr outputs, has delay slot (bool)

    // Addition/Subtraction Operations
    add(0b000000, 1, G_ADD),
    addc(0b000010, 1, G_ADD),
    addk(0b000100, 1, G_ADD),
    addkc(0b000110, 1, G_ADD),
    addi(0b001000, 1, G_ADD),
    addic(0b001010, 1, G_ADD),
    addik(0b001100, 1, G_ADD),
    addikc(0b001110, 1, G_ADD),
    rsub(0b000001, 1, G_SUB),
    rsubc(0b000011, 1, G_SUB),
    rsubk(0b000101, 1, G_SUB),
    rsubkc(0b000111, 1, G_SUB),
    rsubi(0b001001, 1, G_SUB),
    rsubic(0b001011, 1, G_SUB),
    rsubik(0b001101, 1, G_SUB),
    rsubikc(0b001111, 1, G_SUB),

    // Multiplication
    mul(0b010000, 3, G_MUL),
    mulh(0b010000, 3, G_MUL),
    mulhu(0b010000, 3, G_MUL),
    mulhsu(0b010000, 3, G_MUL),
    muli(0b011000, 3, G_MUL),

    // Logical Operations
    and(0b100001, 1, G_LOGICAL),
    andi(0b101001, 1, G_LOGICAL),
    andn(0b100011, 1, G_LOGICAL),
    andni(0b101011, 1, G_LOGICAL),
    bsrl(0b010001, 1, G_LOGICAL),
    bsra(0b010001, 1, G_LOGICAL),
    bsll(0b010001, 1, G_LOGICAL),
    bsrli(0b011001, 1, G_LOGICAL),
    bsrai(0b011001, 1, G_LOGICAL),
    bslli(0b011001, 1, G_LOGICAL),
    or(0b100000, 1, G_LOGICAL),
    ori(0b101000, 1, G_LOGICAL),
    xor(0b100010, 1, G_LOGICAL),
    xori(0b101010, 1, G_LOGICAL),

    // Unary Logical Operations
    sra(0b100100, 1, G_UNARY, G_LOGICAL),
    src(0b100100, 1, G_UNARY, G_LOGICAL),
    srl(0b100100, 1, G_UNARY, G_LOGICAL),

    // Conditional Jumps
    beq(0b100111, 1, G_CJUMP, G_RJUMP),
    beqd(0b100111, 1, G_CJUMP, G_RJUMP),
    beqi(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    beqid(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bge(0b100111, 1, G_CJUMP, G_RJUMP),
    bged(0b100111, 1, G_CJUMP, G_RJUMP),
    bgei(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bgeid(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bgt(0b100111, 1, G_CJUMP, G_RJUMP),
    bgtd(0b100111, 1, G_CJUMP, G_RJUMP),
    bgti(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bgtid(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    ble(0b100111, 1, G_CJUMP, G_RJUMP),
    bled(0b100111, 1, G_CJUMP, G_RJUMP),
    blei(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bleid(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    blt(0b100111, 1, G_CJUMP, G_RJUMP),
    bltd(0b100111, 1, G_CJUMP, G_RJUMP),
    blti(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bltid(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bne(0b100111, 1, G_CJUMP, G_RJUMP),
    bned(0b100111, 1, G_CJUMP, G_RJUMP),
    bnei(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bneid(0b101111, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    // TODO check latencies

    // Unconditional Jumps
    br(0b100110, 1, G_UJUMP, G_RJUMP),
    bra(0b100110, 1, G_UJUMP, G_AJUMP),
    brd(0b100110, 1, G_UJUMP, G_RJUMP),
    brad(0b100110, 1, G_UJUMP, G_AJUMP),
    brld(0b100110, 1, G_UJUMP, G_RJUMP),
    brald(0b100110, 1, G_UJUMP, G_AJUMP),
    bri(0b101110, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    brai(0b101110, 1, G_UJUMP, G_AJUMP, G_IJUMP),
    brid(0b101110, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    braid(0b101110, 1, G_UJUMP, G_AJUMP, G_IJUMP),
    brlid(0b101110, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    bralid(0b101110, 1, G_UJUMP, G_AJUMP, G_IJUMP),
    brk(0b100110, 1, G_UJUMP, G_RJUMP),
    brki(0b101110, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    rtbd(0b101101, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    rtid(0b101101, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    rted(0b101101, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    rtsd(0b101101, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    // TODO check latencies

    // Floating Point
    fadd(0b010110, 6, G_FLOAT, G_ADD),
    frsub(0b010110, 6, G_FLOAT, G_SUB),
    fmul(0b010110, 6, G_FLOAT, G_MUL),
    fdiv(0b010110, 30, G_FLOAT),
    fcmp_un(0b010110, 3, G_FLOAT),
    fcmp_lt(0b010110, 3, G_FLOAT),
    fcmp_eq(0b010110, 3, G_FLOAT),
    fcmp_le(0b010110, 3, G_FLOAT),
    fcmp_gt(0b010110, 3, G_FLOAT),
    fcmp_ne(0b010110, 3, G_FLOAT),
    fcmp_ge(0b010110, 3, G_FLOAT),
    flt(0b010110, 6, G_FLOAT),
    fint(0b010110, 5, G_FLOAT),
    fsqrt(0b010110, 29, G_FLOAT),

    // Store
    sb(0b110100, 2, G_STORE),
    sbr(0b110100, 2, G_STORE),
    sbi(0b111100, 2, G_STORE),
    sh(0b110101, 2, G_STORE),
    shr(0b110101, 2, G_STORE),
    shi(0b111101, 2, G_STORE),
    sw(0b110110, 2, G_STORE),
    swr(0b110110, 2, G_STORE),
    swi(0b111110, 2, G_STORE),
    swx(0b110110, 2, G_STORE),

    // Load
    lbu(0b110000, 2, G_LOAD),
    lbur(0b110000, 2, G_LOAD),
    lbui(0b111000, 2, G_LOAD),
    lhu(0b110001, 2, G_LOAD),
    lhur(0b110001, 2, G_LOAD),
    lhui(0b111001, 2, G_LOAD),
    lw(0b110010, 2, G_LOAD),
    lwr(0b110010, 2, G_LOAD),
    lwi(0b111010, 2, G_LOAD),
    lwx(0b110010, 2, G_LOAD),

    // Others
    idiv(0b010010, 34, G_OTHER),
    idivu(0b010010, 34, G_OTHER),
    cmp(0b000101, 1, G_OTHER),
    cmpu(0b000101, 1, G_OTHER),
    pcmpbf(0b100000, 1, G_OTHER),
    pcmpeq(0b100010, 1, G_OTHER),
    pcmpne(0b100011, 1, G_OTHER),
    mbar(0b101110, 1, G_OTHER),
    mfs(0b100101, 1, G_OTHER),
    msrclr(0b100101, 1, G_OTHER),
    msrset(0b100101, 1, G_OTHER),
    mts(0b100101, 1, G_OTHER),
    imm(0b101100, 1, G_OTHER),
    clz(0b100100, 1, G_OTHER),
    get(0b011011, 1, G_OTHER),
    getd(0b010011, 1, G_OTHER),
    put(0b011011, 2, G_OTHER),
    putd(0b010011, 2, G_OTHER),
    sext16(0b100100, 1, G_OTHER),
    sext8(0b100100, 1, G_OTHER),
    swapb(0b100100, 1, G_OTHER),
    swaph(0b100100, 1, G_OTHER),
    wdc(0b100100, 1, G_OTHER),
    wdc_flush(0b100100, 1, G_OTHER),
    wdc_clear(0b100100, 1, G_OTHER),
    wdc_ext_flush(0b100100, 1, G_OTHER),
    wdc_ext_clear(0b100100, 1, G_OTHER),
    wic(0b100100, 1, G_OTHER);

    /*
     * Instruction property fields
     */
    // private final int opcodebitmask = 0b11111100000000000000000000000000;
    private final String instructionName;
    private final int opcode;
    private final int latency;
    private final List<InstructionType> genericType;

    // private final int nrInputs = 2; // TODO fix
    // private final int nrOutputs = 1; // TODO fix
    // private final boolean hasDelay = false;

    /*
     * Constructor
     */
    private MicroBlazeInstructionSetFields(int opcode, int latency, InstructionType... tp) {
        this.instructionName = name();
        this.opcode = opcode;
        this.latency = latency;
        this.genericType = Arrays.asList(tp);
    }

    /*
     * Private helper method too look up the list
     */
    private int getLatency() {
        return this.latency;
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
}
