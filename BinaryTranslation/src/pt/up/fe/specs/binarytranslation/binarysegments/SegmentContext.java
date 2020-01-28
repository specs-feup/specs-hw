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

import java.util.Map;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.HashedSequence;

/**
 * Holds the mapping of a symbolic representation of a register or value (e.g., r<a>, r<b>.. to a specific register or
 * value of the originating processor instruction trace). This is required to generate CDFGs and/or final outputs for
 * every in/out context
 * 
 * @author nuno
 *
 */
public class SegmentContext {

    // TODO, for trace segments, the context could include the values of the registers if possible
    // i think i can query this out of gdb...

    private Integer startaddr;
    private int ocurrences;
    private Map<String, String> context;

    public SegmentContext(HashedSequence seq) {

        this.startaddr = seq.getStartAddress();
        this.context = seq.getRegremap();
        this.ocurrences = seq.getOcurrences();
    }

    public Integer getStartaddresses() {
        return this.startaddr;
    }

    public int getOcurrences() {
        return ocurrences;
    }

    /*
     * Retrieves the register remmaping map
     */
    public String getRepresentation() {

        String ret = "Start Addresses: 0x" + Integer.toHexString(this.startaddr) + "\n";
        ret += "Number of ocurrences: " + Integer.toString(this.ocurrences) + "\n";
        for (String str : this.context.keySet())
            ret += this.context.get(str) + "\t:\t" + str + "\n";

        return ret;
    }

    /*
     * returns String representation of non symbolic originating operand (i.e. r4, or w7)
     */
    public String resolve(String symbol) {
        return this.context.get(symbol);
    }

    /*
     * Compare two contexts by mapping values
     */
    public Boolean equals(SegmentContext that) {
        return this.context.equals(that.context);
    }
}
