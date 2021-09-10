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

package pt.up.fe.specs.binarytranslation.instruction;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.analysis.graphs.pseudocode.PseudoInstructionGraph;
import pt.up.fe.specs.binarytranslation.asm.parsing.IsaParser;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

/**
 * Generic implementation of interface instruction.
 * 
 * @author NunoPaulino
 * 
 */
public abstract class AInstruction implements Instruction {

    @Expose
    private final Long address;

    @Expose
    private final String instruction;

    // decoded field data (abstract class)
    private final AInstructionData idata;

    // shared by all instructions, so they can go decode themselves
    protected static InstructionSet instSet;
    protected static IsaParser parser;

    // the enum which represents the ISA properties of this instruction
    // protected final InstructionProperties props;

    /*
     * Main constructor
     */
    protected AInstruction(Long address, String instruction, AInstructionData idata) {
        this.address = address;
        this.instruction = instruction.strip();
        this.idata = idata;
    }

    /*
     * Copy constructor
     */
    protected AInstruction(AInstruction other) {
        this.address = other.getAddress();
        this.instruction = new String(other.getInstruction());
        this.idata = other.getData().copy();
        // this.props = other.getProperties(); // is an enum (i.e. final constant)
    }

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public AInstructionData getData() {
        return idata;
    }

    @Override
    public InstructionProperties getProperties() {
        return this.idata.props;
    }

    /*
    @Override
    public final String getName() {
        return idata.getPlainName();
    }
    */

    @Override
    public final Long getAddress() {
        return address;
    }

    @Override
    public final String getInstruction() {
        return instruction;
    }

    /*
    @Override
    public final int getLatency() {
        return idata.getLatency();
    }
    
    @Override
    public final int getDelay() {
        return idata.getDelay();
    }
    */

    @Override
    public final PseudoInstructionGraph getPseudocodeGraph() {
        var g = new PseudoInstructionGraph(this);
        return g;
    }

    // Check for instruction type /////////////////////////////////////////////
    @Override
    public final boolean isAdd() {
        return getProperties().getGenericType().contains(InstructionType.G_ADD);
    }

    @Override
    public final boolean isSub() {
        return getProperties().getGenericType().contains(InstructionType.G_SUB);
    }

    @Override
    public final boolean isMul() {
        return getProperties().getGenericType().contains(InstructionType.G_MUL);
    }

    @Override
    public final boolean isLogical() {
        return getProperties().getGenericType().contains(InstructionType.G_LOGICAL);
    }

    @Override
    public final boolean isUnary() {
        return getProperties().getGenericType().contains(InstructionType.G_UNARY);
    }

    @Override
    public final boolean isJump() {
        return (getProperties().getGenericType().contains(InstructionType.G_JUMP)
                || this.isConditionalJump() || this.isUnconditionalJump()
                || this.isAbsoluteJump() || this.isRelativeJump() || this.isImmediateJump());
    }

    @Override
    public final boolean isConditionalJump() {
        return getProperties().getGenericType().contains(InstructionType.G_CJUMP);
    }

    @Override
    public final boolean isUnconditionalJump() {
        return getProperties().getGenericType().contains(InstructionType.G_UJUMP);
    }

    @Override
    public final boolean isRelativeJump() {
        return getProperties().getGenericType().contains(InstructionType.G_RJUMP);
    }

    @Override
    public final boolean isAbsoluteJump() {
        return getProperties().getGenericType().contains(InstructionType.G_AJUMP);
    }

    @Override
    public final boolean isImmediateJump() {
        return getProperties().getGenericType().contains(InstructionType.G_IJUMP);
    }

    @Override
    public final boolean isImmediateValue() {
        return getProperties().getGenericType().contains(InstructionType.G_IMMV);
    }

    @Override
    public final boolean isStore() {
        return getProperties().getGenericType().contains(InstructionType.G_STORE);
    }

    @Override
    public final boolean isLoad() {
        return getProperties().getGenericType().contains(InstructionType.G_LOAD);
    }

    @Override
    public final boolean isMemory() {
        return getProperties().getGenericType().contains(InstructionType.G_MEMORY) || this.isLoad() || this.isStore();
    }

    @Override
    public final boolean isFloat() {
        return getProperties().getGenericType().contains(InstructionType.G_FLOAT);
    }

    @Override
    public final boolean isUnknown() {
        return getProperties().getGenericType().contains(InstructionType.G_UNKN);
    }

    ///////////////////////////////////////////// Additional non basic types:
    @Override
    public boolean isBackwardsJump() {
        Number branchTarget = this.getData().getBranchTarget();
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
        Number branchTarget = this.getData().getBranchTarget();
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
     * Gets asm string representation of instruction, including operands
     */
    @Override
    public String getRepresentation() {
        var str = new StringBuilder();
        str.append(this.getName() + "\t");
        var it = this.getData().getOperands().iterator();
        while (it.hasNext()) {
            var curr = it.next();
            str.append(" " + curr.getRepresentation());
            if (it.hasNext() && !curr.isSubOperation())
                str.append(", ");
        }

        // target if branch
        if (this.isJump())
            str.append("\t// target: 0x" +
                    Long.toHexString(this.getData().getBranchTarget().longValue()));

        // register values
        if (this.getData().getRegisters() != null) {
            str.append("\tregs: (");
            var it2 = this.getData().getOperands().iterator();
            while (it2.hasNext()) {
                var curr = it2.next();
                if (curr.isRegister() && curr.isRead()) {
                    str.append(curr.getRepresentation() + " = ");
                    str.append(this.getRegisters().getValue(curr.getRepresentation()) + "  ");
                }
            }
            str.append(")");
        }
        return str.toString();
    }

    /*
     * Creates addr:instruction String.
     */
    @Override
    public String toString() {
        String prt = "0x" + Long.toHexString(this.getAddress().longValue()) + ":";
        prt += this.getInstruction() + "\t " + getRepresentation() + "\t ";
        return prt;
    }

    @Override
    public RegisterDump getRegisters() {
        return this.getData().getRegisters();
    }

    /*@Override
    public void setRegisters(RegisterDump registers) {
        this.registers = registers;
    }*/
}
