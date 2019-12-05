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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.binarysegments.AFrequentSequence;
import pt.up.fe.specs.binarytranslation.binarysegments.FrequentTraceSequence;
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
     * HashCode --> (Addr --> occurence count)
     */
    private Map<Integer, Map<Integer, Integer>> addrs = new HashMap<Integer, Map<Integer, Integer>>();

    /*
     * Public constructor
     */
    public FrequentTraceSequenceDetector(InstructionStream istream) {
        super(istream);
    }

    @Override
    protected void addHashedSequence(Integer hashCode, List<Instruction> candidate, Map<String, String> regremap) {

        var startAddr = candidate.get(0).getAddress();

        // if hash exists
        if (this.addrs.containsKey(hashCode)) {

            var key = this.addrs.get(hashCode);

            // if addr exists
            Integer c = 0;
            if (key.containsKey(startAddr.intValue())) {
                c = key.get(startAddr.intValue());
            }
            key.put(startAddr.intValue(), c + 1);
        }

        // if doesn't
        else {
            this.hashed.put(hashCode, new HashedSequence(candidate, startAddr, regremap));

            var newmap = new HashMap<Integer, Integer>();
            newmap.put(startAddr.intValue(), 1);
            this.addrs.put(hashCode, newmap);
        }
    }

    @Override
    protected void removeUnique() {

        // Remove all sequences which only happen once
        Iterator<Integer> it = this.hashed.keySet().iterator();
        while (it.hasNext()) {
            var addrlist = this.addrs.get(it.next());
            var sum = 0;
            for (Integer i : addrlist.values())
                sum += i;

            if (sum <= 1)
                it.remove();
        }
    }

    @Override
    protected AFrequentSequence makeFrequentSequence(Integer hashcode, List<Instruction> ilist) {
        var addrs = this.addrs.get(hashcode);
        return new FrequentTraceSequence(ilist, addrs);
    }
}
