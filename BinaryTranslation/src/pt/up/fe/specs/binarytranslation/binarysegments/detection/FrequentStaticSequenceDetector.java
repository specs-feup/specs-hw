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
import pt.up.fe.specs.binarytranslation.binarysegments.FrequentStaticSequence;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author Nuno
 *
 */
public class FrequentStaticSequenceDetector extends AFrequentSequenceDetector {

    /*
     * This map holds all hash codes and list of occurring addresses for each
     * Map: <hashcode, list of addresses>
     */
    private Map<Integer, List<Integer>> addrs = new HashMap<Integer, List<Integer>>();

    /*
     * Public constructor
     */
    public FrequentStaticSequenceDetector(InstructionStream istream) {
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

    /*
     * Remove all sequences which only happen once
     */
    @Override
    protected void removeUnique() {

        // iterate through hashcodes of sequences
        Iterator<Integer> it = this.addrs.keySet().iterator();

        while (it.hasNext()) {
            var hashcode = it.next();
            var addrlist = this.addrs.get(hashcode);
            if (addrlist.size() <= 1) {

                // remove hashed sequence from hashed sequences list by its starting addr
                var keyval = hashcode.toString() + "_" + Integer.toString(addrlist.get(0));
                this.hashed.remove(keyval);
                it.remove();
            }
        }
    }

    private FrequentStaticSequence makeFrequentSequence(List<HashedSequence> seqs) {

        // use first sequence with this hash code to create symbolic sequence
        var symbolicseq = seqs.get(0).makeSymbolic();

        // Create all contexts
        var contexts = new ArrayList<SegmentContext>();
        for (HashedSequence seq : seqs.subList(0, seqs.size()))
            contexts.add(new SegmentContext(seq.getStartAddresss(), seq.getRegremap()));

        return new FrequentStaticSequence(symbolicseq, contexts);
    }

    @Override
    protected void makeFrequentSequences() {

        // for all sequences which occur more than once, symbolify and add to output
        this.allsequences = new ArrayList<BinarySegment>();

        // all start addrs grouped by hashcode
        Iterator<Integer> it = this.addrs.keySet().iterator();

        // for each hashcode
        while (it.hasNext()) {

            // get hashcode
            var hashcode = it.next();

            // get all start addrs of all sequences with this hashcode
            var addrlist = this.addrs.get(hashcode);

            // get a list of the sequences by their hashcode_startaddr key
            var seqlist = new ArrayList<HashedSequence>();
            for (Integer startaddr : addrlist) {
                var keyval = hashcode.toString() + "_" + Integer.toString(startaddr);
                seqlist.add(this.hashed.get(keyval));
            }

            // make the frequent sequence
            this.allsequences.add(makeFrequentSequence(seqlist));
        }
    }
}
