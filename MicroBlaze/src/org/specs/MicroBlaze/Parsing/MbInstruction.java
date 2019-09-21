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

package org.specs.MicroBlaze.Parsing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.specs.MicroBlaze.ArgumentsProperties;
import org.specs.MicroBlaze.ArgumentsProperties.ArgumentProperty;
import org.specs.MicroBlaze.OperationProperties;
import org.specs.MicroBlaze.isa.MbInstructionName;

import pt.up.fe.specs.binarytranslation.legacy.TraceInstruction32;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Represents a Microblaze instruction.
 * 
 * @author Joao Bispo
 */
public class MbInstruction implements TraceInstruction32 {

    /**
     * INSTANCE VARIABLES
     */
    private final int address;
    private final MbInstructionName instructionName;
    private final List<MbOperand> operands;
    private final Integer cycles;
    private final Integer encodedInst;

    private MbInstruction(int address, MbInstructionName instructionName, List<MbOperand> operands,
            Integer encodedInst) {
        this(address, instructionName, null, operands, encodedInst);
    }

    private MbInstruction(int address, MbInstructionName instructionName, Integer cycles, List<MbOperand> operands,
            Integer encodedInst) {
        this.address = address;
        this.instructionName = instructionName;
        this.cycles = cycles;
        this.operands = Collections.unmodifiableList(operands);
        this.encodedInst = encodedInst;
    }

    /**
     * Makes a clone of this object.
     * 
     * @return
     */
    @Override
    public MbInstruction clone() {
        List<MbOperand> newOperandList = new ArrayList<>(operands);
        return new MbInstruction(address, instructionName, newOperandList, encodedInst);
    }

    @Override
    public int getAddress32() {
        return address;
    }

    @Override
    public String getInstruction() {
        StringBuilder builder = new StringBuilder();

        builder.append(instructionName);
        if (!operands.isEmpty()) {
            builder.append(" ");
            builder.append(operands.get(0).getAsm());
        }
        for (int i = 1; i < operands.size(); i++) {
            MbOperand operand = operands.get(i);
            builder.append(MbParserUtils.REGISTER_SEPARATOR);
            builder.append(operand.getAsm());
        }

        return builder.toString();
    }

    @Override
    public Integer getCycles() {
        return cycles;
    }

    @Override
    public Optional<Integer> getEncoded() {
        return Optional.ofNullable(encodedInst);
    }

    public MbInstructionName getInstructionName() {
        return instructionName;
    }

    public List<MbOperand> getOperands() {
        return operands;
    }

    /**
     * Returns a list with the operands which respects the given MbOperand flow and type. If given a null as a value, it
     * is interpreted as a wildcard.
     * 
     * @param flow
     * @param type
     * @return a list of operands which respect the given MbOperand flow and type.
     */
    public List<MbOperand> getOperands(MbOperand.Flow flow, MbOperand.Type type) {
        List<MbOperand> filteredOps = new ArrayList<>();

        for (MbOperand op : operands) {
            if (flow != null) {
                if (flow != op.getFlow()) {
                    continue;
                }
            }

            if (type != null) {
                if (type != op.getType()) {
                    continue;
                }
            }

            filteredOps.add(op);
        }
        return filteredOps;
    }

    /**
     * 
     * @param instruction
     * @return true if it is a MicroBlaze nop
     */
    public boolean isMbNop() {
        if (getInstructionName() != MbInstructionName.or) {
            return false;
        }

        for (int i = 0; i < getOperands().size(); i++) {
            MbOperand operand = getOperands().get(i);

            // If operand is not R0, is not a nop
            if (!operand.isR0()) {
                return false;
            }
        }

        // Every operand is a register and 0, and operation is OR
        // It is a nop
        return true;
    }

    public boolean hasSideEffects() {
        return OperationProperties.hasSideEffects(instructionName);
    }

    public String getAsm() {
        StringBuilder builder = new StringBuilder();

        builder.append(SpecsStrings.toHexString(address, 8));
        builder.append(" ");
        builder.append(getInstruction());

        if (cycles != null) {
            builder.append(" (").append(cycles).append(" cycles)");
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return getAsm();
    }

    public static MbInstruction create(TraceInstruction32 traceInstruction) {
        return create(traceInstruction.getAddress32(), traceInstruction.getInstruction(), traceInstruction.getCycles(),
                traceInstruction.getEncoded().orElse(null));
    }

    public static MbInstruction create(int address, String instruction) {
        return create(address, instruction, null);
    }

    public static MbInstruction create(int address, String instruction, Integer cycle) {
        return create(address, instruction, cycle, null);
    }

    public static MbInstruction create(int address, String instruction, Integer cycle, Integer instInt) {
        // Parse arguments
        String[] arguments = MbParserUtils.parseArguments(instruction);

        MbInstructionName instructionName = MbParserUtils.getMbInstructionName(instruction);

        ArgumentProperty[] argProps = ArgumentsProperties.getProperties(instructionName);

        // Check arguments properties have the same size as the arguments
        if (arguments.length != argProps.length) {
            SpecsLogs.getLogger().warning(
                    "Number of arguments (" + arguments.length + ") different from " + "the number of properties ("
                            + argProps.length + ") for instruction '" + instructionName + "'.");
            return null;
        }

        // For each argument, return the correct operand
        List<MbOperand> operands = new ArrayList<>();
        for (int i = 0; i < arguments.length; i++) {
            MbOperand mbOperand = MbParserUtils.parseMbArgument(arguments[i], argProps[i]);
            operands.add(mbOperand);
        }

        return new MbInstruction(address, instructionName, cycle, operands, instInt);
    }

    @Override
    public Boolean isJump() {
        return OperationProperties.isJump(instructionName);
    }

    @Override
    public boolean isBackwardsBranch() {
        // TODO Auto-generated method stub
        return false;
    }
}
