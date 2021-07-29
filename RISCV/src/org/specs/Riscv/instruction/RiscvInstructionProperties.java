package org.specs.Riscv.instruction;

import static org.specs.Riscv.parsing.RiscvAsmFieldType.*;
import static pt.up.fe.specs.binarytranslation.instruction.InstructionType.*;

import java.util.Arrays;
import java.util.List;

import org.specs.Riscv.parsing.RiscvAsmFieldType;
import org.specs.Riscv.parsing.RiscvIsaParser;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.asm.parsing.IsaParser;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;

public enum RiscvInstructionProperties implements InstructionProperties {

    // https://riscv.org/wp-content/uploads/2017/05/riscv-spec-v2.2.pdf

    ///////////////////////////////////////////////////////////////////////////
    // I Extension ////////////////////////////////////////////////////////////

    // R-type: | funct7 | rs2 | rs1 | funct3 | rd | opcode |
    add(0x0000_0033, OP, G_ADD),
    sub(0x4000_0033, OP, G_SUB),
    sll(0x0000_1033, OP, G_LOGICAL),
    slt(0x0000_2033, OP, G_LOGICAL),
    sltu(0x0000_3033, OP, G_LOGICAL),
    xor(0x0000_4033, OP, G_LOGICAL),
    srl(0x0000_5833, OP, G_LOGICAL),
    sra(0x4000_5833, OP, G_LOGICAL),
    or(0x0000_6033, OP, G_LOGICAL),
    and(0x0000_7033, OP, G_LOGICAL),

    // I-type: | imm12bits | rs1 | funct3 | rd | opcode |
    addi(0x0000_0013, OPIMM, G_ADD),
    slti(0x0000_2013, OPIMM, G_LOGICAL),
    sltiu(0x0000_3013, OPIMM, G_LOGICAL),
    xori(0x0000_4013, OPIMM, G_LOGICAL),
    ori(0x0000_6013, OPIMM, G_LOGICAL),
    andi(0x0000_7013, OPIMM, G_LOGICAL),
    slli(0x0000_1013, OPIMM, G_LOGICAL),
    srli(0x0000_5013, OPIMM, G_LOGICAL),
    srai(0x4000_5013, OPIMM, G_LOGICAL),
    lb(0x0000_0003, LOAD, G_LOAD),
    lh(0x0000_1003, LOAD, G_LOAD),
    lw(0x0000_2003, LOAD, G_LOAD),
    lbu(0x0000_4003, LOAD, G_LOAD),
    lhu(0x0000_5003, LOAD, G_LOAD),
    jalr(0x0000_0067, JALR, G_UJUMP, G_AJUMP),

    // S-type: | imm7bits | rs2 | rs1 | funct3 | imm5bits | opcode |
    sb(0x0000_0023, STORE, G_STORE),
    sh(0x0000_1023, STORE, G_STORE),
    sw(0x0000_2023, STORE, G_STORE),

    // U-type: | imm20bits | rd | 0x10111 |
    lui(0x0000_0037, LUI, G_STORE),
    auipc(0x0000_0017, AUIPC, G_STORE),

    // B-type: | bit12 | imm6bits | rs2 | rs1 | funct3 | imm4bits | bit11 | 1100011 |
    beq(0x0000_0063, BRANCH, G_CJUMP, G_RJUMP),
    bne(0x0000_1063, BRANCH, G_CJUMP, G_RJUMP),
    blt(0x0000_4063, BRANCH, G_CJUMP, G_RJUMP),
    bge(0x0000_5063, BRANCH, G_CJUMP, G_RJUMP),
    bltu(0x0000_6063, BRANCH, G_CJUMP, G_RJUMP),
    bgeu(0x0000_7063, BRANCH, G_CJUMP, G_RJUMP),

    // J-type: | bit20 | imm10bits | bit11 | imm8bits | rd | opcode |
    jal(0x0000_006f, JAL, G_UJUMP, G_AJUMP),

    ///////////////////////////////////////////////////////////////////////////
    // M Extension ////////////////////////////////////////////////////////////
    // R-type: | funct7 | rs2 | rs1 | funct3 | rd | opcode |
    mul(0x0200_0033, OP, G_MUL),
    mulh(0x0200_0833, OP, G_MUL),
    mulhsu(0x0200_1033, OP, G_MUL),
    mulhu(0x0200_1833, OP, G_MUL),
    div(0x0200_2033, OP, G_DIV),
    divu(0x0200_2833, OP, G_DIV),
    rem(0x0200_3033, OP, G_OTHER),
    remu(0x0200_3833, OP, G_OTHER),

