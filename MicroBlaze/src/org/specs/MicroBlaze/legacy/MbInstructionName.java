/*
 * Copyright 2010 SPeCS Research Group.
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

package org.specs.MicroBlaze.legacy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.specs.MicroBlaze.InstructionProperties;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.asm.processor.InstructionName;

/**
 * List of instructions in the MicroBlaze Architecture, according to MicroBlaze Processor Reference Guide v10.3.
 *
 * @author Joao Bispo
 */
public enum MbInstructionName implements InstructionName {

    add(0b000000),
    addc(0b000010),
    addk(0b000100),
    addkc(0b000110),
    addi(0b001000),
    addic(0b001010),
    addik(0b001100),
    addikc(0b001110),
    and(0b100001),
    andi(0b101001),
    andn(0b100011),
    andni(0b101011),
    beq(0b100111),
    beqd(0b100111),
    beqi(0b101111),
    beqid(0b101111),
    bge(0b100111),
    bged(0b100111),
    bgei(0b101111),
    bgeid(0b101111),
    bgt(0b100111),
    bgtd(0b100111),
    bgti(0b101111),
    bgtid(0b101111),
    ble(0b100111),
    bled(0b100111),
    blei(0b101111),
    bleid(0b101111),
    blt(0b100111),
    bltd(0b100111),
    blti(0b101111),
    bltid(0b101111),
    bne(0b100111),
    bned(0b100111),
    bnei(0b101111),
    bneid(0b101111),
    br(0b100110),
    bra(0b100110),
    brd(0b100110),
    brad(0b100110),
    brld(0b100110),
    brald(0b100110),
    bri(0b101110),
    brai(0b101110),
    brid(0b101110),
    braid(0b101110),
    brlid(0b101110),
    bralid(0b101110),
    brk(0b100110),
    brki(0b101110),
    bsrl(0b010001),
    bsra(0b010001),
    bsll(0b010001),
    bsrli(0b011001),
    bsrai(0b011001),
    bslli(0b011001),
    clz(0b100100),
    cmp(0b000101),
    cmpu(0b000101),
    fadd(0b010110),
    frsub(0b010110),
    fmul(0b010110),
    fdiv(0b010110),
    fcmp_un(0b010110, "fcmp.un"),
    fcmp_lt(0b010110, "fcmp.lt"),
    fcmp_eq(0b010110, "fcmp.eq"),
    fcmp_le(0b010110, "fcmp.le"),
    fcmp_gt(0b010110, "fcmp.gt"),
    fcmp_ne(0b010110, "fcmp.ne"),
    fcmp_ge(0b010110, "fcmp.ge"),
    flt(0b010110),
    fint(0b010110),
    fsqrt(0b010110),
    get(0b011011),
    getd(0b010011),
    idiv(0b010010),
    idivu(0b010010),
    imm(0b101100),
    lbu(0b110000),
    lbur(0b110000),
    lbui(0b111000),
    lhu(0b110001),
    lhur(0b110001),
    lhui(0b111001),
    lw(0b110010),
    lwr(0b110010),
    lwi(0b111010),
    lwx(0b110010),
    mbar(0b101110),
    mfs(0b100101),
    msrclr(0b100101),
    msrset(0b100101),
    mts(0b100101),
    mul(0b010000),
    mulh(0b010000),
    mulhu(0b010000),
    mulhsu(0b010000),
    muli(0b011000),
    or(0b100000),
    ori(0b101000),
    pcmpbf(0b100000),
    pcmpeq(0b100010),
    pcmpne(0b100011),
    put(0b011011),
    putd(0b010011),
    // There are a lot of puts...
    rsub(0b000001),
    rsubc(0b000011),
    rsubk(0b000101),
    rsubkc(0b000111),
    rsubi(0b001001),
    rsubic(0b001011),
    rsubik(0b001101),
    rsubikc(0b001111),
    rtbd(0b101101),
    rtid(0b101101),
    rted(0b101101),
    rtsd(0b101101),
    sb(0b110100),
    sbr(0b110100),
    sbi(0b111100),
    sext16(0b100100),
    sext8(0b100100),
    sh(0b110101),
    shr(0b110101),
    shi(0b111101),
    sra(0b100100),
    src(0b100100),
    srl(0b100100),
    sw(0b110110),
    swr(0b110110),
    swapb(0b100100),
    swaph(0b100100),
    swi(0b111110),
    swx(0b110110),
    wdc(0b100100),
    // There are other wdc, with a '.'
    wdc_flush(0b100100, "wdc.flush"),
    wdc_clear(0b100100, "wdc.clear"),
    wdc_ext_flush(0b100100, "wdc.ext.flush"),
    wdc_ext_clear(0b100100, "wdc.ext.clear"),
    wic(0b100100),
    xor(0b100010),
    xori(0b101010);

