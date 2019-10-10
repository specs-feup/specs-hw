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
import pt.up.fe.specs.binarytranslation.InstructionData;
import pt.up.fe.specs.binarytranslation.InstructionOperand;
import pt.up.fe.specs.binarytranslation.InstructionSet;
import pt.up.fe.specs.binarytranslation.InstructionType;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Generic implementation of interface instruction.
 * 
 * @author NunoPaulino
 * 
 */
public abstract class AInstruction implements Instruction {

    private Number address;
    private String instruction;

    // decoded field data (abstract class)
    private final InstructionData idata;

    // shared by all instructions, so they can go decode themselves
    protected static InstructionSet instSet;

    public AInstruction(Number address, String instruction, InstructionData idata) {
        this.address = address;
        this.instruction = instruction.strip();
        this.idata = idata;
    }

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public InstructionData getData() {
        return idata;
    }

    public final String getName() {
        return idata.getPlainName();
    }

    public final Number getAddress() {
        return address;
    }

    public final String getInstruction() {
        return instruction;
    }

    public final int getLatency() {
        return idata.getLatency();
    }

    public final int getDelay() {
        return idata.getDelay();
    }

    public String toString() {
        return SpecsStrings.toHexString(address.longValue(), 8) + ": " + instruction;
    }

    // Check for instruction type /////////////////////////////////////////////
    public final boolean isAdd() {
        return idata.getGenericTypes().contains(InstructionType.G_ADD);
    }

    public final boolean isSub() {
        return idata.getGenericTypes().contains(InstructionType.G_SUB);
    }

    public final boolean isMul() {
        return idata.getGenericTypes().contains(InstructionType.G_MUL);
    }

    public final boolean isLogical() {
        return idata.getGenericTypes().contains(InstructionType.G_LOGICAL);
    }

    public final boolean isUnary() {
        return idata.getGenericTypes().contains(InstructionType.G_UNARY);
    }

    public final boolean isJump() {
        return (idata.getGenericTypes().contains(InstructionType.G_CJUMP) |
                idata.getGenericTypes().contains(InstructionType.G_UJUMP));
    }

    public final boolean isConditionalJump() {
        return idata.getGenericTypes().contains(InstructionType.G_CJUMP);
    }

    public final boolean isUnconditionalJump() {
        return idata.getGenericTypes().contains(InstructionType.G_UJUMP);
    }

    public final boolean isRelativeJump() {
        return idata.getGenericTypes().contains(InstructionType.G_RJUMP);
    }

    public final boolean isAbsoluteJump() {
        return idata.getGenericTypes().contains(InstructionType.G_AJUMP);
    }

    public final boolean isImmediate() {
        return idata.getGenericTypes().contains(InstructionType.G_IJUMP);
    }

    public final boolean isStore() {
        return idata.getGenericTypes().contains(InstructionType.G_STORE);
    }

    public final boolean isLoad() {
        return idata.getGenericTypes().contains(InstructionType.G_LOAD);
    }

    public final boolean isMemory() {
        return idata.getGenericTypes().contains(InstructionType.G_MEMORY);
    }

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
        int i = 0;
        for (InstructionOperand op : this.idata.getOperands()) {
            System.out.print(op.getRepresentation());
            if (i++ < this.idata.getOperands().size() - 1)
                System.out.print(", ");
        }
        System.out.print("\n");
    }
}
