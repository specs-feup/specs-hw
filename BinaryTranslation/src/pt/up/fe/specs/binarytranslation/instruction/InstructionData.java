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

    // from asm fields
    protected List<Operand> operands;

    /*
     * Constructor
     */
    public InstructionData(InstructionProperties props) {
        this.plainname = props.getName();
        this.latency = props.getLatency();
        this.delay = props.getDelay();
        this.genericType = props.getGenericType();
    }

    /*
     * Private helper
     */
    protected InstructionData(String plainname,
            int latency, int delay, List<InstructionType> genericType, List<Operand> ops) {
        this.plainname = plainname;
        this.latency = latency;
        this.delay = delay;
        this.genericType = genericType;
        this.operands = ops;
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
    public List<Operand> getOperands() {
        return this.operands;
    }

    ///////////////////////////////////////////////////////////////////////////
    public int getBitWidth() {
        return 32;
    }

    public Boolean isSimd() {
        return false;
    }
}
