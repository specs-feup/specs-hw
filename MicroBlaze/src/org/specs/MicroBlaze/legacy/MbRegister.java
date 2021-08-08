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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.asm.processor.RegisterId;

/**
 * Mapping between Fireworks 3 Stage Pipelined processor registers and the register number used in the DTool simulator.
 * 
 * <p>
 * In the descriptions, '0' is the Most-Significant Bit.
 * 
 * @author Joao Bispo
 */
public enum MbRegister implements RegisterId {

    R0(0),
    R1(1),
    R2(2),
    R3(3),
    R4(4),
    R5(5),
    R6(6),
    R7(7),
    R8(8),
    R9(9),
    R10(10),
    R11(11),
    R12(12),
    R13(13),
    R14(14),
    R15(15),
    R16(16),
    R17(17),
    R18(18),
    R19(19),
    R20(20),
    R21(21),
    R22(22),
    R23(23),
    R24(24),
    R25(25),
    R26(26),
    R27(27),
    R28(28),
    R29(29),
    R30(30),
    R31(31),
    R32(32), // Special register, for experiments in simulator
    RPC(Integer.parseInt("0000", 16)),
    RMSR(Integer.parseInt("0001", 16)),
    REAR(Integer.parseInt("0003", 16)),
    /**
     * The Exception Status Register contains status bits for the processor. When read with the MFS instruction, the ESR
     * is specified by setting Sa = 0x0005.
     * 
     * <p>
     * 0:18 Reserved <br>
     * 19 DS Delay Slot Exception <br>
     * 20:26 ESS Exception Specific Status <br>
     * 27:31 EC Exception Cause <br>
     */
    RESR(Integer.parseInt("0005", 16)),
    /**
     * The bits in this register are sticky − floating point instructions can only set bits in the register, and the
     * only way to clear the register is by using the MTS instruction.
     * 
     * <p>
     * Bits: <br>
     * 0:26 Reserved <br>
     * 27 IO Invalid operation <br>
     * 28 DZ Divide-by-zero <br>
     * 29 OF Overflow <br>
     * 30 UF Underflow <br>
     * 31 DO Denormalized operand error <br>
     */
    RFSR(Integer.parseInt("0007", 16)),
    RBTR(Integer.parseInt("000b", 16)),
    RPVR0(Integer.parseInt("2000", 16)),
    RPVR1(Integer.parseInt("2001", 16)),
    RPVR2(Integer.parseInt("2002", 16)),
    RPVR3(Integer.parseInt("2003", 16)),
    RPVR4(Integer.parseInt("2004", 16)),
    RPVR5(Integer.parseInt("2005", 16)),
    RPVR6(Integer.parseInt("2006", 16)),
    RPVR7(Integer.parseInt("2007", 16)),
    RPVR8(Integer.parseInt("2008", 16)),
    RPVR9(Integer.parseInt("2009", 16)),
    RPVR10(Integer.parseInt("200a", 16)),
    RPVR11(Integer.parseInt("200b", 16)),
    REDR(Integer.parseInt("000d", 16)),
    RSLR(Integer.parseInt("0800", 16)),
    RSHR(Integer.parseInt("0802", 16)),
    RPID(Integer.parseInt("1000", 16)),
    RZPR(Integer.parseInt("1001", 16)),
    RTLBX(Integer.parseInt("1002", 16)),
    RTLBLO(Integer.parseInt("1003", 16)),
    RTLBHI(Integer.parseInt("1004", 16)),
    RTLBSX(Integer.parseInt("1005", 16)),

    IMM(Integer.parseInt("10000", 16)); // Special register, for representing MicroBlaze IMM

    /*
        public int getPvrRegEnd() {
    	return RPVR11.asmValue;
        }
    */
    private final Integer asmValue;

    private static final Map<String, MbRegister> nameMap;

    static {
        nameMap = new HashMap<>();
        for (MbRegister regId : MbRegister.values()) {
            MbRegister.nameMap.put(regId.getName(), regId);
        }
    }

    private static final Set<MbRegister> REGS_WITH_ADDRESS = EnumSet.range(RPC, RTLBSX);
    private static final Map<Integer, MbRegister> ADDRESS_TO_REG;

    static {
        ADDRESS_TO_REG = SpecsFactory.newHashMap();

        for (MbRegister reg : MbRegister.REGS_WITH_ADDRESS) {
            MbRegister.ADDRESS_TO_REG.put(reg.asmValue, reg);
        }
    }

    /**
     * 
     * @param asmValue
     */
    private MbRegister(Integer asmValue) {
        this.asmValue = asmValue;
    }

    /**
     * TODO: move to FwProcessor related class
     * 
     * @return
     */
    // @Override
    public Integer getAsmValue() {
        return asmValue;
    }

    /**
     * 
     * @return the number of the register represented as a 5-bit binary string
     */
    public String get5BitString() {
        String binaryString = Integer.toBinaryString(getAsmValue());
        if (binaryString.length() > 5) {
            throw new RuntimeException("Register has a number with more than 5 bits (" + getAsmValue() + ")");
        }

        return SpecsStrings.padLeft(binaryString, 5, '0');
    }

    public static MbRegister getRegFromAddr(int addr) {
        MbRegister reg = MbRegister.ADDRESS_TO_REG.get(addr);
        if (reg == null) {
            SpecsLogs.warn("There is not register mapped to address " + SpecsStrings.toHexString(addr, 8));
        }

        return reg;
    }

    /**
     * Can be used as ID.
     * 
     */
    @Override
    public String getName() {
        /*
              if(MbRegisterUtils.isGpr(this)) {
                 return "REG"+asmValue;
              }
        
              if(this == RMSR) {
                 return "MSR";
              }
          */
        return name().toLowerCase();
    }

    public String getAsmRepresentation() {
        return name().toLowerCase();
    }

    /*
    public static String getCarryFlagName() {
       //return RMSR.name() + "[29]";
       return RegisterUtils.buildRegisterBit(RMSR, 29);
    }
     *
     */

    /*
    public static String getImmName() {
       return IMMEDIATE_NAME;
    }
     *
     */

    public static MbRegister getRegId(String name) {
        MbRegister regId = MbRegister.nameMap.get(name);
        if (regId == null) {
            SpecsLogs.getLogger().warning("Could not find register with name '" + name + "'.");
        }
        return regId;
    }

}
