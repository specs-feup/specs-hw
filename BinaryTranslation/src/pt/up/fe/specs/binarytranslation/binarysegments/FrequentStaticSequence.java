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
public class FrequentStaticSequence extends AFrequentSequence {

    private List<Integer> startAddresses;
    // list of addresses in the ELF where this sequence happens

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    public FrequentStaticSequence(List<Instruction> ilist, List<SegmentContext> contexts) {
        super(ilist, contexts);
        this.segtype = BinarySegmentType.STATIC_FREQUENT_SEQUENCE;

        this.startAddresses = new ArrayList<Integer>();
        for (SegmentContext context : contexts)
            this.startAddresses.add(context.getStartaddresses());
    }

    @Override
    public int getExecutionCycles() {
        Integer staticCycles = this.getLatency();
        return staticCycles * this.contexts.size();
    }

    @Override
    protected List<Integer> getAddresses() {
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
