/**
 * Copyright 2021 SPeCS.
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
 
package org.specs.Arm.instruction;

import org.specs.Arm.parsing.ArmAsmFieldData;

import pt.up.fe.specs.binarytranslation.instruction.AInstructionData;
import pt.up.fe.specs.binarytranslation.instruction.InstructionProperties;

/**
 * This class contains the interpreted fields, accessible trough getters, which are read from the raw parser field data,
 * in "fieldData"; this class should not do any complex processing, and should be used only for data lookup
 * 
 * @author nuno
 *
 */
public class ArmInstructionData extends AInstructionData {

    /*
     * Fields only relevant for ARM instructions
     */
    // private final int bitwidth;
    // private final Boolean isSimd;
    private final ArmInstructionCondition condition;
    // private final Number branchTarget;

    public ArmInstructionData(
            InstructionProperties props,
            ArmAsmFieldData fieldData,
            ArmRegisterDump registers) {
        super(props, fieldData, registers);

        // TODO: not sure if still needed here
        // this.bitwidth = fieldData.getBitWidth();
        // this.isSimd = fieldData.isSimd();
        this.condition = fieldData.getCond();

        // deprecated
        // now handled in AinstructionData
        // this.operands = fieldData.getOperands();
        // this.branchTarget = fieldData.getBranchTarget();
    }

    /*
     * Helper constructor for copy, calls super copy
     */
    private ArmInstructionData(ArmInstructionData other) {
        super(other);
        this.condition = other.condition;

        // this.isSimd = isSimd;
        // this.branchTarget = branchTarget;
        // this.bitwidth = bitwidth;
    }

    /*
     * Copy "constructor"
     */
    @Override
    public ArmInstructionData copy() {
        return new ArmInstructionData(this);

        /*
        String copyname = new String(this.plainname);
        List<InstructionType> copytype = new ArrayList<InstructionType>(this.genericType);
        
        List<Operand> copyops = new ArrayList<Operand>();
        for (Operand op : this.operands)
            copyops.add(op.copy());
        
        return new ArmInstructionData(copyname, this.latency, this.delay, copytype, copyops, this.isSimd,
                this.condition, this.branchTarget, this.bitwidth);*/
    }

    /*
     * Some ARM instructions need a suffix (i.e., the conditionals)
     */
    public String getAdjustedName() {
        var name = this.getProperties().getName();
        if (this.condition.equals(ArmInstructionCondition.NONE)) {
            return name;
        } else {
            return name + "." + this.condition.getShorthandle();
        }
    }

    /*
    @Override
    public int getBitWidth() {
        return bitwidth;
    }
    
    @Override
    public Boolean isSimd() {
        return isSimd;
    }
    
    @Override
    public Number getBranchTarget() {
        return branchTarget;
    }*/
}
