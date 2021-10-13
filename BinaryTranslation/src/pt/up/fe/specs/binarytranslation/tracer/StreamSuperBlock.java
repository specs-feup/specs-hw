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

public class StreamSuperBlock extends AStreamUnit {

    private List<StreamBasicBlock> tbblist;

    /*
     * Deep Copy
     
    public StreamSuperBlock(StreamSuperBlock other) {
        super(other);
        this.tbblist = new ArrayList<StreamBasicBlock>();
        for (var el : other.getList())
            this.tbblist.add(new StreamBasicBlock(el));
    }*/

    /*
     * Deep copy
     
    @Override
    public StreamSuperBlock deepCopy() {
        return new StreamSuperBlock(this);
    }*/

    /*
     * User by copy constructor
     */
    protected List<StreamBasicBlock> getList() {
        return this.tbblist;
    }

    public StreamSuperBlock(List<StreamBasicBlock> tbblist) {
        super(StreamUnitType.StreamSuperBlock,
                tbblist.get(tbblist.size() - 1).getTargetAddr());
        this.tbblist = tbblist;
    }

    // used in map comparisons
    @Override
    public int hashCode() {
        int hash = 0;
        for (var tbb : this.tbblist) {
            hash += tbb.hashCode();
        }
        return hash; // TODO: works??
    }

    @Override
    public String toString() {
        var bld = new StringBuilder();
        for (var bb : this.tbblist) {
            bld.append(bb.toString());
        }
        return bld.toString();
    }

    @Override
    public Instruction getStart() {
        return this.tbblist.get(0).getStart();
    }

    @Override
    public Instruction getEnd() {
        return this.tbblist.get(this.tbblist.size() - 1).getEnd();
    }

    @Override
    public boolean containsAddr(Long addr) {
        for (var tbb : this.tbblist) {
            if (tbb.containsAddr(addr))
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
        for (var tbb : this.tbblist) {
            if (tbb.includesTarget(other))
                return true;
        }
        return false;
    }
}
