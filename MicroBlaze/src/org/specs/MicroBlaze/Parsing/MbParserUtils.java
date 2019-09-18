/*
 * Copyright 2011 SPeCS Research Group.
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

package org.specs.MicroBlaze.Parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.specs.MicroBlaze.ArgumentsProperties.ArgumentProperty;
import org.specs.MicroBlaze.MbInstructionName;
import org.specs.MicroBlaze.MbRegister;
import org.specs.MicroBlaze.MbRegisterUtils;
import org.specs.MicroBlaze.Parsing.MbOperand.Flow;
import org.specs.MicroBlaze.Parsing.MbOperand.Type;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Utility methods related to the String representation of MicroBlaze assembly instructions.
 * 
 * @author Joao Bispo
 */
public class MbParserUtils {

    /**
     * Simple, initial parsing of the arguments of the MicroBlaze instruction string.
     *
     * @param instruction
     * @return
     */
    public static String[] parseArguments(String instruction) {
	int whiteSpaceIndex = SpecsStrings.indexOfFirstWhitespace(instruction);
	String registersString = instruction.substring(whiteSpaceIndex).trim();

	String[] regs = registersString.split(REGISTER_SEPARATOR);
	for (int i = 0; i < regs.length; i++) {
	    regs[i] = regs[i].trim();
	}

	return regs;
    }

    public static MbOperand parseMbArgument(String argument, ArgumentProperty argProp) {

	// MbRegisterId regId = EnumUtils.valueOf(MbRegisterId.class, argument.toUpperCase());

	Optional<MbRegister> regId = MbRegisterUtils.getId(argument);

	// If present is a register; otherwise, it is an immediate
	if (regId.isPresent()) {
	    return MbOperand.newRegister(regId.get(), argProp.getFlow());
	    // return newRegisterOperand(regId.get(), argProp);
	}

	return MbOperand.newImmediate(argument, argProp.getFlow());
	/*
		// Check type
		MbOperand.Type type = getType(regId);
	
		// Check flow
		MbOperand.Flow flow = getFlow(argProp);
	
	
		// Get value
		// Integer value = null;
		Short value = null;
		if (type == MbOperand.Type.register) {
		    // value = regId.getAsmValue();
		    value = regId.getAsmValue().shortValue();
		} else {
		    value = parseValue(type, argument);
		    if (value == null) {
			System.out.println("Argument:" + argument + ", Prop:" + argProp);
		    }
		}
	
		// Integer value = parseValue(type, argument);
		if (value == null) {
		    return null;
		}
	
		String id = null;
		if (type == MbOperand.Type.immediate) {
		    // id = MbRegisterId.getConstantName();
		    id = MbRegisterUtils.getConstantName();
		}
	
		if (type == MbOperand.Type.register) {
		    // id = MbRegisterId.values()[value].getName();
		    id = regId.getName();
		}
	
		return new MbOperand(flow, type, value, id, regId);
		*/
    }

    /*
    private static MbOperand newImmediateOperand(String argument, ArgumentProperty argProp) {
    // Check type
    MbOperand.Type type = MbOperand.Type.immediate;
    
    // Check flow
    MbOperand.Flow flow = argProp.getFlow();
    
    // Get value
    
    Short value = parseValue(type, argument);
    if (value == null) {
        throw new RuntimeException("Could not extract short value. Argument:" + argument + ", Prop:" + argProp);
    }
    
    String id = MbRegisterUtils.getConstantName();
    
    return new MbOperand(flow, type, value, id, null);
    }
    */

    /**
     * TODO: Move to inside of MbOperand as static constructor
     * 
     * @param mbRegister
     * @param argProp
     * @return
     */
    /*
    private static MbOperand newRegisterOperand(MbRegister mbRegister, ArgumentProperty argProp) {
    // Type
    MbOperand.Type type = MbOperand.Type.register;
    
    // Check flow
    MbOperand.Flow flow = argProp.getFlow();
    
    // Get value
    Short value = mbRegister.getAsmValue().shortValue();
    
    // Get id
    String id = mbRegister.getName();
    
    return new MbOperand(flow, type, value, id, mbRegister);
    }
    */

    /*
    private static MbOperand.Type getType(Optional<MbRegister> regId) {
    if (regId.isPresent()) {
        return MbOperand.Type.register;
    }
    
    return MbOperand.Type.immediate;
    }
    */

    /**
     * Extracts the flow of the given ArgumentProperty. If it could not be determined throws an exception.
     * 
     * @param argProp
     * @return
     */
    public static Flow getFlow(ArgumentProperty argProp) {
	if (argProp == ArgumentProperty.read) {
	    return MbOperand.Flow.READ;
	}

	if (argProp == ArgumentProperty.write) {
	    return MbOperand.Flow.WRITE;
	}

	throw new RuntimeException("Case not defined: '" + argProp + "'");
	// LoggingUtils.getLogger().warning("Case not defined: '" + argProp + "'");
	// return null;

    }

    // public static Integer parseValue(Type type, String argument) {
    public static Short parseValue(Type type, String argument) {
	if (type == MbOperand.Type.IMMEDIATE) {
	    Short value = null;
	    try {
		value = Short.parseShort(argument);
	    } catch (NumberFormatException ex) {
		throw new RuntimeException("Expecting a short immediate: '\" + argument + \"'.", ex);
		// LoggingUtils.getLogger().
		// warning("Expecting an integer immediate: '" + argument + "'.");
		// warning("Expecting a short immediate: '" + argument + "'.");
	    }
	    return value;
	}

	throw new RuntimeException("Case not defined:" + type);
	// LoggingUtils.getLogger().warning("Case not defined:" + type);
	// return null;
    }

    public static String getHexAddress(int address, int padLength) {
	String hexString = Integer.toHexString(address).toUpperCase();
	// Pad String
	return "0x" + SpecsStrings.padLeft(hexString, padLength, '0');
    }

    public static List<MbInstruction> getMbInstructions(List<Integer> addresses,
	    List<String> instructions) {

	int numInstructions = instructions.size();

	List<MbInstruction> mbInstructions = new ArrayList<>();
	for (int i = 0; i < numInstructions; i++) {
	    int address = addresses.get(i);
	    String instruction = instructions.get(i);

	    mbInstructions.add(MbInstruction.create(address, instruction));
	}

	return mbInstructions;
    }

    public static MbInstructionName getMbInstructionName(String instruction) {
	int separatorIndex = instruction.indexOf(" ");
	String instructionNameString = instruction.substring(0, separatorIndex);
	MbInstructionName instructionName = (MbInstructionName) MbInstructionName.values()[0]
		.getEnum(instructionNameString);
	if (instructionName == null) {
	    SpecsLogs.getLogger().warning("Could not parse instruction name for instruction '" + instruction + "'");
	    return null;
	}

	return instructionName;
    }

    public static final String REGISTER_PREFIX = "r";
    // public static final String REGISTER_PREFIX = "R";
    public static final String REGISTER_SEPARATOR = ", ";
}
