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
public abstract class AInstructionData {

    // from enum properties
    protected final InstructionProperties props;

    /*protected final String plainname;
    protected final int latency;
    protected final int delay;
    protected final List<InstructionType> genericType;*/

    // from GDB
    private final RegisterDump registers; // RegisterDump.nullDump;
    // TODO fix later

    // from asm fields
    protected final List<Operand> operands;

    // compute from operands during parsing
    private final Number branchTarget;

    /*
     * Helper Constructor
     
    private AInstructionData(
            InstructionProperties props,
            List<Operand> operands,
            Number branchTarget,
            RegisterDump registers) {
        this.props = props;
        this.operands = operands;
        this.branchTarget = branchTarget;
        this.registers = registers;
    }*/

    /*
     * Constructor
     */
    protected AInstructionData(InstructionProperties props,
            AsmFieldData fieldData,
            RegisterDump registers) {
        this.props = props;
        this.operands = fieldData.getOperands(registers);
        this.branchTarget = fieldData.getBranchTarget(this.operands);
        this.registers = registers;
    }

    /*
     * 
     
    private static List<Operand> cloneOperands(AInstructionData other) {
        var copyops = new ArrayList<Operand>();
        for (Operand op : other.getOperands())
            copyops.add(op.copy());
        return copyops;
    }*/

    /*
     * Private helper copy
     */
    protected AInstructionData(AInstructionData other) {
        this.props = other.props;
        this.operands = other.operands;
        this.branchTarget = other.branchTarget;
        this.registers = other.registers;

        // NOTE: assume that operands, branchtarget, and registers are IMMUTABLE

        /*
        this(new String(other.getPlainName()),
                other.getLatency(),
                other.getDelay(),
                new ArrayList<InstructionType>(other.getGenericTypes()), // TODO: does this deep copy??
                AInstructionData.cloneOperands(other),
                other.getBranchTarget(),
                other.getRegisters().copy()); // TODO: does this deep copy??*/
    }

    /*
     * Must be implemented by children  
     */
    public abstract AInstructionData copy();

    /*
     * 
     */

    public InstructionProperties getProperties() {
        return props;
    }

    /*
     * Get plain name
     
    public String getPlainName() {
        return this.props.getName();
    }
    
    
     * Get latency
     
    public int getLatency() {
        return this.props.getLatency()
    }
    
    
     * get delay
     
    public int getDelay() {
        return this.delay;
    }
    
    
     * Get all generic types of this instruction
     
    public List<InstructionType> getGenericTypes() {
        return this.genericType;
    }*/

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