    ///////////////////////////////////////////////////////////////////////////
    // A Extension ////////////////////////////////////////////////////////////
    // R-type: | funct7 | rs2 | rs1 | funct3 | rd | opcode |
    lr_w("lr.w", 0x1000_202F, AMO, G_LOAD),
    sc_w("sc.w", 0x1800_202F, AMO, G_STORE),
    amoswap_w("amoswap.w", 0x0800_202F, AMO, G_MEMORY),
    amoadd_w("amoadd.w", 0x0000_202F, AMO, G_MEMORY),
    amoxor_w("amoxor.w", 0x2000_202F, AMO, G_MEMORY),
    amoand_w("amoand.w", 0x6000_202F, AMO, G_MEMORY),
    amoor_w("amoor.w", 0x4000_202F, AMO, G_MEMORY),
    amomin_w("amomin.w", 0x8000_202F, AMO, G_MEMORY),
    amomax_w("amomax.w", 0xA000_202F, AMO, G_MEMORY),
    amominu_w("amominu.w", 0xC000_202F, AMO, G_MEMORY),
    amomaxu_w("amomaxu.w", 0xE000_202F, AMO, G_MEMORY),

    ///////////////////////////////////////////////////////////////////////////
    // F Extension ////////////////////////////////////////////////////////////
    // R-type: | funct7 | rs2 | rs1 | funct3 | rd | opcode |
    fadd_s("fadd.s", 0x0000_0053, OPFPb, G_FLOAT, G_ADD), //
    fsub_s("fsub.s", 0x0800_0053, OPFPb, G_FLOAT, G_SUB), //
    fmul_s("fmul.s", 0x1000_0053, OPFPb, G_FLOAT, G_MUL),
    fdiv_s("fdiv.s", 0x1800_0053, OPFPb, G_FLOAT, G_DIV),
    fsqrt_s("fsqrt.s", 0x5800_0053, OPFPb, G_FLOAT, G_OTHER),

    fsqnj_s("fsqnj.s", 0x2000_0053, OPFPa, G_FLOAT, G_OTHER),
    fsqrtn_s("fsqrtn.s", 0x2000_1053, OPFPa, G_FLOAT, G_OTHER),
    fsqnjx_s("fsqnjx.s", 0x2000_2053, OPFPa, G_FLOAT, G_OTHER),
    fmin_s("fmin.s", 0x2800_0053, OPFPa, G_FLOAT, G_OTHER),
    fmax_s("fmax.s", 0x2800_1053, OPFPa, G_FLOAT, G_OTHER),

    /*
    fcvt_w_s("fcvt.w.s", 0xC000_0053, OPFPb, G_FLOAT, G_OTHER), // here rs2 is opcode...
    fcvt_wu_s("fcvt.wu.s", 0xC000_0053, OPFPb, G_FLOAT, G_OTHER), // here rs2 is opcode...    
    fmv_x_w("fmv.x.w", 0xE000_0053, OPFPa, G_FLOAT, G_OTHER),
    */

    feq_s("feq.s", 0xA000_0053, OPFPa, G_FLOAT, G_OTHER),
    flt_s("flt.s", 0xA000_1053, OPFPa, G_FLOAT, G_OTHER),
    fle_s("fle.s", 0xA000_2053, OPFPa, G_FLOAT, G_OTHER),

    /*
    fclass_s("fclass.s", 0xE000_0053, OPFPa, G_FLOAT, G_OTHER),
    fcvt_s_w("fcvt.s.w", 0xD000_0053, OPFPb, G_FLOAT, G_OTHER),
    fcvt_s_wu("fcvt.s.wu", 0xD000_0053, OPFPb, G_FLOAT, G_OTHER), // here rs2 is opcode...
    */

    fmv_w_x("fmv.w.x", 0xF000_0053, OPFPa, G_FLOAT, G_OTHER),

    // R4-type: | rs3 | funct2 | rs2 | rs1 | funct3 | rd | opcode |
    fmadd_s("fmadd.s", 0x0000_0043, MADD, G_FLOAT, G_ADD),
    fmsub_s("fmsub.s", 0x0000_0047, MSUB, G_FLOAT, G_SUB),
    fnmadd_s("fnmadd.s", 0x0000_004F, NMADD, G_FLOAT, G_ADD),
    fnmsub_s("fnmsub.s", 0x0000_004B, NMSUB, G_FLOAT, G_SUB),

    // I-type: | imm12bits | rs1 | funct3 | rd | opcode |
    flw(0x0000_2007, LOADFP, G_LOAD),

    // S-type: | imm7bits | rs2 | rs1 | funct3 | imm5bits | opcode |
    fsw(0x0000_2027, STOREFP, G_STORE),

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
     * Helper constructor with explicit name for instruction
     */
    private RiscvInstructionProperties(String explicitname, int opcode, int latency,
            int delay, RiscvAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, latency, delay, mbtype, Arrays.asList(tp));
        this.instructionName = explicitname;
    }

    /*
     * Helper constructor with default latency and delay slot size
     */
    private RiscvInstructionProperties(String explicitname, int opcode,
            RiscvAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, 1, 0, mbtype, Arrays.asList(tp));
        this.instructionName = explicitname;
    }

    /*
     * Helper constructor with default latency and delay slot size
     */
    private RiscvInstructionProperties(int opcode,
            RiscvAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, 1, 0, mbtype, Arrays.asList(tp));
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

    /*
     * Only used for Junit tests!
     */
    @Override
    public AsmFieldData getFieldData() {
        return this.fieldData;
    }
}
