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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Utility methods related to MicroBlaze instructions.
 *
 * @author Joao Bispo
 */
public class MbUtils {

    /**
     * 
     * @param mbInstructions
     * @return true if the list of MbInstructions has any store instruction. False otherwise
     */

    public static boolean hasStores(List<MbInstruction> mbInstructions) {
        for (MbInstruction inst : mbInstructions) {
            if (OperationProperties.isStore(inst.getInstructionName())) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasLoads(List<MbInstruction> mbInstructions) {
        for (MbInstruction inst : mbInstructions) {
            if (OperationProperties.isLoad(inst.getInstructionName())) {
                return true;
            }
        }

        return false;
    }

    public static List<String> getOutputIds(List<MbOperand> operands) {
        List<String> outputRegs = new ArrayList<>();

        for (MbOperand operand : getOperands(operands, MbOperand.Flow.WRITE)) {
            outputRegs.add(operand.getId());
        }

        return outputRegs;
    }

    public static List<MbOperand> getOperands(List<MbOperand> operands, MbOperand.Flow flow) {
        List<MbOperand> returnOperands = new ArrayList<>();
        for (MbOperand operand : operands) {
            if (operand.getFlow() != flow) {
                continue;
            }

            returnOperands.add(operand);
        }

        return returnOperands;
    }

    /**
     *
     * @param instruction
     * @return true if it is a MicroBlaze nop
     */
    public static boolean isNop(MbInstruction mbInst) {
        if (mbInst.getInstructionName() != MbInstructionName.or) {
            return false;
        }

        for (int i = 0; i < mbInst.getOperands().size(); i++) {
            MbOperand operand = mbInst.getOperands().get(i);

            // If operand is not R0, is not a nop
            if (!operand.isR0()) {
                return false;
            }
        }

        // Every operand is a register and 0, and operation is OR
        // It is a nop
        return true;
    }

    // TODO: Move this to its own class
    public static final Map<MbInstructionName, String> instructionOp;

    static {
        instructionOp = new EnumMap<>(MbInstructionName.class);

        instructionOp.put(MbInstructionName.add, "+");
        instructionOp.put(MbInstructionName.addc, "+");
        instructionOp.put(MbInstructionName.addi, "+");
        instructionOp.put(MbInstructionName.addic, "+");
        instructionOp.put(MbInstructionName.addik, "+");
        instructionOp.put(MbInstructionName.addikc, "+");
        instructionOp.put(MbInstructionName.addk, "+");
        instructionOp.put(MbInstructionName.addkc, "+");

        instructionOp.put(MbInstructionName.and, "&");
        instructionOp.put(MbInstructionName.andi, "&");
        instructionOp.put(MbInstructionName.andn, "!&");
        instructionOp.put(MbInstructionName.andni, "!&");

        instructionOp.put(MbInstructionName.bsll, "<<");
        instructionOp.put(MbInstructionName.bslli, "<<");
        instructionOp.put(MbInstructionName.bsra, ">>");
        instructionOp.put(MbInstructionName.bsrai, ">>");
        instructionOp.put(MbInstructionName.bsrl, ">>>");
        instructionOp.put(MbInstructionName.bsrli, ">>>");

        instructionOp.put(MbInstructionName.fadd, ".+");
        instructionOp.put(MbInstructionName.fdiv, "./");
        instructionOp.put(MbInstructionName.fmul, ".*");
        instructionOp.put(MbInstructionName.frsub, ".-");

        instructionOp.put(MbInstructionName.idiv, "/");
        instructionOp.put(MbInstructionName.idivu, "/");

        instructionOp.put(MbInstructionName.mul, "*");
        instructionOp.put(MbInstructionName.mulh, "*");
        instructionOp.put(MbInstructionName.mulhsu, "*");
        instructionOp.put(MbInstructionName.mulhu, "*");
        instructionOp.put(MbInstructionName.muli, "*");

        instructionOp.put(MbInstructionName.or, "|");
        instructionOp.put(MbInstructionName.ori, "|");

        instructionOp.put(MbInstructionName.rsub, "-");
        instructionOp.put(MbInstructionName.rsubc, "-");
        instructionOp.put(MbInstructionName.rsubi, "-");
        instructionOp.put(MbInstructionName.rsubic, "-");
        instructionOp.put(MbInstructionName.rsubik, "-");
        instructionOp.put(MbInstructionName.rsubikc, "-");
        instructionOp.put(MbInstructionName.rsubk, "-");
        instructionOp.put(MbInstructionName.rsubkc, "-");

        // instructionOp.put(MbInstructionName.sra, ">>");
        // instructionOp.put(MbInstructionName.src, ">>");
        // instructionOp.put(MbInstructionName.srl, ">>");

        instructionOp.put(MbInstructionName.xor, "^");
        instructionOp.put(MbInstructionName.xori, "^");
    }
}
