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

import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;

/**
 * Generic implementation of interface instruction.
 * 
 * @author NunoPaulino
 * 
 */
public abstract class AInstruction implements Instruction {

    @Expose
    private Long address;

    @Expose
    private String instruction;

    private RegisterDump registers = null; // RegisterDump.nullDump;
    // TODO fix later

    // decoded field data (abstract class)
    private final InstructionData idata;

    // shared by all instructions, so they can go decode themselves
    protected static InstructionSet instSet;

    // the enum which represents the ISA properties of this instruction
    protected final InstructionProperties props;

    public AInstruction(Long address, String instruction,
            InstructionData idata, InstructionProperties props) {
        this.address = address;
        this.instruction = instruction.strip();
        this.idata = idata;
        this.props = props;
    }

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public InstructionData getData() {
        return idata;
    }

    @Override
    public InstructionProperties getProperties() {
        return props;
    }

    @Override
    public final String getName() {
        return idata.getPlainName();
    }

    @Override
    public final Long getAddress() {
        return address;
    }

    @Override
    public final String getInstruction() {
        return instruction;
    }

    @Override
    public final int getLatency() {
        return idata.getLatency();
    }

    @Override
    public final int getDelay() {
        return idata.getDelay();
    }

    /*@Override
    public String toString() {
        return SpecsStrings.toHexString(address.longValue(), 8) + ": " + instruction;
    }*/

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
        return (idata.getGenericTypes().contains(InstructionType.G_JUMP)
                || this.isConditionalJump() || this.isUnconditionalJump()
                || this.isAbsoluteJump() || this.isRelativeJump() || this.isImmediateJump());
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
    public final boolean isImmediateJump() {
        return idata.getGenericTypes().contains(InstructionType.G_IJUMP);
    }

    @Override
    public final boolean isImmediateValue() {
        return idata.getGenericTypes().contains(InstructionType.G_IMMV);
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
        return idata.getGenericTypes().contains(InstructionType.G_MEMORY) || this.isLoad() || this.isStore();
    }

    @Override
    public final boolean isFloat() {
        return idata.getGenericTypes().contains(InstructionType.G_FLOAT);
    }

    @Override
    public final boolean isUnknown() {
        return idata.getGenericTypes().contains(InstructionType.G_UNKN);
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
     * Gets asm string representation of instruction, including operands
     */
    @Override
    public String getRepresentation() {
        var str = this.getName() + "\t";
        var it = this.getData().getOperands().iterator();

        while (it.hasNext()) {
            var curr = it.next();
            str += " " + curr.getRepresentation();
            if (it.hasNext() && !curr.isSubOperation())
                str += ", ";
        }

        // target if branch
        if (this.isJump())
            str += "\t// target: 0x" +
                    Long.toHexString(this.getBranchTarget().longValue());

        return str;
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
        return registers;
    }

    @Override
    public void setRegisters(RegisterDump registers) {
        this.registers = registers;
    }

    /* 
     * TODO: very clunky here
     * TODO: replace with SymbolicInstruction class!
     
    @Override
    public void makeSymbolic(Integer address, Map<String, String> regremap) throws NullPointerException {
    
        // symbolify address
        this.address = address;
    
        // symbolify operands
        for (Operand op : this.getData().getOperands()) {
            if (!op.isSubOperation() && !op.isSpecial()) {
                // TODO move this condition to the construction of the
                // regremap map
    
                var tmp = op.getRepresentation();
    
                String r = null;
                try {
                    r = regremap.get(tmp);
    
                } catch (NullPointerException e) {
                    throw e;
                }
                // TODO if a certain operand doesnt have a remap value, it should not be made symbolic!
    
                String r2 = null;
                try {
                    r2 = r.substring(r.indexOf('<') + 1, r.indexOf('>')); // NOTE: and ugly hack, but works...
    
                } catch (NullPointerException e) {
                    throw e;
                }
    
                op.setSymbolic(r2);
    
                // TODO: BIG ISSUE
                // for ARM, i cannot symbolify the address, since one of the IMM operands is computed from it...
                // this happens for ldr for instance, and others
                // check for "fielddata.getAddr()" in arm operand getter
            }
        }
    }
    */
}
