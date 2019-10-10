package org.specs.Arm.isa;

import static org.specs.Arm.isa.ArmInstructionType.*;
import static pt.up.fe.specs.binarytranslation.InstructionType.*;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.InstructionProperties;
import pt.up.fe.specs.binarytranslation.InstructionType;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;
import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;

public enum ArmInstructionProperties implements InstructionProperties {

    // name, binary code, latency, delay, subtype
    // within ISA (i.e. instruction binary format), generic type

    // Annotations of chaper and page numbers are relative to the ARMv8 reference manual:
    // https://static.docs.arm.com/ddi0487/ea/DDI0487E_a_armv8_arm.pdf

    // DPI_PCREL (C4-253)
    adr(0x1000_0000, 1, 1, DPI_PCREL, G_OTHER),
    adrp(0x9000_0000, 1, 1, DPI_PCREL, G_OTHER),

    // DPI_ADDSUBIMM (C4-253 to C4-254)
    add(0x1100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    adds(0x3100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    sub(0x5100_0000, 1, 1, DPI_ADDSUBIMM, G_SUB),
    subs(0x7100_0000, 1, 1, DPI_ADDSUBIMM, G_SUB),

    // DPI_ADDSUBIMM_TAGS (C4-254)
    addg(0x9180_0000, 1, 1, DPI_ADDSUBIMM_TAGS, G_ADD),
    subg(0xD180_0000, 1, 1, DPI_ADDSUBIMM_TAGS, G_SUB),

    // LOGICAL (C4-255)
    and(0x1200_0000, 1, 1, LOGICAL, G_LOGICAL),
    orr(0x3200_0000, 1, 1, LOGICAL, G_LOGICAL),
    eor(0x5200_0000, 1, 1, LOGICAL, G_LOGICAL),
    ands(0x7200_0000, 1, 1, LOGICAL, G_LOGICAL),

    // MOVEW (C4-255)
    movn(0x1280_0000, 1, 1, MOVEW, G_OTHER),
    movz(0x5280_0000, 1, 1, MOVEW, G_OTHER),
    movk(0x7280_0000, 1, 1, MOVEW, G_OTHER),

    // BITFIELD (C4-256)
    sbfm(0x1300_0000, 1, 1, BITFIELD, G_OTHER),
    bfm(0x3300_0000, 1, 1, BITFIELD, G_OTHER),
    ubfm(0x5300_0000, 1, 1, BITFIELD, G_OTHER),

    // EXTRACT (C4-256)
    extr(0x1380_0000, 1, 1, EXTRACT, G_OTHER),

    // CONDITIONALBRANCH (C4-257)
    bcond("b", 0x5400_0000, 1, 1, CONDITIONALBRANCH, G_JUMP, G_CJUMP, G_RJUMP),

    // EXCEPTION (C4-258)
    svc(0xD400_0001, 1, 1, EXCEPTION, G_OTHER),
    hvc(0xD400_0002, 1, 1, EXCEPTION, G_OTHER),
    smc(0xD400_0003, 1, 1, EXCEPTION, G_OTHER),
    brk(0xD420_0000, 1, 1, EXCEPTION, G_OTHER),
    hlt(0xD440_0000, 1, 1, EXCEPTION, G_OTHER),
    dcps1(0xD4A0_0001, 1, 1, EXCEPTION, G_OTHER),
    dcps2(0xD4A0_0002, 1, 1, EXCEPTION, G_OTHER),
    dcps3(0xD4A0_0003, 1, 1, EXCEPTION, G_OTHER),

    // UNCONDITIONAL BRANCH (REGISTER) (C4-262 to C4-264)
    br(0xD61F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    braaz(0xD61F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    braa(0xD71F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    brabz(0xD61F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    brab(0xD71F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blraaz(0xD63F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blraa(0xD73F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blrabz(0xD63F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blrab(0xD73F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blr(0xD63F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    ret(0xD65F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    retaa(0xD65F_0BFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    retab(0xD65F_0FFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    eret(0xD69F_03E0, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    eretaa(0xD69F_0EFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    eretab(0xD69F_0FFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),

    // UNCONDITIONAL BRANCH (IMMEDIATE) (C4-264 to C4-265)
    b(0x1400_0000, 1, 1, UCONDITIONALBRANCH_IMM, G_JUMP, G_UJUMP, G_IJUMP),
    bl(0x9400_0000, 1, 1, UCONDITIONALBRANCH_IMM, G_JUMP, G_UJUMP, G_IJUMP),

    // COMPARE AND BRANCH (C4-265)
    cbz(0x3400_0000, 1, 1, COMPARE_AND_BRANCH_IMM, G_CMP, G_JUMP, G_IJUMP),
    cbnz(0x3500_0000, 1, 1, COMPARE_AND_BRANCH_IMM, G_CMP, G_JUMP, G_IJUMP),

    // TEST_AND_BRANCH (C4-265)
    tbz(0x3600_0000, 1, 1, TEST_AND_BRANCH, G_CMP, G_JUMP, G_IJUMP),
    tbnz(0x3700_0000, 1, 1, TEST_AND_BRANCH, G_CMP, G_JUMP, G_IJUMP),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_REG_LITERAL (C4-280)
    ldr_reg("ldr", 0x1800_0000, 1, 1, LOAD_REG_LITERAL_FMT1, G_MEMORY, G_LOAD),
    ldrsw_reg("ldrsw", 0x9800_0000, 1, 1, LOAD_REG_LITERAL_FMT2, G_MEMORY, G_LOAD),
    prfm(0xD800_0000, 1, 1, LOAD_REG_LITERAL_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR (LOAD_STORE_NOALLOC) (C4-280 to C4-281)
    stnp(0x2800_0000, 1, 1, LOAD_STORE_PAIR_NO_ALLOC, G_MEMORY, G_STORE),
    ldnp(0x28C0_0000, 1, 1, LOAD_STORE_PAIR_NO_ALLOC, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_PAIR (LOAD_STORE_PAIR_POSTINDEX +
    // LOAD_STORE_PAIR_OFFSET + LOAD_STORE_PAIR_PREINDEX) (C4-281 to C4-282)
    stp(0x2800_0000, 1, 1, LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1, G_MEMORY, G_STORE),
    ldp(0x28C0_0000, 1, 1, LOAD_STORE_PAIR_REG_PREOFFPOST_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_ADDITIONAL (C4-282)
    stgp(0x6800_0000, 1, 1, LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2, G_MEMORY, G_STORE),
    ldpsw(0x6840_0000, 1, 1, LOAD_STORE_PAIR_REG_PREOFFPOST_FMT2, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_PAIR_IMM_UNSCALED_FMT1 (C4-283 to C4-284)
    sturb(0x3800_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_STORE),
    ldurb(0x3840_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    sturh(0x7800_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_STORE),
    ldurh(0x7840_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    ldursw(0xB880_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    prfum(0xF880_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_UNSCALED_FMT2 (C4-283 to C4-284)
    ldursb(0x3800_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldursh(0x7800_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_UNSCALED_FMT3 (C4-283 to C4-284)
    stur(0x3800_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT3, G_MEMORY, G_STORE),
    ldur(0x3840_0000, 1, 1, LOAD_STORE_PAIR_IMM_FMT3, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_PAIR_UNPRIV_FMT1 (C4-286)
    sttrb(0x3800_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_STORE),
    ldtrb(0x3840_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    sttrh(0x7800_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_STORE),
    ldtrh(0x7840_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),
    ldtrsw(0xB880_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_UNPRIV_FMT2 (C4-286)
    ldtrsb(0x38C0_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),
    ldtrsh(0x78C0_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_UNPRIV_FMT3 (C4-286)
    sttr(0xB800_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT3, G_MEMORY, G_STORE),
    ldtr(0xB840_0800, 1, 1, LOAD_STORE_PAIR_IMM_FMT3, G_MEMORY, G_LOAD),

    ///////////////////////////////////////////////////////////////////////////

    // LOAD_STORE_PAIR_IMM_PREPOST_FMT1 (C4-284 to C4-285 and C4-286 to C4-287)
    strb(0x3800_0000, 1, 1, LOAD_STORE_PAIR_IMM_PREPOST_FMT1, G_MEMORY, G_STORE),
    ldrb(0x3840_0000, 1, 1, LOAD_STORE_PAIR_IMM_PREPOST_FMT1, G_MEMORY, G_LOAD),
    strh(0x7800_0000, 1, 1, LOAD_STORE_PAIR_IMM_PREPOST_FMT1, G_MEMORY, G_STORE),
    ldrh(0x7840_0000, 1, 1, LOAD_STORE_PAIR_IMM_PREPOST_FMT1, G_MEMORY, G_LOAD),
    ldrsw(0xB880_0000, 1, 1, LOAD_STORE_PAIR_IMM_PREPOST_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_PREPOST_FMT2 (C4-284 to C4-285 and C4-286 to C4-287)
    ldrsb(0x3800_0000, 1, 1, LOAD_STORE_PAIR_IMM_PREPOST_FMT2, G_MEMORY, G_LOAD),
    ldrsh(0x7800_0000, 1, 1, LOAD_STORE_PAIR_IMM_PREPOST_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_PREPOST_FMT3 (C4-284 to C4-285 and C4-286 to C4-287)
    str_imm("str", 0x3800_0000, 1, 1, LOAD_STORE_PAIR_IMM_PREPOST_FMT3, G_MEMORY, G_STORE),
    ldr_imm("ldr", 0x3840_0000, 1, 1, LOAD_STORE_PAIR_IMM_PREPOST_FMT3, G_MEMORY, G_LOAD),

    unknown(0x000000000, 1, 1, UNDEFINED, G_UNKN);

    ///////////////////////////////////////////////////////////////////////////

    /*
     * Instruction property fields
     */
    private String instructionName;
    private final int opcode; // 32 bit instruction code without operands
    private final int reducedopcode; // only the bits that matter, built after parsing the fields
    private final int latency;
    private final int delay;
    private final ArmInstructionType codetype;
    private final List<InstructionType> genericType;
    private final AsmInstructionData iData; // decoded fields of this instruction

    /*
     *  Helper constructor
     */
    private ArmInstructionProperties(int opcode, int latency,
            int delay, ArmInstructionType mbtype, List<InstructionType> tp) {
        this.instructionName = "";
        this.opcode = opcode;
        this.latency = latency;
        this.delay = delay;
        this.codetype = mbtype;
        this.genericType = tp;

        // use the parser to initialize private fields of instruction set itself
        IsaParser parser = new ArmIsaParser();
        this.iData = parser.parse(Integer.toHexString(opcode)); // TODO make new overload for "parse"
        this.reducedopcode = this.iData.getReducedOpcode();
    }

    /*
     * Constructor
     */
    private ArmInstructionProperties(int opcode, int latency,
            int delay, ArmInstructionType mbtype, InstructionType... tp) {
        this(opcode, latency, delay, mbtype, Arrays.asList(tp));
        this.instructionName = name();
        // System.out.print(this.codetype.toString() + ": " + this.instructionName + "\n");
    }

    /*
     * Helper constructor with explicit name for instruction
     */
    private ArmInstructionProperties(String explicitname, int opcode, int latency,
            int delay, ArmInstructionType mbtype, InstructionType... tp) {
        this(opcode, latency, delay, mbtype, Arrays.asList(tp));
        this.instructionName = explicitname;
        // System.out.print(this.codetype.toString() + ": " + this.instructionName + "\n");
    }

    /*
     * Private helper method too look up the list
     */
    public int getLatency() {
        return this.latency;
    }

    /*
     * Private helper method too look up the list
     */
    public int getDelay() {
        return this.delay;
    }

    /*
     * Private helper method too get full opcode
     */
    public int getOpCode() {
        return this.opcode;
    }

    /*
     * Private helper method too get only the bits that matter
     */
    public int getReducedOpCode() {
        return this.reducedopcode;
    }

    /*
     * Private helper method too look up type in the list
     */
    public List<InstructionType> getGenericType() {
        return this.genericType;
    }

    /*
     * Private helper method too look up name the list
     */
    public String getName() {
        return this.instructionName;
    }

    /*
     * get code type of a particular instruction
     */
    public AsmInstructionType getCodeType() {
        return this.codetype;
    }
}
