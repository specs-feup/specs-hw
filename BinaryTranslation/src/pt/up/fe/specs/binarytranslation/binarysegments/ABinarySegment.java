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

/**
 * 
 * @author Nuno
 *
 */
public abstract class ABinarySegment implements BinarySegment {

    // TODO: move these 2 things to a future BinaryApplication class, or something
    protected String appName;
    protected String compilationFlags;

    protected BinarySegmentType segtype;
    protected List<Instruction> instlist;
    protected List<SegmentContext> contexts;

    protected ABinarySegment() {
        this.instlist = new ArrayList<Instruction>();
    }

    public int getSegmentLength() {
        return instlist.size();
    }

    public BinarySegmentType getSegmentType() {
        return this.segtype;
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
            ret += inst.getRepresentation() + "\n";
        }
        return ret;
    }

    public void printSegment() {
        System.out.print(this.getRepresentation());
        return;
    }

    public Integer getSegmentCycles() {
        Integer cycles = 0;
        for (Instruction i : this.instlist) {
            cycles += i.getLatency();
        }
        return cycles;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setCompilationFlags(String compilationFlags) {
        this.compilationFlags = compilationFlags;
    }

    public String getAppName() {
        return appName;
    }

    public String getCompilationFlags() {
        return compilationFlags;
    }

    public List<SegmentContext> getContexts() {
        return contexts;
    }
}
