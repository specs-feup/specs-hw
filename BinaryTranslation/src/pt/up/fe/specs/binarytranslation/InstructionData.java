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

package pt.up.fe.specs.binarytranslation;

import java.util.List;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;

/**
 * Holds data taken from an executed or static instruction, after decoding its raw parsed fields
 * 
 * @author NunoPaulino
 *
 */
public abstract class InstructionData {

    protected String plainname;
    protected int latency;
    protected int delay;
    protected List<InstructionType> genericType;
    protected List<InstructionOperand> operands;

    public InstructionData(InstructionProperties props, AsmInstructionData fieldData) {
        this.plainname = props.getName();
        this.latency = props.getLatency();
        this.delay = props.getDelay();
        this.genericType = props.getGenericType();
        this.operands = fieldData.getOperands();
    }

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
     * Get Operands
     */
    public List<InstructionOperand> getOperands() {
        return this.operands;
    }
}
