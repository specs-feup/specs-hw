/**
 * Copyright 2019 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.specs.MicroBlaze.instruction;

import static org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType.*;
import static pt.up.fe.specs.binarytranslation.instruction.InstructionType.*;

import java.util.Arrays;
import java.util.List;

import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;
import org.specs.MicroBlaze.parsing.MicroBlazeIsaParser;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.asm.parsing.IsaParser;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;

public enum MicroBlazeInstructionProperties implements InstructionProperties {

    // name, binary code, latency, delay, subtype
    // within ISA (i.e. instruction binary format), generic type

    // TODO add if operands are permutable

    // SPECIAL
    mts(SPECIAL, G_OTHER),
    mtse(SPECIAL, G_OTHER),
    mfs(SPECIAL, G_OTHER),
    mfse(SPECIAL, G_OTHER),
    msrclr(SPECIAL, G_OTHER),
    msrset(SPECIAL, G_OTHER),

    // MBAR
    mbar(MBAR, G_OTHER),

    // UBRANCH
    br(UBRANCH, G_UJUMP, G_RJUMP),
    bra(UBRANCH, G_UJUMP, G_AJUMP),
    brd(1, 1, UBRANCH, G_UJUMP, G_RJUMP),
    brad(1, 1, UBRANCH, G_UJUMP, G_AJUMP),

    // ULBRANCH
    brld(1, 1, ULBRANCH, G_UJUMP, G_RJUMP),
    brald(1, 1, ULBRANCH, G_UJUMP, G_AJUMP),
    brk(ULBRANCH, G_UJUMP, G_RJUMP),

    // UIBRANCH
    bri(UIBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),
    brai(UIBRANCH, G_UJUMP, G_AJUMP, G_IJUMP),
    brid(1, 1, UIBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),
    braid(1, 1, UIBRANCH, G_UJUMP, G_AJUMP, G_IJUMP),

    // UILBRANCH
    brlid(1, 1, UILBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),
    bralid(1, 1, UILBRANCH, G_UJUMP, G_AJUMP, G_IJUMP),
    brki(UILBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),

    // CBRANCH
    beq(CBRANCH, G_CJUMP, G_RJUMP),
    beqd(1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    bge(CBRANCH, G_CJUMP, G_RJUMP),
    bged(1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    bgt(CBRANCH, G_CJUMP, G_RJUMP),
    bgtd(1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    ble(CBRANCH, G_CJUMP, G_RJUMP),
    bled(1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    blt(CBRANCH, G_CJUMP, G_RJUMP),
    bltd(1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    bne(CBRANCH, G_CJUMP, G_RJUMP),
    bned(1, 1, CBRANCH, G_CJUMP, G_RJUMP),

    // CIBRANCH
    beqi(CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    beqid(1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgei(CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgeid(1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgti(CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgtid(1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    blei(CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bleid(1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    blti(CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bltid(1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bnei(CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bneid(1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),

    // RETURN
    rtbd(1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    rtid(1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    rted(1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    rtsd(1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),

    // IBARREL
    bsrli(IBARREL_FMT1, G_LOGICAL),
    bsrai(IBARREL_FMT1, G_LOGICAL),
    bslli(IBARREL_FMT1, G_LOGICAL),
    bsefi(IBARREL_FMT2, G_LOGICAL),
    bsifi(IBARREL_FMT2, G_LOGICAL),

    // STREAM
    get(STREAM, G_OTHER),
    put(2, 0, STREAM, G_OTHER),

    // DSTREAM
    getd(1, 1, DSTREAM, G_OTHER),
    putd(2, 1, DSTREAM, G_OTHER),

    // IMM
    imm(1, 1, IMM, G_IMMV),
    // NOTE: I assign "1" to the delay value of imm
    // since it emulates the atomicity of the instruction

    // TYPE A
    add(TYPE_A, G_ADD),
    addc(TYPE_A, G_ADD),
    addk(TYPE_A, G_ADD),
    addkc(TYPE_A, G_ADD),
    rsub(TYPE_A, G_SUB),
    rsubc(TYPE_A, G_SUB),
    rsubk(TYPE_A, G_SUB),
    rsubkc(TYPE_A, G_SUB),
    cmp(TYPE_A, G_CMP),
    cmpu(TYPE_A, G_CMP),
    mul(3, 0, TYPE_A, G_MUL),
    mulh(3, 0, TYPE_A, G_MUL),
    mulhu(3, 0, TYPE_A, G_MUL),
    mulhsu(3, 0, TYPE_A, G_MUL),
    bsrl(TYPE_A, G_LOGICAL),
    bsra(TYPE_A, G_LOGICAL),
    bsll(TYPE_A, G_LOGICAL),
    idiv(34, 0, TYPE_A, G_OTHER),
    idivu(34, 0, TYPE_A, G_OTHER),
    fadd(6, 0, TYPE_A, G_FLOAT, G_ADD),
    frsub(6, 0, TYPE_A, G_FLOAT, G_SUB),
    fmul(6, 0, TYPE_A, G_FLOAT, G_MUL),
    fdiv(30, 0, TYPE_A, G_FLOAT),
    fcmp_un("fcmp.un", 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_lt("fcmp.lt", 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_eq("fcmp.eq", 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_le("fcmp.le", 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_gt("fcmp.gt", 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_ne("fcmp.ne", 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_ge("fcmp.ge", 3, 0, TYPE_A, G_CMP, G_FLOAT),
    flt(6, 0, TYPE_A, G_FLOAT),
    fint(5, 0, TYPE_A, G_FLOAT),
    fsqrt(29, 0, TYPE_A, G_FLOAT),
    or(TYPE_A, G_LOGICAL),
    pcmpbf(TYPE_A, G_CMP),
    and(TYPE_A, G_LOGICAL),
    xor(TYPE_A, G_LOGICAL),
    pcmpeq(TYPE_A, G_CMP),
    andn(TYPE_A, G_LOGICAL),
    pcmpne(TYPE_A, G_CMP),
    sra(TYPE_A, G_UNARY, G_LOGICAL),
    src(TYPE_A, G_UNARY, G_LOGICAL),
    srl(TYPE_A, G_UNARY, G_LOGICAL),
    sext8(TYPE_A, G_OTHER),
    sext16(TYPE_A, G_OTHER),
    clz(TYPE_A, G_OTHER),
    swapb(TYPE_A, G_OTHER),
    swaph(TYPE_A, G_OTHER),
    wic(TYPE_A, G_OTHER),
    wdc(TYPE_A, G_OTHER),
    wdc_flush("wdc.flush", TYPE_A, G_OTHER),
    wdc_clear("wdc.clear", TYPE_A, G_OTHER),
    wdc_clear_ea("wdc.clear.ea", TYPE_A, G_OTHER),
    wdc_ext_flush("wdc.ext.flush", TYPE_A, G_OTHER),
    wdc_ext_clear("wdc.ext.clear", TYPE_A, G_OTHER),
    lbu(2, 0, TYPE_A, G_LOAD),
    lbur(2, 0, TYPE_A, G_LOAD),
    lbuea(2, 0, TYPE_A, G_LOAD),
    lhu(2, 0, TYPE_A, G_LOAD),
    lhur(2, 0, TYPE_A, G_LOAD),
    lhuea(2, 0, TYPE_A, G_LOAD),
    lw(2, 0, TYPE_A, G_LOAD),
    lwr(2, 0, TYPE_A, G_LOAD),
    lwx(2, 0, TYPE_A, G_LOAD),
    lwea(2, 0, TYPE_A, G_LOAD),
    sb(2, 0, TYPE_A, G_STORE),
    sbr(2, 0, TYPE_A, G_STORE),
    sbea(2, 0, TYPE_A, G_STORE),
    sh(2, 0, TYPE_A, G_STORE),
    shr(2, 0, TYPE_A, G_STORE),
    shea(2, 0, TYPE_A, G_STORE),
    sw(2, 0, TYPE_A, G_STORE),
    swr(2, 0, TYPE_A, G_STORE),
    swx(2, 0, TYPE_A, G_STORE),
    swea(2, 0, TYPE_A, G_STORE),

    // TYPE B
    addi(TYPE_B, G_ADD),
    addic(TYPE_B, G_ADD),
    addik(TYPE_B, G_ADD),
    addikc(TYPE_B, G_ADD),
    rsubi(TYPE_B, G_SUB),
    rsubic(TYPE_B, G_SUB),
    rsubik(TYPE_B, G_SUB),
    rsubikc(TYPE_B, G_SUB),
    muli(3, 0, TYPE_B, G_MUL),
    ori(TYPE_B, G_LOGICAL),
    andi(TYPE_B, G_LOGICAL),
    xori(TYPE_B, G_LOGICAL),
    andni(TYPE_B, G_LOGICAL),
    lbui(2, 0, TYPE_B, G_LOAD),
    lhui(2, 0, TYPE_B, G_LOAD),
    lwi(2, 0, TYPE_B, G_LOAD),
    sbi(2, 0, TYPE_B, G_STORE),
    shi(2, 0, TYPE_B, G_STORE),
    swi(2, 0, TYPE_B, G_STORE),

    unknown(UNDEFINED, G_UNKN);

    /*
     * Instruction property fields
     */
    private final int delay;
    private final int latency;
    private final String enumName;
    private String instructionName;
    private final int reducedopcode; // only the bits that matter, built after parsing the fields
    private final MicroBlazeInstructionEncoding opcode; // 32 bit instruction code without operands
    private final MicroBlazeAsmFieldType codetype;
    private final List<InstructionType> genericType;
    private final AsmFieldData fieldData; // decoded fields of this instruction
    private final IsaParser parser = new MicroBlazeIsaParser();

    /*
     * Constructor
     */
    private MicroBlazeInstructionProperties(int latency,
            int delay, MicroBlazeAsmFieldType mbtype, List<InstructionType> tp) {
        this.instructionName = name();
        this.enumName = name();
        this.opcode = MicroBlazeInstructionEncoding.valueOf(name());
        this.latency = latency;
        this.delay = delay;
        this.codetype = mbtype;
        this.genericType = tp;

        // use the parser to initialize private fields of instruction set itself
        this.fieldData = parser.parse(opcode.getCode());
        this.reducedopcode = fieldData.getReducedOpcode();
    }

    /*
     * Constructor
     */
    private MicroBlazeInstructionProperties(int latency,
            int delay, MicroBlazeAsmFieldType mbtype, InstructionType... tp) {
        this(latency, delay, mbtype, Arrays.asList(tp));
    }

    /*
     * Helper constructor with explicit name for instruction
     */
    private MicroBlazeInstructionProperties(String explicitname, int latency,
            int delay, MicroBlazeAsmFieldType mbtype, InstructionType... tp) {
        this(latency, delay, mbtype, Arrays.asList(tp));
        this.instructionName = explicitname;
    }

    /*
     * Helper constructor with default latency and delay slot
     */
    private MicroBlazeInstructionProperties(String explicitname,
            MicroBlazeAsmFieldType mbtype, InstructionType... tp) {
        this(1, 0, mbtype, Arrays.asList(tp));
        this.instructionName = explicitname;
    }

    /*
     * Helper constructor with default latency and delay slot
     */
    private MicroBlazeInstructionProperties(
            MicroBlazeAsmFieldType mbtype, InstructionType... tp) {
        this(1, 0, mbtype, Arrays.asList(tp));
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
        return (int) Long.parseLong(this.opcode.getCode(), 16);
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
