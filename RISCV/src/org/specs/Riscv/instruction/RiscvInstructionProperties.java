package org.specs.Riscv.instruction;

import static org.specs.Riscv.parsing.RiscvAsmFieldType.*;
import static pt.up.fe.specs.binarytranslation.instruction.InstructionType.*;

import java.util.Arrays;
import java.util.List;

import org.specs.Riscv.parsing.RiscvAsmFieldType;
import org.specs.Riscv.parsing.RiscvIsaParser;

import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.parsing.IsaParser;

public enum RiscvInstructionProperties implements InstructionProperties {

    // R-type: | funct7 | rs2 | rs1 | funct3 | rd | 0110011 |
    add(0x0000_0033, R, G_ADD),
    sub(0x4000_0033, R, G_SUB),
    sll(0x0000_0833, R, G_LOGICAL),
    slt(0x0000_1033, R, G_LOGICAL),
    sltu(0x0000_1833, R, G_LOGICAL),
    xor(0x0000_2033, R, G_LOGICAL),
    srl(0x0000_2833, R, G_LOGICAL),
    sra(0x4000_2833, R, G_LOGICAL),
    or(0x0000_3033, R, G_LOGICAL),
    and(0x0000_3833, R, G_LOGICAL),

    // I-type: | imm12bits | rs1 | funct3 | rd | 0010011 |
    addi(0x0000_0013, I, G_ADD),
    slti(0x0000_2013, I, G_LOGICAL),
    sltiu(0x0000_3013, I, G_LOGICAL),
    xori(0x0000_4013, I, G_LOGICAL),
    ori(0x0000_6013, I, G_LOGICAL),
    andi(0x0000_7013, I, G_LOGICAL),
    slli(0x0000_0813, I, G_LOGICAL),
    srli(0x0000_2813, I, G_LOGICAL),
    srai(0x4000_2813, I, G_LOGICAL),

    // S-type: | imm7bits | rs2 | rs1 | funct3 | imm5bits | 0100111 |
    sb(0x0000_0023, S, G_STORE),
    sh(0x0000_1023, S, G_STORE),
    sw(0x0000_2023, S, G_STORE),

    // U-type: | imm20bits | rd | 0x10111 |
    lui(0x0000_0037, U, G_STORE),
    auipc(0x0000_0017, U, G_STORE),

    // LOAD: | imm12bits | rs1 | funct3 | rd | 0000011 |
    lb(0x0000_0003, LOAD, G_LOAD),
    lh(0x0000_1003, LOAD, G_LOAD),
    lw(0x0000_2003, LOAD, G_LOAD),
    lbu(0x0000_4003, LOAD, G_LOAD),
    lhu(0x0000_5003, LOAD, G_LOAD),

    // JALR: | imm12bits | rs1 | funct3 | rd | 1100111 |
    jalr(0x0000_0067, JALR, G_UJUMP, G_AJUMP),

    // SB-type: | bit12 | imm6bits | rs2 | rs1 | funct3 | imm4bits | bit11 | 1100011 |
    beq(0x0000_00c7, SB, G_CJUMP, G_RJUMP),
    bne(0x0000_10c7, SB, G_CJUMP, G_RJUMP),
    blt(0x0000_40c7, SB, G_CJUMP, G_RJUMP),
    bge(0x0000_50c7, SB, G_CJUMP, G_RJUMP),
    bltu(0x0000_60c7, SB, G_CJUMP, G_RJUMP),
    bgeu(0x0000_70c7, SB, G_CJUMP, G_RJUMP),

    // UJ-type: | bit20 | imm10bits | bit11 | imm8bits | rd | 1101111 |
    jal(0x0000_006f, UJ, G_UJUMP, G_AJUMP),

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
    private final AsmFieldData fieldData; // decoded fields of this instruction
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

        // use the parser to initialize private fields of instruction set itself
        this.fieldData = parser.parse(Integer.toHexString(opcode));
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
     * Helper constructor with default latency and delay slot
     */
    private RiscvInstructionProperties(int opcode,
            RiscvAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, 1, 0, mbtype, Arrays.asList(tp));
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

    /*
     * Only used for Junit tests!
     */
    @Override
    public AsmFieldData getFieldData() {
        return this.fieldData;
    }
}
