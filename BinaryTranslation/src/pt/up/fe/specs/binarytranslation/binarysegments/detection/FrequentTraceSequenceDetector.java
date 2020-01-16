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

package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.FrequentTraceSequence;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author nuno
 *
 */
public class FrequentTraceSequenceDetector extends AFrequentSequenceDetector {

    /*
     * This map holds all hash codes and list of occurring addresses for each
     * Map: <hashcode, list of addresses>
     */
    private Map<Integer, List<Integer>> addrs = new HashMap<Integer, List<Integer>>();

    /*
     * Public constructor
     */
    public FrequentTraceSequenceDetector(InstructionStream istream) {
        super(istream);
    }

    @Override
    protected void listHashedSequence(Integer hashCode, Integer startAddr) {

        // add sequence addr to list, if equivalent already exists
        if (this.addrs.containsKey(hashCode)) {
            this.addrs.get(hashCode).add(startAddr);
        }

        // add hashcode to addr list map
        else {
            var l = new ArrayList<Integer>();
            l.add(startAddr);
            this.addrs.put(hashCode, l);
        }
    }

    @Override
    protected void removeUnique() {

        // iterate through hashcodes of sequences
        Iterator<Integer> it = this.addrs.keySet().iterator();

        while (it.hasNext()) {
            var hashcode = it.next();
            var addrlist = this.addrs.get(hashcode);

            // number of executions of this segment
            var sum = 0;
            for (Integer i : addrlist.values()) {
                var keyval = hashcode.toString() + "_" + Integer.toString(i);
                sum += this.hashed.get(keyval).getOcurrences();
            }

            if (sum <= 1) {

                // remove hashed sequence from hashed sequences list by its starting addr
                var keyval = hashcode.toString() + "_" + Integer.toString(addrlist.get(0));
                this.hashed.remove(keyval);
                it.remove();
            }
        }
    }

    @Override
    protected List<Integer> getAddressList(Integer hashcode) {
        return new ArrayList<>(this.addrs.get(hashcode).keySet());
    }

    @Override
    protected Iterator<Integer> getHashIterator() {
        return this.addrs.keySet().iterator();
    }

    protected BinarySegment makeFrequentSequence(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new FrequentTraceSequence(symbolicseq, contexts);
    }
}
