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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

/**
 * Holds data taken from an executed or static instruction, after decoding its raw parsed fields
 * 
 * @author NunoPaulino
 *
 */
public abstract class InstructionData {

    // from enum properties
    protected final String plainname;
    protected final int latency;
    protected final int delay;
    protected final List<InstructionType> genericType;

    //
    private final RegisterDump registers; // RegisterDump.nullDump;
    // TODO fix later

    // from asm fields
    protected final List<Operand> operands;

    // compute from operands during parsing
    private final Number branchTarget;

    /*
     * Helper Constructor
     */
    private InstructionData(String name,
            int latency, int delay,
            List<InstructionType> genericType,
            List<Operand> operands,
            Number branchTarget,
            RegisterDump registers) {
        this.plainname = name;
        this.latency = latency;
        this.delay = delay;
        this.genericType = genericType;
        this.operands = operands;
        this.branchTarget = branchTarget;
        this.registers = registers;
    }

    /*
     * Constructor
     */
    public InstructionData(InstructionProperties props,
            AsmFieldData fieldData,
            RegisterDump registers) {
        this(props.getName(),
                props.getLatency(),
                props.getDelay(),
                props.getGenericType(),
                fieldData.getOperands(registers),
                fieldData.getBranchTarget(registers),
                registers);
    }

    /*
     * 
     */
    private static List<Operand> cloneOperands(InstructionData other) {
        var copyops = new ArrayList<Operand>();
        for (Operand op : other.getOperands())
            copyops.add(op.copy());
        return copyops;
    }

    /*
     * Private helper copy
     */
    protected InstructionData(InstructionData other) {
        this(new String(other.getPlainName()),
                other.getLatency(),
                other.getDelay(),
                new ArrayList<InstructionType>(other.getGenericTypes()), // TODO: does this deep copy??
                InstructionData.cloneOperands(other),
                other.getBranchTarget(),
                new RegisterDump(other.getRegisters())); // TODO: does this deep copy??
    }

    /*
     * Must be implemented by children  
     */
    public abstract InstructionData copy();

    /*
     * Get plain name
     */
    public String getPlainName() {
        return this.plainname;
    }

    /*
     * Get latency
     */
    public int getLatency() {
        return this.latency;
    }

    /*
     * get delay
     */
    public int getDelay() {
        return this.delay;
    }

    /*
     * Get all generic types of this instruction
     */
    public List<InstructionType> getGenericTypes() {
        return this.genericType;
    }

    /*
     * Get register values
     */
    public RegisterDump getRegisters() {
        return registers;
    }

    /*
     * Get Operands
     */
    public List<Operand> getOperands() {
        return this.operands;
    }

    /*
    * Get target of branch if instruction is branch
    */
    public Number getBranchTarget() {
        return branchTarget;
    }

    ///////////////////////////////////////////////////////////////////////////
    public int getBitWidth() {
        return 32;
    }

    public Boolean isSimd() {
        return false;
    }
}
