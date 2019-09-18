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

/**
 * Methods containg properties of MicroBlaze instructions.
 *
 * @author Joao Bispo
 */
public class OperationProperties {

    public static boolean isAdd(MbInstructionName mbInstructionName) {
	return InstructionProperties.ADD_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isRsub(MbInstructionName mbInstructionName) {
	return InstructionProperties.RSUB_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isBinaryLogical(MbInstructionName mbInstructionName) {
	return InstructionProperties.BINARY_LOGICAL_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isUnaryLogical(MbInstructionName mbInstructionName) {
	return InstructionProperties.UNARY_LOGICAL_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isArithmeticOperation(MbInstructionName mbInstructionName) {
	return isAdd(mbInstructionName) | isRsub(mbInstructionName) |
		isBinaryLogical(mbInstructionName) | isUnaryLogical(mbInstructionName);
    }

    public static boolean isJump(MbInstructionName mbInstructionName) {
	return InstructionProperties.JUMP_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isLoad(MbInstructionName mbInstructionName) {
	return InstructionProperties.LOAD_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isStore(MbInstructionName mbInstructionName) {
	return InstructionProperties.STORE_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isMemoryOperation(MbInstructionName mbInstructionName) {
	return isLoad(mbInstructionName) | isStore(mbInstructionName);
    }

    public static boolean isUnconditionalJump(MbInstructionName mbInstructionName) {
	return InstructionProperties.UNCONDITIONAL_BRANCHES.contains(mbInstructionName);
    }

    public static boolean isUnconditionalJumpImm(MbInstructionName mbInstructionName) {
	return InstructionProperties.UNCONDITIONAL_JUMP_IMM.contains(mbInstructionName);
    }

    public static boolean isUnconditionalJumpReg(MbInstructionName mbInstructionName) {
	return InstructionProperties.UNCONDITIONAL_JUMP_REG.contains(mbInstructionName);
    }

    public static boolean isConditionalJump(MbInstructionName mbInstructionName) {
	return InstructionProperties.CONDITIONAL_BRANCHES.contains(mbInstructionName);
    }

    public static boolean isFloatArith(MbInstructionName mbInstructionName) {
	return InstructionProperties.FLOAT_ARITH.contains(mbInstructionName);
    }

    public static boolean isFloatComp(MbInstructionName mbInstructionName) {
	return InstructionProperties.FLOAT_COMP.contains(mbInstructionName);
    }

    public static boolean isConversion(MbInstructionName mbInstructionName) {
	return InstructionProperties.CONVERSION.contains(mbInstructionName);
    }

    /**
     *
     * @param mbInstructionName
     * @return true if instruction represents a conditional branches with immediate offset
     */
    public static boolean isConditionalBranchImm(MbInstructionName mbInstructionName) {
	return InstructionProperties.CONDITIONAL_BRANCHES_IMM.contains(mbInstructionName);
    }

    public static boolean isBranchAndLink(MbInstructionName mbInstructionName) {
	return InstructionProperties.BRANCH_AND_LINK.contains(mbInstructionName);
    }

    public static boolean isReturn(MbInstructionName mbInstructionName) {
	return InstructionProperties.RETURN.contains(mbInstructionName);
    }

    public static boolean isShift(MbInstructionName mbInstructionName) {
	return InstructionProperties.SHIFT_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isShiftSingle(MbInstructionName mbInstructionName) {
	return InstructionProperties.SHIFT_SINGLE_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isShiftBarrel(MbInstructionName mbInstructionName) {
	return InstructionProperties.SHIFT_BARREL_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isComparator(MbInstructionName mbInstructionName) {
	return InstructionProperties.COMPARATOR_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isComparatorPattern(MbInstructionName mbInstructionName) {
	return InstructionProperties.COMPARATOR_PATTERN_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isComparatorInteger(MbInstructionName mbInstructionName) {
	return InstructionProperties.COMPARATOR_INTEGER_INSTRUCTIONS.contains(mbInstructionName);
    }

    /**
     * Which instructions strictly represent boolean operations.
     *
     * <p>
     * Ex.: and, or, xor
     */
    public static boolean isBoolean(MbInstructionName mbInstructionName) {
	return InstructionProperties.BOOLEAN_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isSignExtension(MbInstructionName mbInstructionName) {
	return InstructionProperties.SIGN_EXTENSION_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isMultiplication(MbInstructionName mbInstructionName) {
	return InstructionProperties.MULTIPLICATION_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isMultiplicationSigned(MbInstructionName mbInstructionName) {
	return InstructionProperties.MULTIPLICATION_SIGNED_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isMultiplicationUnsigned(MbInstructionName mbInstructionName) {
	return InstructionProperties.MULTIPLICATION_UNSIGNED_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isDivision(MbInstructionName mbInstructionName) {
	return InstructionProperties.DIVISION_INSTRUCTIONS.contains(mbInstructionName);
    }

    public static boolean isFloatingPoint(MbInstructionName mbInstructionName) {
	return InstructionProperties.FLOATING_POINT.contains(mbInstructionName);
    }

    public static boolean isTypeB(MbInstructionName mbInstructionName) {
	return InstructionProperties.TYPE_B.contains(mbInstructionName);
    }

    /**
     * Currently considering as instructions with side effects:
     *
     * <p>
     * - All store instructions;
     * 
     * @param mbInstructionName
     * @return
     */
    public static boolean hasSideEffects(MbInstructionName mbInstructionName) {
	if (isStore(mbInstructionName)) {
	    return true;
	}

	return false;
	// return InstructionProperties.INSTRUCTIONS_WITH_SIDE_EFFECTS.contains(mbInstructionName);
    }

    /**
     *
     * @param instructionName
     * @return the number of delay slots (cycles) of the given instruction
     */
    public static int getDelaySlots(MbInstructionName instructionName) {
	boolean hasDelaySlot = InstructionProperties.INSTRUCTIONS_WITH_DELAY_SLOT.contains(instructionName);
	if (hasDelaySlot) {
	    return 1;
	}

	return 0;
    }

}
