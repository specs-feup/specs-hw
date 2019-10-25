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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;

/**
 * 
 * @author Nuno
 *
 */
public abstract class ABinarySegment implements BinarySegment {

    protected SegmentType segtype;
    protected List<Instruction> instlist;
    protected List<Operand> liveins = null, liveouts = null;

    public ABinarySegment() {
        this.instlist = new ArrayList<Instruction>();
    }

    public int getSegmentLength() {
        return instlist.size();
    }

    public SegmentType getSegmentType() {
        return this.segtype;
    }

    public List<Operand> getLiveIns() {
        return this.liveins;
    }

    public List<Operand> getLiveOuts() {
        return this.liveouts;
    }

    public int getTotalLatency() {
        int totlat = 0;
        for (int i = 0; i < instlist.size(); i++)
            totlat += instlist.get(i).getLatency();
        return totlat;
    }

    public List<Instruction> getInstructions() {
        return this.instlist;
    }

    public String getRepresentation() {
        String ret = "";
        for (Instruction inst : this.instlist) {
            ret += inst.getRepresentation();
        }
        return ret;
    }

    public void printSegment() {
        System.out.print(this.getRepresentation());
        return;
    }

    /*
     * Call to build liveins and liveouts list, only after segment is complete
     */
    protected void buildLiveInsAndLiveOuts() {

        this.liveins = new ArrayList<Operand>();
        this.liveouts = new ArrayList<Operand>();

        for (Instruction i : this.instlist) {
            for (Operand op : i.getData().getOperands()) {

                // everything that is a register is a livein
                if (op.isSymbolic() && op.isRead() && !this.liveins.contains(op)) {
                    this.liveins.add(op);
                }

                // only liveout if written
                if (op.isSymbolic() && op.isWrite() && !this.liveouts.contains(op)) {
                    this.liveouts.add(op);
                }
            }
        }
        return;
    }
}
