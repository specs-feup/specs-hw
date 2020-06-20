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

import java.util.*;

import org.specs.MicroBlaze.parsing.*;

import com.google.common.base.Enums;

import pt.up.fe.specs.binarytranslation.instruction.*;
import pt.up.fe.specs.binarytranslation.parsing.*;

public enum MicroBlazeInstructionProperties implements InstructionProperties {

    // name, binary code, latency, delay, subtype
    // within ISA (i.e. instruction binary format), generic type

    // TODO add if operands are permutable

    // SPECIAL
    mts(0x9400_C000, SPECIAL, G_OTHER),
    mtse(0x9500_C000, SPECIAL, G_OTHER),
    mfs(0x9400_8000, SPECIAL, G_OTHER),
    mfse(0x9408_8000, SPECIAL, G_OTHER),
    msrclr(0x9411_0000, SPECIAL, G_OTHER),
    msrset(0x9410_0000, SPECIAL, G_OTHER),

    // MBAR
    mbar(0xB802_0004, MBAR, G_OTHER),

    // UBRANCH
    br(0x9800_0000, UBRANCH, G_UJUMP, G_RJUMP),
    bra(0x9808_0000, UBRANCH, G_UJUMP, G_AJUMP),
    brd(0x9810_0000, 1, 1, UBRANCH, G_UJUMP, G_RJUMP),
    brad(0x9818_0000, 1, 1, UBRANCH, G_UJUMP, G_AJUMP),

    // ULBRANCH
    brld(0x9814_0000, 1, 1, ULBRANCH, G_UJUMP, G_RJUMP),
    brald(0x981C_0000, 1, 1, ULBRANCH, G_UJUMP, G_AJUMP),
    brk(0x980C_0000, ULBRANCH, G_UJUMP, G_RJUMP),

    // UIBRANCH
    bri(0xB800_0000, UIBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),
    brai(0xB808_0000, UIBRANCH, G_UJUMP, G_AJUMP, G_IJUMP),
    brid(0xB810_0000, 1, 1, UIBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),
    braid(0xB818_0000, 1, 1, UIBRANCH, G_UJUMP, G_AJUMP, G_IJUMP),

    // UILBRANCH
    brlid(0xB814_0000, 1, 1, UILBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),
    bralid(0xB81C_0000, 1, 1, UILBRANCH, G_UJUMP, G_AJUMP, G_IJUMP),
    brki(0xB80C_0000, UILBRANCH, G_UJUMP, G_RJUMP, G_IJUMP),

    // brk and brald are same instruction??

