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

package org.specs.MicroBlaze.legacy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.specs.MicroBlaze.legacy.ArgumentsProperties.ArgumentProperty;
import org.specs.MicroBlaze.legacy.MbOperand.Flow;
import org.specs.MicroBlaze.legacy.MbOperand.Type;

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
        Optional<MbRegister> regId = MbRegisterUtils.getId(argument);

        // If present is a register; otherwise, it is an immediate
        if (regId.isPresent()) {
            return MbOperand.newRegister(regId.get(), argProp.getFlow());
        }

        return MbOperand.newImmediate(argument, argProp.getFlow());
    }

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
    }

    // public static Integer parseValue(Type type, String argument) {
    public static Short parseValue(Type type, String argument) {
        if (type == MbOperand.Type.IMMEDIATE) {
            Short value = null;
            try {
                value = Short.parseShort(argument);
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Expecting a short immediate: '\" + argument + \"'.", ex);
            }
            return value;
        }

        throw new RuntimeException("Case not defined:" + type);
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
    public static final String REGISTER_SEPARATOR = ", ";
}