    /**
     * INSTANCE VARIABLES
     */
    private final String instructionName;
    private final int opcode;
    private final boolean specialName;

    private final static Map<String, MbInstructionName> specialCases;

    static {
        specialCases = new HashMap<>();
        for (MbInstructionName mbInstructionName : values()) {
            if (!mbInstructionName.specialName) {
                continue;
            }

            specialCases.put(mbInstructionName.getName(), mbInstructionName);
        }
    }

    public static final Collection<String> storeInstructions;
    public static final Collection<String> loadInstructions;

    static {
        storeInstructions = new ArrayList<>();

        for (MbInstructionName instName : InstructionProperties.STORE_INSTRUCTIONS) {
            storeInstructions.add(instName.getName());
        }

        loadInstructions = new ArrayList<>();

        for (MbInstructionName instName : InstructionProperties.LOAD_INSTRUCTIONS) {
            loadInstructions.add(instName.getName());
        }
    }

    private final static Map<MbInstructionName, Integer> PSEUDO_RD;

    static {
        PSEUDO_RD = new EnumMap<>(MbInstructionName.class);
        PSEUDO_RD.put(beq, 0b00000);
        PSEUDO_RD.put(beqd, 0b10000);
        PSEUDO_RD.put(beqi, 0b00000);
        PSEUDO_RD.put(beqid, 0b10000);
        PSEUDO_RD.put(bge, 0b00101);
        PSEUDO_RD.put(bged, 0b10101);
        PSEUDO_RD.put(bgei, 0b00101);
        PSEUDO_RD.put(bgeid, 0b10101);
        PSEUDO_RD.put(bgt, 0b00100);
        PSEUDO_RD.put(bgtd, 0b10100);
        PSEUDO_RD.put(bgti, 0b00100);
        PSEUDO_RD.put(bgtid, 0b10100);
        PSEUDO_RD.put(ble, 0b00011);
        PSEUDO_RD.put(bled, 0b10011);
        PSEUDO_RD.put(blei, 0b00011);
        PSEUDO_RD.put(bleid, 0b10011);
        PSEUDO_RD.put(blt, 0b00010);
        PSEUDO_RD.put(bltd, 0b10010);
        PSEUDO_RD.put(blti, 0b00010);
        PSEUDO_RD.put(bltid, 0b10010);
        PSEUDO_RD.put(bne, 0b00001);
        PSEUDO_RD.put(bned, 0b10001);
        PSEUDO_RD.put(bnei, 0b00001);
        PSEUDO_RD.put(bneid, 0b10001);
    }

    private final static Map<MbInstructionName, Integer> PSEUDO_RA;

    static {
        PSEUDO_RA = new EnumMap<>(MbInstructionName.class);

        PSEUDO_RA.put(br, 0b00000);
        PSEUDO_RA.put(bra, 0b01000);
        PSEUDO_RA.put(brd, 0b10000);
        PSEUDO_RA.put(brad, 0b11000);
        PSEUDO_RA.put(brld, 0b10100);
        PSEUDO_RA.put(brald, 0b11100);
        PSEUDO_RA.put(bri, 0b00000);
        PSEUDO_RA.put(brai, 0b01000);
        PSEUDO_RA.put(brid, 0b10000);
        PSEUDO_RA.put(braid, 0b11000);
        PSEUDO_RA.put(brlid, 0b10100);
        PSEUDO_RA.put(bralid, 0b11100);
        PSEUDO_RA.put(brk, 0b01100);
        PSEUDO_RA.put(brki, 0b01100);
    }

