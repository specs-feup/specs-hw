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

package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * 
 * @author Nuno
 *
 */
public class StaticBasicBlock extends ABasicBlock {

    /*
     * 
     */
    public StaticBasicBlock(List<Instruction> ilist, List<SegmentContext> contexts) {
        super(ilist, contexts);
        this.segtype = BinarySegmentType.STATIC_BASIC_BLOCK;
    }

    @Override
    public int getUniqueId() {
        String hashstring = "";
        for (Instruction i : this.instlist) {
            hashstring += i.getAddress().toString();
        }
        return hashstring.hashCode();
    }

    @Override
    public Integer getExecutionCycles() {
        Integer cycles = 0;
        for (Instruction i : this.instlist) {
            cycles += i.getLatency();
        }
        return cycles;
    }
}