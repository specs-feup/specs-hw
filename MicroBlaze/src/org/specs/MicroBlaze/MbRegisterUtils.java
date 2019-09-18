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

package org.specs.MicroBlaze;

import java.util.Optional;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.asm.processor.RegisterUtils;

/**
 *
 * @author Joao Bispo
 */
public class MbRegisterUtils {

    /**
     *
     * @param registerId
     * @return true if is a general-purpose register
     */
    public static boolean isGpr(MbRegister registerId) {
	String name = registerId.name();
	if (!Character.isDigit(name.charAt(1))) {
	    return false;
	}

	return true;
    }

    public static Integer getGprNumber(MbRegister registerId) {
	String name = registerId.name();
	String regNumberString = name.substring(1);
	Integer regNumber = SpecsStrings.parseInteger(regNumberString);
	if (regNumber == null) {
	    SpecsLogs.getLogger().warning("Could not parse register '" + name + "'. Expecting R<number> format.");
	    return null;
	}

	return regNumber;
    }

    /**
     *
     * @param registerId
     * @return true if is a Processor Version register
     */
    public static boolean isPvr(MbRegister registerId) {
	// String name = registerId.getName();
	String name = registerId.name();
	if (name.length() < 5) {
	    return false;
	}

	if (!name.substring(1, 4).equals(MbRegisterUtils.PVR)) {
	    return false;
	}

	return true;
    }

    public static Integer getPvrNumber(MbRegister registerId) {
	// String name = registerId.getName();
	String name = registerId.name();
	String regNumberString = name.substring(4);
	Integer regNumber = SpecsStrings.parseInteger(regNumberString);
	if (regNumber == null) {
	    SpecsLogs.getLogger().warning("Could not parse register '" + name + "'. Expecting RPVR<number> format.");
	    return null;
	}

	if (regNumber > 11 || regNumber < 0) {
	    SpecsLogs.getLogger().warning("Expecting a number between 0 and 11: '" + name + "'");
	    return null;
	}

	return regNumber;
    }

    public static String getCarryFlagName() {
	return RegisterUtils.buildRegisterBit(MbRegister.RMSR, 29);
    }

    public static String getConstantName() {
	return CONSTANT_NAME;
    }

    /**
     * Converts a string to the corresponding MicroBlaze Register. Operation is case-insensitive.
     * 
     * @param registerName
     * @return
     */
    public static Optional<MbRegister> getId(String registerName) {
	MbRegister regId = SpecsEnums.valueOf(MbRegister.class, registerName.toUpperCase());
	return Optional.ofNullable(regId);
    }

    /**
     * 
     * @param regId
     *            the id of the general register, from 0 to 31
     * @return
     */
    public static String getGeneralRegName(int regId) {
	if (regId < 0 || regId > 31) {
	    throw new RuntimeException("MicroBlaze only has 32 general registers, id '" + regId + "' does not exist.");
	}

	return "r" + regId;
    }

    public static final String CONSTANT_NAME = "CONST";

    public static final String PVR = "PVR";
}
