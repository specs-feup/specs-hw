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
 
package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class StreamInstruction extends AStreamUnit {

    private final Instruction inst;

    /*
     * Copy
     
    public StreamInstruction(StreamInstruction other) {
        super(other);
        this.inst = other.getActual().copy();
    }
    
    @Override
    public StreamInstruction deepCopy() {
        return new StreamInstruction(this);
    }*/

    public StreamInstruction(Instruction inst) {
        super(StreamUnitType.StreamInstruction,
                StreamInstruction.getBranchTarget(inst));
        this.inst = inst;
    }

    protected static Long getBranchTarget(Instruction inst) {
        if (inst.isJump())
            return (Long) inst.getData().getBranchTarget();
        else
            return inst.getAddress() + 4;
    }

    @Override
    public int hashCode() {
        return this.inst.getAddress().intValue(); // .hashCode();
    }

    public Instruction getActual() {
        return inst;
    }

    @Override
    public String toString() {
        return this.inst.toString() + "\n";
    }

    @Override
    public Instruction getStart() {
        return this.inst;
    }

    @Override
    public Instruction getEnd() {
        return this.inst;
    }

    @Override
    public boolean containsAddr(Long addr) {
        return this.inst.getAddress().longValue() == addr.longValue();
    }

    /*
     * True if any instruction in this TraceUnit
     * includes the target of the "other"
     */
    @Override
    public boolean includesTarget(StreamUnit other) {
        var otherTargetAddr = other.getTargetAddr();
        return this.inst.getAddress().longValue() == otherTargetAddr.longValue();
    }
}
