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

    // DPI_PCREL
    adr(0x1000_0000, 1, 1, DPI_PCREL, G_OTHER),
    adrp(0x9000_0000, 1, 1, DPI_PCREL, G_OTHER),

    // DPI_ADDSUBIMM
    add(0x1100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    adds(0x3100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    sub(0x5100_0000, 1, 1, DPI_ADDSUBIMM, G_SUB),
    subs(0x7100_0000, 1, 1, DPI_ADDSUBIMM, G_SUB),

    // DPI_ADDSUBIMM_TAGS
    addg(0x9180_0000, 1, 1, DPI_ADDSUBIMM_TAGS, G_ADD),
    subg(0xD180_0000, 1, 1, DPI_ADDSUBIMM_TAGS, G_SUB),

    // LOGICAL
    and(0x1200_0000, 1, 1, LOGICAL, G_LOGICAL),
    orr(0x3200_0000, 1, 1, LOGICAL, G_LOGICAL),
    eor(0x5200_0000, 1, 1, LOGICAL, G_LOGICAL),
    ands(0x7200_0000, 1, 1, LOGICAL, G_LOGICAL),

    // MOVEW
    movn(0x1280_0000, 1, 1, MOVEW, G_OTHER),
    movz(0x5280_0000, 1, 1, MOVEW, G_OTHER),
    movk(0x7280_0000, 1, 1, MOVEW, G_OTHER),

    // BITFIELD
    sfbm(0x1300_0000, 1, 1, BITFIELD, G_OTHER),
    bfm(0x5300_0000, 1, 1, BITFIELD, G_OTHER),
    ubfm(0x7300_0000, 1, 1, BITFIELD, G_OTHER),

    // EXTRACT
    extr(0x1380_0000, 1, 1, BITFIELD, G_OTHER),

    // CONDITIONALBRANCH
    bcond(0x5400_0000, 1, 1, CONDITIONALBRANCH, G_JUMP, G_CJUMP, G_RJUMP),

    // EXCEPTION
    svc(0xD400_0001, 1, 1, EXCEPTION, G_OTHER),
    hvc(0xD400_0002, 1, 1, EXCEPTION, G_OTHER),
    smc(0xD400_0003, 1, 1, EXCEPTION, G_OTHER),
    brk(0xD420_0000, 1, 1, EXCEPTION, G_OTHER),
    hlt(0xD440_0000, 1, 1, EXCEPTION, G_OTHER),
    dcps1(0xD4A0_0001, 1, 1, EXCEPTION, G_OTHER),
    dcps2(0xD4A0_0002, 1, 1, EXCEPTION, G_OTHER),
    dcps3(0xD4A0_0003, 1, 1, EXCEPTION, G_OTHER),

    // UNCONDITIONAL BRANCH (REGISTER)
    br(0xD61F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),

    braaz(0xD61F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    braa(0xD71F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    brabz(0xD61F_0400, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    brab(0xD71F_0400, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),

    blraaz(0xD63F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blraa(0xD73F_0800, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blrabz(0xD63F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    blrab(0xD73F_0C00, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),

    blr(0xD63F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),

    ret(0xD65F_0000, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    retaa(0xD65F_0BFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    retbb(0xD65F_0FFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),

    eret(0xD69F_03E0, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    eretaa(0xD69F_0EFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),
    eretab(0xD69F_0FFF, 1, 1, UCONDITIONALBRANCH_REG, G_JUMP, G_UJUMP),

    // UNCONDITIONAL BRANCH (IMMEDIATE)
    b(0x1400_0000, 1, 1, UCONDITIONALBRANCH_IMM, G_JUMP, G_UJUMP, G_IJUMP),
    bl(0x9400_0000, 1, 1, UCONDITIONALBRANCH_IMM, G_JUMP, G_UJUMP, G_IJUMP),

    // COMPARE AND BRANCH
    cbz(0x3400_0000, 1, 1, COMPARE_AND_BRANCH_IMM, G_CMP, G_JUMP, G_IJUMP),
    cbnz(0x3500_0000, 1, 1, COMPARE_AND_BRANCH_IMM, G_CMP, G_JUMP, G_IJUMP),

    // TEST_AND_BRANCH
    tbz(0x3600_0000, 1, 1, TEST_AND_BRANCH, G_CMP, G_JUMP, G_IJUMP),
    tbnz(0x3700_0000, 1, 1, TEST_AND_BRANCH, G_CMP, G_JUMP, G_IJUMP),

    // LOAD_REG_LITERAL
    ldr(0x1800_0000, 1, 1, LOAD_REG_LITERAL, G_MEMORY, G_LOAD),
    ldr_simd(0x1C00_0000, 1, 1, LOAD_REG_LITERAL, G_MEMORY, G_LOAD),
    // ldrsw(0x9800_0000, 1, 1, LOAD_REG_LITERAL, G_MEMORY, G_LOAD), // TODO how to handle??
    prfm(0x9800_0000, 1, 1, LOAD_REG_LITERAL, G_MEMORY, G_LOAD),
    // TODO check if "sf" field processing is best for these insts (and variants)

    // LOAD_STORE_PAIR (LOAD_STORE_NOALLOC)
    stnp(0x2800_0000, 1, 1, LOAD_STORE_PAIR, G_MEMORY, G_STORE),
    ldnp(0x2840_0000, 1, 1, LOAD_STORE_PAIR, G_MEMORY, G_LOAD),
    stnp_simd(0x2C00_0000, 1, 1, LOAD_STORE_PAIR, G_MEMORY, G_STORE),
    ldnp_simd(0x2C40_0000, 1, 1, LOAD_STORE_PAIR, G_MEMORY, G_LOAD),
    // TODO check if "sf" field processing is best for these insts (and variants)

    // LOAD_STORE_PAIR (LOAD_STORE_PAIR_POSTINDEX + LOAD_STORE_PAIR_OFFSET + LOAD_STORE_PAIR_PREINDEX)
    stp(0x2800_0000, 1, 1, LOAD_STORE_PAIR, G_MEMORY, G_STORE),
    ldp(0x2840_0000, 1, 1, LOAD_STORE_PAIR, G_MEMORY, G_LOAD),
    stp_simd(0x2C00_0000, 1, 1, LOAD_STORE_PAIR, G_MEMORY, G_STORE),
    ldp_simd(0x2C40_0000, 1, 1, LOAD_STORE_PAIR, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_ADDITIONAL
    stgp(0x6800_0000, 1, 1, LOAD_STORE_PAIR_ADDITIONAL, G_MEMORY, G_STORE),
    ldpsw(0x6840_0000, 1, 1, LOAD_STORE_PAIR_ADDITIONAL, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_UNSCALED_FMT1
    sturb(0x3800_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT1, G_MEMORY, G_STORE),
    ldurb(0x3840_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT1, G_MEMORY, G_LOAD),
    sturh(0x7800_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT1, G_MEMORY, G_STORE),
    ldurh(0x7840_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT1, G_MEMORY, G_LOAD),
    ldursw(0xB880_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT1, G_MEMORY, G_LOAD),
    prfum(0xF880_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT1, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_UNSCALED_FMT2
    ldursb(0x3800_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT2, G_MEMORY, G_LOAD),
    ldursh(0x7800_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT2, G_MEMORY, G_LOAD),

    // LOAD_STORE_PAIR_IMM_UNSCALED_FMT3
    stur(0x0000_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT1, G_MEMORY, G_STORE),
    ldur(0x0000_0000, 1, 1, LOAD_STORE_PAIR_IMM_UNSCALED_FMT1, G_MEMORY, G_LOAD),

    /*
     * Instruction property fields
     */
    private final String instructionName;
    private final int opcode; // 32 bit instruction code without operands
    private final int reducedopcode; // only the bits that matter, built after parsing the fields
    private final int latency;
    private final int delay;
    private final ArmInstructionType codetype;
    private final List<InstructionType> genericType;
    private final AsmInstructionData iData; // decoded fields of this instruction

    /*
     * Constructor
     */
    private ArmInstructionProperties(int opcode, int latency,
            int delay, ArmInstructionType mbtype, InstructionType... tp) {
        this.instructionName = name();
        this.opcode = opcode;
        this.latency = latency;
        this.delay = delay;
        this.codetype = mbtype;
        this.genericType = Arrays.asList(tp);

        // use the parser to initialize private fields of instruction set itself
        IsaParser parser = ArmInstructionParsers.getArmIsaParser();
        this.iData = parser.parse(Integer.toHexString(opcode)); // TODO make new overload for "parse"
        this.reducedopcode = this.iData.getReducedOpcode();
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
