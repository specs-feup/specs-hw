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
 
package pt.up.fe.specs.binarytranslation.detection.segments;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class MegaBlock extends ABinarySegment {

    private List<Integer> startAddresses;

    public MegaBlock(List<Instruction> symbolicseq, List<SegmentContext> contexts, Application app) {
        super(symbolicseq, contexts, app);
        this.segtype = BinarySegmentType.MEGA_BLOCK;
        this.startAddresses = new ArrayList<Integer>();
        for (SegmentContext context : contexts)
            this.startAddresses.add(context.getStartaddresses());
    }

    @Override
    public int getExecutionCycles() {

        // multiply by number of contexts and their ocurrences
        Integer ocurrences = 0;
        for (SegmentContext c : this.getContexts()) {
            ocurrences += c.getOcurrences();
        }

        return this.getLatency() * ocurrences;
    }

    @Override
    public int getOccurences() {
        int occ = 0;
        for (SegmentContext c : this.getContexts()) {
            occ += c.getOcurrences();
        }
        return occ;
    }

    @Override
    public List<Integer> getAddresses() {
        return this.startAddresses;
    }

    @Override
    protected String getAddressListRepresentation() {
        String ret = "";
        for (Integer addr : this.startAddresses) {
            ret += "0x" + Integer.toHexString(addr) + " ";
        }
        ret += "]\n" + "Total execution cycles: " + this.getExecutionCycles() + "\n";
        return ret;
    }
}
