/**
 * Copyright 2015 SPeCS.
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

package org.specs.MicroBlaze.encoder;

import org.specs.MicroBlaze.Parsing.MbInstruction;

import com.google.common.base.Preconditions;

public class TypeAEncoder implements AsmEncoder {

    private final String extendedOpcode;

    public TypeAEncoder(String extendedOpcode) {
	this.extendedOpcode = extendedOpcode;
    }

    @Override
    public String getBinaryString(MbInstruction instruction) {
	Preconditions.checkArgument(instruction.getOperands().size() == 3, "Must have 3 operands");

	// 6-bit opcode + rD + rA + rB + extended opcode
	StringBuilder builder = new StringBuilder();

	String opcode = instruction.getInstructionName().getOpcodeString();
	builder.append(opcode);

	instruction.getOperands().stream()
		.forEach(operand -> builder.append(operand.getRegId().get5BitString()));

	builder.append(extendedOpcode);

	String binaryString = builder.toString();
	int numBits = binaryString.length();
	if (numBits != 32) {
	    throw new RuntimeException("Binary string has " + numBits
		    + " bits instead of 32. Instruction:\n" + instruction);
	}

	return binaryString;
    }

}
