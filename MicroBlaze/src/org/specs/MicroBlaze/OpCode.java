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

import java.util.Map;

import pt.up.fe.specs.util.SpecsFactory;

/**
 * @author Joao Bispo
 * @deprecated Replace with MbInstructionName.getOpcode()
 */
@Deprecated
public class OpCode {

    private final static Map<MbInstructionName, Integer> OP_CODES = SpecsFactory.newEnumMap(MbInstructionName.class);

    static {
	OP_CODES.put(MbInstructionName.brai, Integer.parseInt("101110", 2));
	OP_CODES.put(MbInstructionName.imm, Integer.parseInt("101100", 2));
    }

    /**
     * Returns the value of the opcode of the given instruction.
     * 
     * @param args
     */
    public static int getOpcode(MbInstructionName instruction) {
	// Get opcode
	Integer opcode = OP_CODES.get(instruction);

	// Check if opcode is defined
	if (opcode == null) {
	    throw new RuntimeException("Instruction not implemented: " + instruction);
	}

	// Return opcode
	return opcode;
    }

}