    private final static Map<MbInstructionName, Integer> EXTENDED_OPCODE;

    static {
        EXTENDED_OPCODE = new EnumMap<>(MbInstructionName.class);

        EXTENDED_OPCODE.put(bsrl, 0b00000000000);
        EXTENDED_OPCODE.put(bsra, 0b01000000000);
        EXTENDED_OPCODE.put(bsll, 0b10000000000);
        EXTENDED_OPCODE.put(bsrli, 0b00000000000);
        EXTENDED_OPCODE.put(bsrai, 0b00000010000);
        EXTENDED_OPCODE.put(bslli, 0b00000100000);
    }

    /**
     * Instructions whose assembly value ends with rB and an extended opcode
     */
    // private static EnumSet<MbInstructionName> TYPE_RB_EXTOP_11 = EnumSet.of(bsrl, bsra, bsll);

    /**
     * Instructions whose assembly value ends with an extended opcode and an imm value
     */
    // private static EnumSet<MbInstructionName> TYPE_EXTOP_11_IMM_5 = EnumSet.of(bsrli, bsrai, bslli);

    private MbInstructionName(int opcode) {
        instructionName = name();
        this.opcode = opcode;
        specialName = false;
    }

    private MbInstructionName(int opcode, String instructionName) {
        this.instructionName = instructionName;
        this.opcode = opcode;
        specialName = true;
    }

    public static MbInstructionName getInstName(String instructionName) {
        MbInstructionName specialCase = specialCases.get(instructionName);
        if (specialCase != null) {
            return specialCase;
        }

        try {
            return valueOf(instructionName.toLowerCase());
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MbInstructionName.class.getName())
                    .warning("Instruction not yet present in the list: '" + instructionName + "'");
            return null;
        }
    }

    /**
     * @param instructionName
     * @return the InstructionType with the same name as the given String, or null if could not find an object with the
     *         same name
     */
    // public static MbInstructionName getEnum(String instructionName) {
    @Override
    public Enum<?> getEnum(String instructionName) {
        return getInstName(instructionName);
    }

    @Override
    public Collection<String> getLoadInstructions() {
        return loadInstructions;
    }

    @Override
    public Collection<String> getStoreInstructions() {
        return storeInstructions;
    }

    /**
     *
     * @return the name of this MicroBlaze instruction
     */
    @Override
    public String getName() {
        return instructionName;
        // return this.name();
    }

    /**
     * 
     * @return the integer value of the opcode
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * 
     * @return a 6-bit binary string representing the opcode
     */
    public String getOpcodeString() {
        return SpecsStrings.padLeft(Integer.toBinaryString(opcode), 6, '0');
    }

    /**
     * Certain instructions (e.g., branches) use the rD bits as part of the op code.
     * 
     * @return
     */
    /*
    public int getPseudoRd() {
    Integer pseudoRd = PSEUDO_RD.get(this);
    if (pseudoRd == null) {
        throw new RuntimeException("Instruction '" + this + "' does not have a pseudo rD");
    }
    
    return pseudoRd.intValue();
    }
    */
    /*
    public int getSuffixRb() {
    	Integer rBSuffix = EXTENDED_OPCODE.get(this);
    	if (rBSuffix == null) {
    	    throw new RuntimeException("Instruction '" + this + "' does not have a rB suffix");
    	}
    
    	return rBSuffix.intValue();
    }
    */
    /**
     * Certain instructions (e.g., unconditional jumps) use the rA bits as part of the op code.
     * 
     * @return
     */
    /*
    public int getPseudoRa() {
    Integer pseudoRa = PSEUDO_RA.get(this);
    if (pseudoRa == null) {
        throw new RuntimeException("Instruction '" + this + "' does not have a pseudo rA");
    }
    
    return pseudoRa.intValue();
    }
    */
}
