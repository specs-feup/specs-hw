package org.specs.MicroBlaze.v2;

import static pt.up.fe.specs.binarytranslation.InstructionType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.InstructionType;

public enum MicroBlazeInstructionSetFields {

    // TODO add fields: nr inputs, nr outputs, has delay slot (bool)

    // Addition/Subtraction Operations
    add(0x0000_0000, 1, 0, G_ADD),
    addc(0x0800_0000, 1, 0, G_ADD),
    addk(0x1000_0000, 1, 0, G_ADD),
    addkc(0x1800_0000, 1, 0, G_ADD),
    addi(0x2000_0000, 1, 0, G_ADD),
    addic(0x2800_0000, 1, 0, G_ADD),
    addik(0x3000_0000, 1, 0, G_ADD),
    addikc(0x3800_0000, 1, 0, G_ADD),
    rsub(0x0400_0000, 1, 0, G_SUB),
    rsubc(0xC000_0000, 1, 0, G_SUB),
    rsubk(0x1400_0000, 1, 0, G_SUB),
    rsubkc(0x1C00_0000, 1, 0, G_SUB),
    rsubi(0x2400_0000, 1, 0, G_SUB),
    rsubic(0x2C00_0000, 1, 0, G_SUB),
    rsubik(0x3400_0000, 1, 0, G_SUB),
    rsubikc(0x3C00_0000, 1, 0, G_SUB),

    // Multiplication
    mul(0x4000_0000, 3, 0, G_MUL),
    mulh(0x4000_0001, 3, 0, G_MUL),
    mulhu(0x4000_0003, 3, 0, G_MUL),
    mulhsu(0x4000_0002, 3, 0, G_MUL),
    muli(0x6000_0000, 3, 0, G_MUL),

    // Logical Operations
    and(0x8400_0000, 1, 0, G_LOGICAL),
    andi(0xA400_0000, 1, 0, G_LOGICAL),
    andn(0x8C00_0000, 1, 0, G_LOGICAL),
    andni(0xAC00_0000, 1, 0, G_LOGICAL),
    bsrl(0x4400_0000, 1, 0, G_LOGICAL), // exception to typeb rule
    bsra(0x4400_0800, 1, 0, G_LOGICAL), // exception to typeb rule
    bsll(0x4400_0400, 1, 0, G_LOGICAL), // exception to typeb rule
    bsrli(0x6400_0000, 1, 0, G_LOGICAL), // exception to typeb rule
    bsrai(0x6400_0800, 1, 0, G_LOGICAL), // exception to typeb rule
    bslli(0x6400_0400, 1, 0, G_LOGICAL), // exception to typeb rule
    bsefi(0x6400_4000, 1, 0, G_LOGICAL), // exception to typeb rule
    bsifi(0x6400_8000, 1, 0, G_LOGICAL), // exception to typeb rule
    or(0x8000_0000, 1, 0, G_LOGICAL),
    ori(0xA000_0000, 1, 0, G_LOGICAL),
    xor(0x8800_0000, 1, 0, G_LOGICAL),
    xori(0xA800_0000, 1, 0, G_LOGICAL),

    // Unary Logical Operations
    sra(0x9000_0001, 1, 0, G_UNARY, G_LOGICAL),
    src(0x9000_0021, 1, 0, G_UNARY, G_LOGICAL),
    srl(0x9000_0041, 1, 0, G_UNARY, G_LOGICAL),

