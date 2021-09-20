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
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDumpNull;

/**
 * Holds data taken from an executed or static instruction, after decoding its raw parsed fields
 * 
 * @author NunoPaulino
 *
 */
public abstract class AInstructionData {

    // the enum which represents the ISA properties of this instruction
    protected final InstructionProperties props;

    // from GDB (or null)
    private final RegisterDump registers;

    // from asm fields
    protected final List<Operand> operands;

    // compute from operands during parsing
    private final Number branchTarget;

    /*
     * Constructor
     */
    protected AInstructionData(InstructionProperties props,
            AsmFieldData fieldData,
            RegisterDump registers) {
        this.props = props;
        this.registers = (registers == null) ? (new RegisterDumpNull()) : registers;
        this.operands = fieldData.getOperands(this.registers);
        this.branchTarget = fieldData.getBranchTarget(this.registers);
        // NOTE: if registers are RegisterDumpNull, then all the
        // operand and branch resolvers should still work by returning 0 for all register values
        // (needed for static instruction analysis)
    }

    /*
     * Private helper copy
     */
    protected AInstructionData(AInstructionData other) {
        this.props = other.props;
        this.operands = other.operands;
        this.branchTarget = other.branchTarget;
        this.registers = other.registers;
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
