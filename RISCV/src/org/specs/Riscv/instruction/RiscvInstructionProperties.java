package org.specs.Riscv.instruction;

import static org.specs.Riscv.parsing.RiscvAsmFieldType.UNDEFINED;
import static pt.up.fe.specs.binarytranslation.instruction.InstructionType.G_UNKN;

import java.util.Arrays;
import java.util.List;

import org.specs.Riscv.parsing.RiscvAsmFieldType;
import org.specs.Riscv.parsing.RiscvIsaParser;

import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.parsing.IsaParser;

public enum RiscvInstructionProperties implements InstructionProperties {

    unknown(0xFFFF_FFFF, 1, 1, UNDEFINED, G_UNKN);

    /*
     * Instruction property fields
     */
    private String instructionName;
    private final String enumName;
    private final int opcode; // 32 bit instruction code without operands
    private final int reducedopcode; // only the bits that matter, built after parsing the fields
    private final int latency;
    private final int delay;
    private final RiscvAsmFieldType codetype;
    private final List<InstructionType> genericType;
    private final IsaParser parser = new RiscvIsaParser();

    /*
     * Constructor
     */
    private RiscvInstructionProperties(int opcode, int latency,
            int delay, RiscvAsmFieldType mbtype, List<InstructionType> tp) {
        this.instructionName = name();
        this.enumName = name();
        this.opcode = opcode;
        this.latency = latency;
        this.delay = delay;
        this.codetype = mbtype;
        this.genericType = tp;
        this.reducedopcode = parser.parse(Integer.toHexString(opcode)).getReducedOpcode();
    }

    /*
     * Constructor
     */
    private RiscvInstructionProperties(int opcode, int latency,
            int delay, RiscvAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, latency, delay, mbtype, Arrays.asList(tp));
        this.instructionName = name();
    }

    /*
     * helper method too look up the list
     */
    @Override
    public int getLatency() {
        return this.latency;
    }

    /*
     * helper method too look up the list
     */
    @Override
    public int getDelay() {
        return this.delay;
    }

    /*
     * helper method too get full opcode
     */
    @Override
    public int getOpCode() {
        return this.opcode;
    }

    /*
     * helper method too get only the bits that matter
     */
    @Override
    public int getReducedOpCode() {
        return this.reducedopcode;
    }

    /*
     * helper method too look up type in the list
     */
    @Override
    public List<InstructionType> getGenericType() {
        return this.genericType;
    }

    /*
     * helper method too look up name the list
     */
    @Override
    public String getName() {
        return this.instructionName;
    }

    /*
     * Returns name of enum (should be unique)
     */
    @Override
    public String getEnumName() {
        return enumName;
    }

    /*
     * get code type of a particular instruction
     */
    @Override
    public AsmFieldType getCodeType() {
        return this.codetype;
    }

}
