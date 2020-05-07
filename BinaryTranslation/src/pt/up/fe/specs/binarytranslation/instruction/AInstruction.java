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

import java.io.Serializable;
import java.util.*;

import pt.up.fe.specs.binarytranslation.expression.ExpressionSolver;
import pt.up.fe.specs.binarytranslation.parsing.AsmField;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Generic implementation of interface instruction.
 * 
 * @author NunoPaulino
 * 
 */
public abstract class AInstruction implements Instruction, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1450984418310887398L;

    private Number address;
    private String instruction;

    // decoded field data (abstract class)
    private final InstructionData idata;

    // shared by all instructions, so they can go decode themselves
    protected static InstructionSet instSet;

    // the enum which represents the ISA properties of this instruction
    protected InstructionProperties props;

    public AInstruction(Number address, String instruction,
            InstructionData idata, InstructionProperties props) {
        this.address = address;
        this.instruction = instruction.strip();
        this.idata = idata;
        this.props = props;
    }

    ///////////////////////////////////////////////////////////////////////////
    public InstructionData getData() {
        return idata;
    }

    public InstructionProperties getProperties() {
        return props;
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
        return (idata.getGenericTypes().contains(InstructionType.G_JUMP)
                || this.isConditionalJump() || this.isUnconditionalJump()
                || this.isAbsoluteJump() || this.isRelativeJump() || this.isImmediateJump());
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

    public final boolean isImmediateJump() {
        return idata.getGenericTypes().contains(InstructionType.G_IJUMP);
    }

    public final boolean isImmediateValue() {
        return idata.getGenericTypes().contains(InstructionType.G_IMMV);
    }

    public final boolean isStore() {
        return idata.getGenericTypes().contains(InstructionType.G_STORE);
    }

    public final boolean isLoad() {
        return idata.getGenericTypes().contains(InstructionType.G_LOAD);
    }

    public final boolean isMemory() {
        return idata.getGenericTypes().contains(InstructionType.G_MEMORY) || this.isLoad() || this.isStore();
    }

    public final boolean isFloat() {
        return idata.getGenericTypes().contains(InstructionType.G_FLOAT);
    }

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
    public String getRepresentation() {
        String str = this.getName() + "\t";
        Iterator<Operand> it = this.getData().getOperands().iterator();

        while (it.hasNext()) {
            var curr = it.next();
            str += " " + curr.getRepresentation();
            if (it.hasNext() && !curr.isSubOperation())
                str += ", ";
        }

        return str;
    }

    /*
     * Prints addr:instruction to system output
     */
    @Override
    public void printInstruction() {
        String prt = "0x" + Long.toHexString(this.getAddress().longValue()) + ":";
        prt += this.getInstruction() + "\t " + getRepresentation() + "\t "; // + this.express();
        System.out.print(prt + "\n");
    }

    /* 
     * 
     */
    public void makeSymbolic(Number address, Map<String, String> regremap) throws NullPointerException {

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

    /*
     * 
     */
    public String express() {

        if (this.getProperties().getExpression() == null) {
            return "No expression set yet!";
        }

        // helper map so i have refs from operands to asmfields
        Map<AsmField, Operand> helper = new HashMap<AsmField, Operand>();
        for (Operand op : this.getData().getOperands()) {
            helper.put(op.getAsmField(), op);
        }

        return ExpressionSolver.solve(
                this.getProperties().getExpression(), helper);
    }
}
