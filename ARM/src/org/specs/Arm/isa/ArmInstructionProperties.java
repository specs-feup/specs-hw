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

    // UNCONDITIONAL BRANCH
    br(0xD61F_0000, 1, 1, UCONDITIONALBRANCH, G_JUMP, G_UJUMP),
    braaz(0xD61F_0000, 1, 1, UCONDITIONALBRANCH, G_JUMP, G_UJUMP),
    braa(0xD71F_0000, 1, 1, UCONDITIONALBRANCH, G_JUMP, G_UJUMP),
    brabz(0xD61F_0400, 1, 1, UCONDITIONALBRANCH, G_JUMP, G_UJUMP),
    brab(0xD71F_0400, 1, 1, UCONDITIONALBRANCH, G_JUMP, G_UJUMP),
    blraaz(0xD63F_0000, 1, 1, UCONDITIONALBRANCH, G_JUMP, G_UJUMP),
    blraa(0xD73F_0000, 1, 1, UCONDITIONALBRANCH, G_JUMP, G_UJUMP),
    blrabz(0xD63F_0400, 1, 1, UCONDITIONALBRANCH, G_JUMP, G_UJUMP),
    blrab(0xD73F_0400, 1, 1, UCONDITIONALBRANCH, G_JUMP, G_UJUMP);

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
