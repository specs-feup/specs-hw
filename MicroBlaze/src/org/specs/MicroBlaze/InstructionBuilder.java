/**
 * Copyright 2013 SPeCS Research Group.
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

package org.specs.MicroBlaze;

import org.specs.MicroBlaze.legacy.MbInstructionName;

import pt.up.fe.specs.util.SpecsBits;

/**
 * @author Joao Bispo
 * 
 */
public class InstructionBuilder {

    private final static int PROCESSOR_BITS = 32;
    private final static int OPCODE_BITS = 6;

    private final static int FLAG_A_BIT = 19;

    /**
     * Returns the value of the opcode, shifted so it is as it should appear in a MicroBlaze instruction.
     * 
     * @param args
     */
    public static int getOpcodeShifted(MbInstructionName instruction) {
	// Get opcode
	// int opcode = OpCode.getOpcode(instruction);
	int opcode = instruction.getOpcode();

	// Shift opcode
	opcode = opcode << (PROCESSOR_BITS - OPCODE_BITS);

	// Return
	return opcode;
    }

    /**
     * Returns the flag 'A' set (used in instructions such as the BRAI instruction).
     * 
     * @return
     */
    public static int getFlagA() {
	int flagA = 0;

	flagA = SpecsBits.setBit(FLAG_A_BIT, flagA);

	return flagA;
    }

    public static int newImm(int immValue) {
	int opCode = getOpcodeShifted(MbInstructionName.imm);

	int instruction = opCode | (short) immValue;

	return instruction;
    }

    /**
     * @return
     */
    /*
    public static int newBrai(int immValue) {
    int opCode = getOpcodeShifted(MbInstructionName.brai);
    
    int flagA = getFlagA();
    
    int instruction = opCode | flagA | (short) immValue;
    
    return instruction;
    }
    */
    public static int newBrai(int immValue) {
	return newBriGeneral(immValue, true);
    }

    public static int newBri(int immValue) {
	return newBriGeneral(immValue, false);
    }

    /**
     * @param lower16
     * @return
     */
    private static int newBriGeneral(int immValue, boolean useFlagA) {
	int opCode = getOpcodeShifted(MbInstructionName.brai);

	int flagA = 0;
	if (useFlagA) {
	    flagA = getFlagA();
	}

	int instruction = opCode | flagA | (short) immValue;

	return instruction;

    }

}
