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

import java.io.Serializable;
import java.util.*;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * 
 * @author Nuno
 *
 */
public abstract class ABinarySegment implements BinarySegment, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2260035887787499004L;

    protected Application appinfo;
    protected BinarySegmentType segtype;
    protected List<Instruction> instlist;
    protected List<SegmentContext> contexts;
    protected float staticCoverage;
    protected float dynamicCoverage;

    protected ABinarySegment(List<Instruction> ilist,
            List<SegmentContext> contexts, Application appinfo) {
        this.instlist = ilist;
        this.appinfo = appinfo;
        this.contexts = contexts;
    }

    /*
     * Basics
     */
    public List<Instruction> getInstructions() {
        return this.instlist;
    }

    public int getSegmentLength() {
        return instlist.size();
    }

    public BinarySegmentType getSegmentType() {
        return this.segtype;
    }

    public List<SegmentContext> getContexts() {
        return contexts;
    }

    public Application getAppinfo() {
        return appinfo;
    }

    /*
     * Execution
     */
    public int getLatency() {
        int totlat = 0;
        for (int i = 0; i < instlist.size(); i++)
            totlat += instlist.get(i).getLatency();
        return totlat;
    }

    // TODO: execution cycles, i.e. total cycles taking into
    // account the occurrences of all contexts of the segment

    /*
     * Coverage functions
     */
    public void setDynamicCoverage(float dcoverage) {
        this.dynamicCoverage = dcoverage;
        // TODO throw something if already set
    }

    public void setStaticCoverage(float scoverage) {
        this.staticCoverage = scoverage;
        // TODO throw something if already set
    }

    public float getStaticCoverage() {
        return staticCoverage;
    }

    public float getDynamicCoverage() {
        return dynamicCoverage;
    }

    /*
     * Printing Functions
     */
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
}