    // CBRANCH
    beq(0x9C00_0000, CBRANCH, G_CJUMP, G_RJUMP),
    beqd(0x9E00_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    bge(0x9CA0_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bged(0x9EA0_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    bgt(0x9C80_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bgtd(0x9E80_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    ble(0x9C60_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bled(0x9E60_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    blt(0x9C40_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bltd(0x9E40_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),
    bne(0x9C20_0000, CBRANCH, G_CJUMP, G_RJUMP),
    bned(0x9E20_0000, 1, 1, CBRANCH, G_CJUMP, G_RJUMP),

    // CIBRANCH
    beqi(0xBC00_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    beqid(0xBE00_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgei(0xBCA0_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgeid(0xBEA0_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgti(0xBC80_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bgtid(0xBE80_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    blei(0xBC60_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bleid(0xBE60_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    blti(0xBC40_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bltid(0xBE40_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bnei(0xBC20_0000, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),
    bneid(0xBE20_0000, 1, 1, CIBRANCH, G_CJUMP, G_RJUMP, G_IJUMP),

    // RETURN
    rtbd(0xB640_0000, 1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    rtid(0xB620_0000, 1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    rted(0xB680_0000, 1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),
    rtsd(0xB600_0000, 1, 1, RETURN, G_UJUMP, G_RJUMP, G_IJUMP),

    // IBARREL
    bsrli(0x6400_0000, IBARREL_FMT1, G_LOGICAL),
    bsrai(0x6400_0200, IBARREL_FMT1, G_LOGICAL),
    bslli(0x6400_0400, IBARREL_FMT1, G_LOGICAL),
    bsefi(0x6400_4000, IBARREL_FMT2, G_LOGICAL),
    bsifi(0x6400_8000, IBARREL_FMT2, G_LOGICAL),

    // STREAM
    get(0x6C00_0000, STREAM, G_OTHER),
    put(0x6C00_8000, 2, 0, STREAM, G_OTHER),

    // DSTREAM
    getd(0x4C00_0000, 1, 1, DSTREAM, G_OTHER),
    putd(0x4C00_0400, 2, 1, DSTREAM, G_OTHER),

    // IMM
    imm(0xB000_0000, 1, 1, IMM, G_IMMV),
    // NOTE: I assign "1" to the delay value of imm
    // since it emulates the atomicity of the instruction

    // TYPE A
    add(0x0000_0000, TYPE_A, G_ADD),
    addc(0x0800_0000, TYPE_A, G_ADD),
    addk(0x1000_0000, TYPE_A, G_ADD),
    addkc(0x1800_0000, TYPE_A, G_ADD),
    rsub(0x0400_0000, TYPE_A, G_SUB),
    rsubc(0x0C00_0000, TYPE_A, G_SUB),
    rsubk(0x1400_0000, TYPE_A, G_SUB),
    rsubkc(0x1C00_0000, TYPE_A, G_SUB),
    cmp(0x1400_0001, TYPE_A, G_CMP),
    cmpu(0x1400_0003, TYPE_A, G_CMP),
    mul(0x4000_0000, 3, 0, TYPE_A, G_MUL),
    mulh(0x4000_0001, 3, 0, TYPE_A, G_MUL),
    mulhu(0x4000_0003, 3, 0, TYPE_A, G_MUL),
    mulhsu(0x4000_0002, 3, 0, TYPE_A, G_MUL),
    bsrl(0x4400_0000, TYPE_A, G_LOGICAL),
    bsra(0x4400_0200, TYPE_A, G_LOGICAL),
    bsll(0x4400_0400, TYPE_A, G_LOGICAL),
    idiv(0x4800_0000, 34, 0, TYPE_A, G_OTHER),
    idivu(0x4800_0002, 34, 0, TYPE_A, G_OTHER),
    fadd(0x5800_0000, 6, 0, TYPE_A, G_FLOAT, G_ADD),
    frsub(0x5800_0080, 6, 0, TYPE_A, G_FLOAT, G_SUB),
    fmul(0x5800_0100, 6, 0, TYPE_A, G_FLOAT, G_MUL),
    fdiv(0x5800_0180, 30, 0, TYPE_A, G_FLOAT),
    fcmp_un("fcmp.un", 0x5800_0200, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_lt("fcmp.lt", 0x5800_0210, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_eq("fcmp.eq", 0x5800_0220, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_le("fcmp.le", 0x5800_0230, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_gt("fcmp.gt", 0x5800_0240, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_ne("fcmp.ne", 0x5800_0250, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    fcmp_ge("fcmp.ge", 0x5800_0260, 3, 0, TYPE_A, G_CMP, G_FLOAT),
    flt(0x5800_0280, 6, 0, TYPE_A, G_FLOAT),
    fint(0x5800_0300, 5, 0, TYPE_A, G_FLOAT),
    fsqrt(0x5800_0380, 29, 0, TYPE_A, G_FLOAT),
    or(0x8000_0000, TYPE_A, G_LOGICAL),
    pcmpbf(0x8000_0400, TYPE_A, G_CMP),
    and(0x8400_0000, TYPE_A, G_LOGICAL),
    xor(0x8800_0000, TYPE_A, G_LOGICAL),
    pcmpeq(0x8800_0400, TYPE_A, G_CMP),
    andn(0x8C00_0000, TYPE_A, G_LOGICAL),
    pcmpne(0x8C00_0400, TYPE_A, G_CMP),
    sra(0x9000_0001, TYPE_A, G_UNARY, G_LOGICAL),
    src(0x9000_0021, TYPE_A, G_UNARY, G_LOGICAL),
    srl(0x9000_0041, TYPE_A, G_UNARY, G_LOGICAL),
    sext8(0x9000_0060, TYPE_A, G_OTHER),
    sext16(0x9000_0061, TYPE_A, G_OTHER),
    clz(0x9000_00E0, TYPE_A, G_OTHER),
    swapb(0x9000_01E0, TYPE_A, G_OTHER),
    swaph(0x9000_01E2, TYPE_A, G_OTHER),
    wic(0x9000_0068, TYPE_A, G_OTHER),
    wdc(0x9000_0064, TYPE_A, G_OTHER),
    wdc_flush("wdc.flush", 0x9000_0074, TYPE_A, G_OTHER),
    wdc_clear("wdc.clear", 0x9000_0066, TYPE_A, G_OTHER),
    wdc_clear_ea("wdc.clear.ea", 0x9000_00E6, TYPE_A, G_OTHER),
    wdc_ext_flush("wdc.ext.flush", 0x9000_0476, TYPE_A, G_OTHER),
    wdc_ext_clear("wdc.ext.clear", 0x9000_0466, TYPE_A, G_OTHER),
    lbu(0xC000_0000, 2, 0, TYPE_A, G_LOAD),
    lbur(0xC000_0200, 2, 0, TYPE_A, G_LOAD),
    lbuea(0xC000_0080, 2, 0, TYPE_A, G_LOAD),
    lhu(0xC400_0000, 2, 0, TYPE_A, G_LOAD),
    lhur(0xC400_0200, 2, 0, TYPE_A, G_LOAD),
    lhuea(0xC400_0080, 2, 0, TYPE_A, G_LOAD),
    lw(0xC800_0000, 2, 0, TYPE_A, G_LOAD),
    lwr(0xC800_0200, 2, 0, TYPE_A, G_LOAD),
    lwx(0xC800_0400, 2, 0, TYPE_A, G_LOAD),
    lwea(0xC800_0080, 2, 0, TYPE_A, G_LOAD),
    sb(0xD000_0000, 2, 0, TYPE_A, G_STORE),
    sbr(0xD000_0200, 2, 0, TYPE_A, G_STORE),
    sbea(0xD000_0080, 2, 0, TYPE_A, G_STORE),
    sh(0xD400_0000, 2, 0, TYPE_A, G_STORE),
    shr(0xD400_0200, 2, 0, TYPE_A, G_STORE),
    shea(0xD400_0080, 2, 0, TYPE_A, G_STORE),
    sw(0xD800_0000, 2, 0, TYPE_A, G_STORE),
    swr(0xD800_0200, 2, 0, TYPE_A, G_STORE),
    swx(0xD800_0400, 2, 0, TYPE_A, G_STORE),
    swea(0xD800_0080, 2, 0, TYPE_A, G_STORE),

    // TYPE B
    addi(0x2000_0000, TYPE_B, G_ADD),
    addic(0x2800_0000, TYPE_B, G_ADD),
    addik(0x3000_0000, TYPE_B, G_ADD),
    addikc(0x3800_0000, TYPE_B, G_ADD),
    rsubi(0x2400_0000, TYPE_B, G_SUB),
    rsubic(0x2C00_0000, TYPE_B, G_SUB),
    rsubik(0x3400_0000, TYPE_B, G_SUB),
    rsubikc(0x3C00_0000, TYPE_B, G_SUB),
    muli(0x6000_0000, 3, 0, TYPE_B, G_MUL),
    ori(0xA000_0000, TYPE_B, G_LOGICAL),
    andi(0xA400_0000, TYPE_B, G_LOGICAL),
    xori(0xA800_0000, TYPE_B, G_LOGICAL),
    andni(0xAC00_0000, TYPE_B, G_LOGICAL),
    lbui(0xE000_0000, 2, 0, TYPE_B, G_LOAD),
    lhui(0xE400_0000, 2, 0, TYPE_B, G_LOAD),
    lwi(0xE800_0000, 2, 0, TYPE_B, G_LOAD),
    sbi(0xF000_0000, 2, 0, TYPE_B, G_STORE),
    shi(0xF400_0000, 2, 0, TYPE_B, G_STORE),
    swi(0xF800_0000, 2, 0, TYPE_B, G_STORE),

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
    private final MicroBlazeAsmFieldType codetype;
    private final List<InstructionType> genericType;
    private final IsaParser parser = new MicroBlazeIsaParser();

    /*
     * Constructor
     */
    private MicroBlazeInstructionProperties(int opcode, int latency,
            int delay, MicroBlazeAsmFieldType mbtype, List<InstructionType> tp) {
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
    private MicroBlazeInstructionProperties(int opcode, int latency,
            int delay, MicroBlazeAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, latency, delay, mbtype, Arrays.asList(tp));
        this.instructionName = name();
    }

    /*
     * Helper constructor with explicit name for instruction
     */
    private MicroBlazeInstructionProperties(String explicitname, int opcode, int latency,
            int delay, MicroBlazeAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, latency, delay, mbtype, Arrays.asList(tp));
        this.instructionName = explicitname;
    }

    /*
     * Helper constructor with default latency and delay slot
     */
    private MicroBlazeInstructionProperties(String explicitname, int opcode,
            MicroBlazeAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, 1, 0, mbtype, Arrays.asList(tp));
        this.instructionName = explicitname;
    }

    /*
     * Helper constructor with default latency and delay slot
     */
    private MicroBlazeInstructionProperties(int opcode,
            MicroBlazeAsmFieldType mbtype, InstructionType... tp) {
        this(opcode, 1, 0, mbtype, Arrays.asList(tp));
    }

    /*
     * helper method too look up the list
     */
    public int getLatency() {
        return this.latency;
    }

    /*
     * helper method too look up the list
     */
    public int getDelay() {
        return this.delay;
    }

    /*
     * helper method too get full opcode
     */
    public int getOpCode() {
        return this.opcode;
    }

    /*
     * helper method too get only the bits that matter
     */
    public int getReducedOpCode() {
        return this.reducedopcode;
    }

    /*
     * helper method too look up type in the list
     */
    public List<InstructionType> getGenericType() {
        return this.genericType;
    }

    /*
     * helper method too look up name the list
     */
    public String getName() {
        return this.instructionName;
    }

    /*
     * Returns name of enum (should be unique)
     */
    public String getEnumName() {
        return enumName;
    }

    /*
     * get code type of a particular instruction
     */
    public AsmFieldType getCodeType() {
        return this.codetype;
    }

    /*
     * 
     *
    public InstructionExpression getExpression() {
        // get expression assuming that expression enum name is equal to
        // enum name of properties enum
        try {
            return MicroBlazeInstructionExpression.valueOf(this.enumName);
        } catch (Exception ex) {
            return null;
        }
    }*/

    /*
     * Only used for Junit tests!
     */
    public AsmFieldData getFieldData() {
        return parser.parse(Integer.toHexString(opcode));
    }
}
