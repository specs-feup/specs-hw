package org.specs.Arm.isa;

import static org.specs.Arm.isa.ArmInstructionType.DATA_PROCESSING_REGISTER;
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

    // DATA_PROCESSING_REGISTER
    and(0x0000_0000, 1, 1, DATA_PROCESSING_REGISTER, G_LOGICAL),
    xor(0x0020_0000, 3, 1, DATA_PROCESSING_REGISTER, G_LOGICAL),
    sub(0x0040_0000, 1, 1, DATA_PROCESSING_REGISTER, G_SUB),
    rsub(0x0060_0000, 1, 1, DATA_PROCESSING_REGISTER, G_SUB),
    add(0x0080_0000, 1, 1, DATA_PROCESSING_REGISTER, G_ADD),

    addi(0x0080_0000, 1, 1, DATA_PROCESSING_REGISTER, G_ADD);

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
