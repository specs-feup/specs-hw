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
public class FrequentStaticSequence extends AFrequentSequence {

    private List<Integer> startAddresses;
    // addr, frequency count

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    private FrequentStaticSequence(List<Instruction> ilist) {
        super(ilist);
        this.segtype = SegmentType.STATIC_FREQUENT_SEQUENCE;
    }

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    public FrequentStaticSequence(List<Instruction> ilist, List<Integer> startAddresses) {
        this(ilist);
        this.startAddresses = startAddresses;
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
        ret += "]\n";
        return ret;
    }
}