    // Conditional Jumps
    beq(0x9C00_0000, 1, 0, G_CJUMP, G_RJUMP),
    beqd(0x9E00_0000, 1, 0, G_CJUMP, G_RJUMP),
    beqi(0xBC00_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    beqid(0xBE00_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bge(0x9CA0_0000, 1, 0, G_CJUMP, G_RJUMP),
    bged(0x9EA0_0000, 1, 1, G_CJUMP, G_RJUMP),
    bgei(0xBCA0_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bgeid(0xBEA0_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bgt(0x9C80_0000, 1, 0, G_CJUMP, G_RJUMP),
    bgtd(0x9E80_0000, 1, 1, G_CJUMP, G_RJUMP),
    bgti(0xBC80_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bgtid(0xBE80_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    ble(0x9C60_0000, 1, 0, G_CJUMP, G_RJUMP),
    bled(0x9E60_0000, 1, 1, G_CJUMP, G_RJUMP),
    blei(0xBC60_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bleid(0xBE60_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    blt(0x9C40_0000, 1, 0, G_CJUMP, G_RJUMP),
    bltd(0x9E40_0000, 1, 1, G_CJUMP, G_RJUMP),
    blti(0xBC40_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bltid(0xBE40_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    bne(0x9C20_0000, 1, 0, G_CJUMP, G_RJUMP),
    bned(0x9E20_0000, 1, 0, G_CJUMP, G_RJUMP),
    bnei(0xBC20_0000, 1, 0, G_CJUMP, G_RJUMP, G_IJUMP),
    bneid(0xBE20_0000, 1, 1, G_CJUMP, G_RJUMP, G_IJUMP),
    // all branches are exception to typeb rule

    // Unconditional Jumps
    br(0x9800_0000, 1, 0, G_UJUMP, G_RJUMP),
    bra(0x9810_0000, 1, 0, G_UJUMP, G_AJUMP),
    brd(0x9808_0000, 1, 1, G_UJUMP, G_RJUMP),
    brad(0x9818_0000, 1, 1, G_UJUMP, G_AJUMP),
    brld(0x9814_0000, 1, 1, G_UJUMP, G_RJUMP),
    brald(0x981C_0000, 1, 1, G_UJUMP, G_AJUMP),
    bri(0xB800_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP),
    brai(0xB808_0000, 1, 0, G_UJUMP, G_AJUMP, G_IJUMP),
    brid(0xB810_0000, 1, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    braid(0xB818_0000, 1, 1, G_UJUMP, G_AJUMP, G_IJUMP),
    brlid(0xB814_0000, 1, 1, G_UJUMP, G_RJUMP, G_IJUMP),
    bralid(0xB81C_0000, 1, 1, G_UJUMP, G_AJUMP, G_IJUMP),
    brk(0x980C_0000, 1, 0, G_UJUMP, G_RJUMP),
    brki(0xB80C_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP),
    // all branches are exception to typeb rule

    // Returns
    rtbd(0xB640_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP), // exception to typeb rule
    rtid(0xB620_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP), // exception to typeb rule
    rted(0xB680_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP), // exception to typeb rule
    rtsd(0xB600_0000, 1, 0, G_UJUMP, G_RJUMP, G_IJUMP), // exception to typeb rule

    // Floating Point
    fadd(0x5800_0000, 6, 0, G_FLOAT, G_ADD),
    frsub(0x5800_0080, 6, 0, G_FLOAT, G_SUB),
    fmul(0x5800_0100, 6, 0, G_FLOAT, G_MUL),
    fdiv(0x5800_0180, 30, 0, G_FLOAT),
    fcmp_un(0x5800_0200, 3, 0, G_FLOAT),
    fcmp_lt(0x5800_0210, 3, 0, G_FLOAT),
    fcmp_eq(0x5800_0220, 3, 0, G_FLOAT),
    fcmp_le(0x5800_0230, 3, 0, G_FLOAT),
    fcmp_gt(0x5800_0240, 3, 0, G_FLOAT),
    fcmp_ne(0x5800_0250, 3, 0, G_FLOAT),
    fcmp_ge(0x5800_0260, 3, 0, G_FLOAT),
    flt(0x5800_0280, 6, 0, G_FLOAT),
    fint(0x5800_0300, 5, 0, G_FLOAT),
    fsqrt(0x5800_0380, 29, 0, G_FLOAT),

    // Store
    sb(0xD000_0000, 2, 0, G_STORE),
    sbr(0xD000_0200, 2, 0, G_STORE),
    sbea(0xD000_0080, 2, 0, G_STORE),
    sbi(0xF000_0000, 2, 0, G_STORE),
    sh(0xD400_0000, 2, 0, G_STORE),
    shr(0xD400_0200, 2, 0, G_STORE),
    shea(0xD400_0080, 2, 0, G_STORE),
    shi(0xF400_0000, 2, 0, G_STORE),
    sw(0xD800_0000, 2, 0, G_STORE),
    swr(0xD800_0200, 2, 0, G_STORE),
    swea(0xD800_0080, 2, 0, G_STORE),
    swi(0xF800_0000, 2, 0, G_STORE),
    swx(0xD800_0400, 2, 0, G_STORE),

    // Load
    lbu(0xC000_0000, 2, 0, G_LOAD),
    lbur(0xC000_0200, 2, 0, G_LOAD),
    lbuea(0xC000_0080, 2, 0, G_LOAD),
    lbui(0xE000_0000, 2, 0, G_LOAD),
    lhu(0xC400_0000, 2, 0, G_LOAD),
    lhur(0xC400_0200, 2, 0, G_LOAD),
    lhuea(0xC400_0080, 2, 0, G_LOAD),
    lhui(0xE400_0000, 2, 0, G_LOAD),
    lw(0xC800_0000, 2, 0, G_LOAD),
    lwr(0xC800_0200, 2, 0, G_LOAD),
    lwea(0xC800_0080, 2, 0, G_LOAD),
    lwi(0xE800_0000, 2, 0, G_LOAD),
    lwx(0xC800_0400, 2, 0, G_LOAD),

    // Others
    idiv(0x4800_0000, 34, 0, G_OTHER),
    idivu(0x4800_0002, 34, 0, G_OTHER),
    cmp(0x1400_0001, 1, 0, G_OTHER),
    cmpu(0x1400_0003, 1, 0, G_OTHER),
    pcmpbf(0x8000_0400, 1, 0, G_OTHER),
    pcmpeq(0x8800_0400, 1, 0, G_OTHER),
    pcmpne(0x8C00_0400, 1, 0, G_OTHER),
    mbar(0xB802_0004, 1, 0, G_OTHER),
    mfs(0x9400_8000, 1, 0, G_OTHER),
    mfse(0x9408_8000, 1, 0, G_OTHER),
    msrclr(0x9411_0000, 1, 0, G_OTHER),
    msrset(0x9410_0000, 1, 0, G_OTHER),
    mts(0x9400_C000, 1, 0, G_OTHER),
    mtse(0x9500_C000, 1, 0, G_OTHER),
    imm(0xB000_0000, 1, 0, G_OTHER),
    clz(0x9000_00E0, 1, 0, G_OTHER),

    get(0x6C00_0000, 1, 0, G_OTHER),
    getd(0x4C00_0000, 1, 0, G_OTHER),
    put(0x6C00_8000, 2, 0, G_OTHER),
    putd(0x4C00_8000, 2, 0, G_OTHER),
    // TODO some gets are missing

    sext16(0x9000_0061, 1, 0, G_OTHER),
    sext8(0x9000_0060, 1, 0, G_OTHER),
    swapb(0x9000_01E0, 1, 0, G_OTHER),
    swaph(0x9000_01E2, 1, 0, G_OTHER),
    wdc(0x9000_0000, 1, 0, G_OTHER),
    wdc_flush(0x9000_0074, 1, 0, G_OTHER),
    wdc_clear(0x9000_0066, 1, 0, G_OTHER),
    wdc_clear_ea(0x9000_00E6, 1, 0, G_OTHER),
    wdc_ext_flush(0x9000_0474, 1, 0, G_OTHER),
    wdc_ext_clear(0x9000_0466, 1, 0, G_OTHER),
    wic(0x9000_0068, 1, 0, G_OTHER);

    // private final int opcodebitmask = 0b11111100000000000000000000000000;
    // private final int nrInputs = 2; // TODO fix
    // private final int nrOutputs = 1; // TODO fix
    // private final boolean hasDelay = false;

    /*
     * Instruction property fields
     */
    private final String instructionName;
    private final int opcode;
    private final int latency;
    private final int delay;
    private final List<InstructionType> genericType;
    private static int reducerMask = 0xFC00_0000; // gets only the first 6 bits

    // Exceptions to TypeA/B rule
    private static int specialInst = 0x9400_0000;

    private static int barrelShift = 0x4400_0000; // barrelshift
    private static int ibarrelShift = 0x6400_0000; // barrelshift with immediate value

    private static int unconditionalBranch = 0x9800_0000;
    private static int iunconditionalBranch = 0xB800_0000;

    private static int conditionalBranch = 0x9C00_0000;
    private static int iconditionalBranch = 0xBC00_0000;
    private static int returnInstructions = 0xB400_0000;

    private static int streamInsts = 0x6C00_0000;
    private static int dstreamInsts = 0x4C00_0000;

    private static int typeAB = 0x20000000; // if this bit is 1, then its typeB instruction
    private static int maskA = makeMaskA();
    private static int maskB = makeMaskB();
    private static int ubranchMask = makeMaskUBranch();
    private static int cbranchMask = makeMaskCBranch();
    private static int specialMask = makeMaskSpecial();
    private static int barrelMask = makeMaskBarrel();
    private static int ibarrelMask = makeMaskiBarrel();
    private static int streamMask = makeMaskStream();
    private static int dstreamMask = makeMaskdStream();

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
     * Private method to create bit mask for typeA 
     * instructions in this ISA, based on the instruction list
     */
    private static int makeMaskA() {
        int mask = 0;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (isTypeA(insts.getOpCode()))
                mask |= insts.getOpCode();
        }
        return mask;
    }

    /*
     * Private method to create bit mask for typeB 
     * instructions in this ISA, based on the instruction list
     */
    private static int makeMaskB() {
        int mask = 0;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (isTypeB(insts.getOpCode()))
                mask |= insts.getOpCode();
        }
        return mask;
    }

    /*
     * Private method to create bit mask for unconditional branches 
     * instructions in this ISA, based on the instruction list
     */
    private static int makeMaskUBranch() {
        int mask = 0;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (isUBranch(insts.getOpCode()))
                mask |= insts.getOpCode();
        }

        // hack to remove MBAR
        mask &= 0xFFFF_FFFB;
        return mask;
    }

    /*
     * Private method to create bit mask for conditional branches 
     * instructions in this ISA, based on the instruction list
     */
    private static int makeMaskCBranch() {
        int mask = 0;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (isCBranch(insts.getOpCode()) || isReturn(insts.getOpCode()))
                mask |= insts.getOpCode();
        }
        return mask;
    }

    /*
     * Private method to create bit mask for branches 
     * instructions in this ISA, based on the instruction list
     */
    private static int makeMaskSpecial() {
        int mask = 0;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (isSpecial(insts.getOpCode()))
                mask |= insts.getOpCode();
        }
        return mask;
    }

    /*
     * Private method to create bit mask for barrel shifts
     */
    private static int makeMaskBarrel() {
        int mask = 0;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (isBarrel(insts.getOpCode()))
                mask |= insts.getOpCode();
        }
        return mask;
    }

    /*
     * Private method to create bit mask 
     * for barrel shifts with immediate values
     */
    private static int makeMaskiBarrel() {
        int mask = 0;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (isiBarrel(insts.getOpCode()))
                mask |= insts.getOpCode();
        }
        return mask;
    }

    /*
     * Private method to create bit mask put/get
     */
    private static int makeMaskStream() {
        int mask = 0;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (isStream(insts.getOpCode()))
                mask |= insts.getOpCode();
        }
        return mask;
    }

    /*
     * Private method to create bit mask putd/getd
     */
    private static int makeMaskdStream() {
        int mask = 0;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (isdStream(insts.getOpCode()))
                mask |= insts.getOpCode();
        }
        return mask;
    }

    /*
     * Check if instruction is typeA (or any relative branch)
     */
    private static boolean isTypeA(int opcode) {
        opcode &= reducerMask;
        return (!isSpecial(opcode)
                && !isBranch(opcode)
                && !isBarrel(opcode)
                && !isiBarrel(opcode)
                && !isReturn(opcode)
                && !isStream(opcode)
                && !isdStream(opcode)
                && ((opcode & typeAB) == 0));
    }

    /*
     * Check if instruction is typeB (or any relative branch)
     */
    private static boolean isTypeB(int opcode) {
        opcode &= reducerMask;
        return (!isSpecial(opcode)
                && !isBranch(opcode)
                && !isBarrel(opcode)
                && !isiBarrel(opcode)
                && !isReturn(opcode)
                && !isStream(opcode)
                && !isdStream(opcode)
                && ((opcode & typeAB) != 0));
    }

    /*
     * Check if instruction is Special
     */
    private static boolean isSpecial(int opcode) {
        opcode &= reducerMask;
        return (opcode == specialInst);
    }

    /*
     * Check if instruction is Branch
     */
    private static boolean isBranch(int opcode) {
        return (isCBranch(opcode) || isUBranch(opcode));
    }

    /*
     * Check if instruction is Conditional Branch
     */
    private static boolean isCBranch(int opcode) {
        opcode &= reducerMask;
        return ((opcode == conditionalBranch)
                || (opcode == iconditionalBranch));
    }

    /*
     * Check if instruction is Unconditional Branch
     */
    private static boolean isUBranch(int opcode) {
        opcode &= reducerMask;
        return ((opcode == unconditionalBranch)
                || (opcode == iunconditionalBranch));
    }

    /*
     * Check if instruction is return instruction
     */
    private static boolean isReturn(int opcode) {
        opcode &= reducerMask;
        return (opcode == returnInstructions);
    }

    /*
     * Check if instruction is Barrel Shift
     */
    private static boolean isBarrel(int opcode) {
        opcode &= reducerMask;
        return (opcode == barrelShift);
    }

    /*
     * Check if instruction is Barrel Shift w/ immediate value
     */
    private static boolean isiBarrel(int opcode) {
        opcode &= reducerMask;
        return (opcode == ibarrelShift);
    }

    /*
     * Check if instruction is put/get
     */
    private static boolean isStream(int opcode) {
        opcode &= reducerMask;
        return (opcode == streamInsts);
    }

    /*
     * Check if instruction is putd/getd
     */
    private static boolean isdStream(int opcode) {
        opcode &= reducerMask;
        return (opcode == dstreamInsts);
    }

    /*
     * Pick a mask
     */
    private static int pickMask(int fullopcode) {

        if (isSpecial(fullopcode))
            return specialMask;

        else if (isCBranch(fullopcode))
            return cbranchMask;

        else if (isUBranch(fullopcode))
            return ubranchMask;

        else if (isBarrel(fullopcode))
            return barrelMask;

        else if (isiBarrel(fullopcode))
            return ibarrelMask;

        else if (isStream(fullopcode))
            return streamMask;

        else if (isdStream(fullopcode))
            return dstreamMask;

        else if (isTypeA(fullopcode))
            return maskA;

        else if (isTypeB(fullopcode))
            return maskB;

        return 0;
        // TODO throw something here
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
    public static String getName(int fullopcode) {
        int mask = pickMask((int) fullopcode);
        int opcode = (fullopcode) & mask;
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
    public static List<InstructionType> getGenericType(int fullopcode) {
        int mask = pickMask((int) fullopcode);
        int opcode = (fullopcode) & mask;
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
    public static int getLatency(int fullopcode) {
        int mask = pickMask((int) fullopcode);
        int opcode = (fullopcode) & mask;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getLatency();
        }
        return -1; // TODO replace with exception
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    public static int getDelay(int fullopcode) {
        int mask = pickMask((int) fullopcode);
        int opcode = (fullopcode) & mask;
        for (MicroBlazeInstructionSetFields insts : values()) {
            if (insts.getOpCode() == opcode)
                return insts.getDelay();
        }
        return -1; // TODO replace with exception
    }
}
