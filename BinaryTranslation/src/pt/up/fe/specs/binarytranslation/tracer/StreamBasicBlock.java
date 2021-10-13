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

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class StreamBasicBlock extends AStreamUnit {

    private List<StreamInstruction> tilist;

    /*
     * Deep Copy
     
    public StreamBasicBlock(StreamBasicBlock other) {
        super(other);
        this.tilist = new ArrayList<StreamInstruction>();
        for (var el : other.getList())
            this.tilist.add(new StreamInstruction(el));
    }
    
    @Override
    public StreamBasicBlock deepCopy() {
        return new StreamBasicBlock(this);
    }*/

    /*
     * User by copy constructor
     */
    protected List<StreamInstruction> getList() {
        return this.tilist;
    }

    public StreamBasicBlock(List<StreamInstruction> tilist, StreamUnitType bbtype) {
        super(bbtype, StreamBasicBlock.getBranchTarget(tilist));
        this.tilist = tilist;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (var tinst : this.tilist) {
            hash += tinst.hashCode();
        }
        return hash; // TODO: works??
    }

    @Override
    public String toString() {
        var bld = new StringBuilder();
        for (var tinst : this.tilist) {
            bld.append(tinst.toString());
        }
        return bld.toString();
    }

    @Override
    public Instruction getStart() {
        return this.tilist.get(0).getActual();
    }

    @Override
    public Instruction getEnd() {
        return this.tilist.get(this.tilist.size() - 1).getActual();
    }

    @Override
    public boolean containsAddr(Long addr) {
        for (var tinst : this.tilist) {
            if (tinst.getActual().getAddress().longValue() == addr.longValue())
                return true;
        }
        return false;
    }

    /*
     * True if any instruction in this TraceUnit
     * includes the target of the "other"
     */
    @Override
    public boolean includesTarget(StreamUnit other) {
        var otherTargetAddr = other.getTargetAddr();
        for (var inst : this.tilist) {
            if (inst.getActual().getAddress().longValue() == otherTargetAddr.longValue())
                return true;
        }
        return false;
    }
}
