/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.binarytranslation.generic;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.InstructionType;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Generic implementation of interface instruction.
 * 
 *
 */
public abstract class AInstruction implements Instruction {

    private Number address;
    private String instruction;

    protected AsmInstructionData fieldData; // raw field data
    protected InstructionData idata; // decoded field data

    protected static IsaParser parser;
    // shared by all insts, so they can go parse themselves

    protected static InstructionSet instSet;
    // shared by all insts, so they can go decode themselves

    public AInstruction(Number address, String instruction) {
        this.address = address;
        this.instruction = instruction.strip();
    }

    @Override
    public String getName() {
        return idata.getPlainName();
    }

    @Override
    public Number getAddress() {
        return address;
    }

    @Override
    public String getInstruction() {
        return instruction;
    }

    @Override
    public int getLatency() {
        return idata.getLatency();
    }

    @Override
    public int getDelay() {
        return idata.getDelay();
    }

    @Override
    public String toString() {
        return SpecsStrings.toHexString(address.longValue(), 8) + ": " + instruction;
    }

    // Check for instruction type /////////////////////////////////////////////
    @Override
    public final boolean isAdd() {
        return idata.getGenericTypes().contains(InstructionType.G_ADD);
    }

    @Override
    public final boolean isSub() {
        return idata.getGenericTypes().contains(InstructionType.G_SUB);
    }

    @Override
    public final boolean isMul() {
        return idata.getGenericTypes().contains(InstructionType.G_MUL);
    }

    @Override
    public final boolean isLogical() {
        return idata.getGenericTypes().contains(InstructionType.G_LOGICAL);
    }

    @Override
    public final boolean isUnary() {
        return idata.getGenericTypes().contains(InstructionType.G_UNARY);
    }

    @Override
    public final boolean isJump() {
        return (idata.getGenericTypes().contains(InstructionType.G_CJUMP) |
                idata.getGenericTypes().contains(InstructionType.G_UJUMP));
    }

    @Override
    public final boolean isConditionalJump() {
        return idata.getGenericTypes().contains(InstructionType.G_CJUMP);
    }

    @Override
    public final boolean isUnconditionalJump() {
        return idata.getGenericTypes().contains(InstructionType.G_UJUMP);
    }

    @Override
    public final boolean isRelativeJump() {
        return idata.getGenericTypes().contains(InstructionType.G_RJUMP);
    }

    @Override
    public final boolean isAbsoluteJump() {
        return idata.getGenericTypes().contains(InstructionType.G_AJUMP);
    }

    @Override
    public final boolean isImmediate() {
        return idata.getGenericTypes().contains(InstructionType.G_IJUMP);
    }

    @Override
    public final boolean isStore() {
        return idata.getGenericTypes().contains(InstructionType.G_STORE);
    }

    @Override
    public final boolean isLoad() {
        return idata.getGenericTypes().contains(InstructionType.G_LOAD);
    }

    @Override
    public final boolean isMemory() {
        return idata.getGenericTypes().contains(InstructionType.G_MEMORY);
    }

    @Override
    public final boolean isFloat() {
        return idata.getGenericTypes().contains(InstructionType.G_FLOAT);
    }

    ///////////////////////////////////////////// Additional non basic types:
    @Override
    public boolean isBackwardsJump() {
        Number branchTarget = this.getBranchTarget();
        if (branchTarget == null)
            return false;

        long val = branchTarget.longValue();
        if (val < this.getAddress().longValue())
            return true;
        else
            return false;
    }

    @Override
    public boolean isForwardsJump() {
        Number branchTarget = this.getBranchTarget();
        if (branchTarget == null)
            return false;

        long val = branchTarget.longValue();
        if (val > this.getAddress().longValue())
            return true;
        else
            return false;
    }

    ///////////////////////////////////////////////////////////////////// Utils
    /*
     * Prints addr:instruction to system output
     */
    @Override
    public void printInstruction() {
        String addr = Long.toHexString(this.getAddress().longValue());
        System.out.print(addr + ":" + this.getInstruction() + ": " + this.getName() + "\t");
        for (GenericInstructionOperand op : this.idata.operands) {
            System.out.print(op.getRepresentation() + ", ");
        }
        System.out.print("\n");
    }

    @Override
    public AsmInstructionData getFields() {
        return this.fieldData;
    }
}
