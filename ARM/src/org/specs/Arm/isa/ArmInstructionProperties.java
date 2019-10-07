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
    adrp(0x9000_0000, 3, 1, DPI_PCREL, G_OTHER),

    // DPI_ADDSUBIMM
    add32(0x1100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    adds32(0x3100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    sub32(0x5100_0000, 1, 1, DPI_ADDSUBIMM, G_SUB),
    subs32(0x7100_0000, 1, 1, DPI_ADDSUBIMM, G_SUB),

    /*add64(0x9100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    adds64(0xB100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    sub64(0xD100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),
    subs64(0xF100_0000, 1, 1, DPI_ADDSUBIMM, G_ADD),*/

    // DPI_ADDSUBIMM_TAGS
    addg(0x9180_0000, 1, 1, DPI_ADDSUBIMM_TAGS, G_ADD),
    subg(0xD180_0000, 1, 1, DPI_ADDSUBIMM_TAGS, G_SUB),

    // LOGICAL
    and32(0x1200_0000, 1, 1, LOGICAL, G_LOGICAL),
    orr32(0x3200_0000, 1, 1, LOGICAL, G_LOGICAL),
    eor32(0x5200_0000, 1, 1, LOGICAL, G_LOGICAL),
    ands32(0x7200_0000, 1, 1, LOGICAL, G_LOGICAL),
    and64(0x9200_0000, 1, 1, LOGICAL, G_LOGICAL),
    orr64(0xB200_0000, 1, 1, LOGICAL, G_LOGICAL),
    eor64(0xD200_0000, 1, 1, LOGICAL, G_LOGICAL),
    ands64(0xF200_0000, 1, 1, LOGICAL, G_LOGICAL),

    // MOVEW
    movn32(0x1280_0000, 1, 1, MOVEW, G_OTHER);

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
