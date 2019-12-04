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

import pt.up.fe.specs.binarytranslation.binarysegments.FrequentStaticSequence;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author Nuno
 *
 */
public class FrequentStaticSequenceDetector extends AFrequentSequenceDetector {

    /*
     * This map holds all hash codes and list of occurring addresses for each
     */
    private Map<Integer, List<Integer>> addrs = new HashMap<Integer, List<Integer>>();

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public FrequentStaticSequenceDetector(InstructionStream istream) {
        super(istream);
    }

    @Override
    protected void addHashedSequence(Integer hashCode, List<Instruction> candidate, Map<String, String> regremap) {

        var startAddr = candidate.get(0).getAddress();

        // add sequence addr to list, if equivalent already exists
        if (this.hashed.containsKey(hashCode)) {

            this.addrs.get(hashCode).add(startAddr.intValue());
        }

        // add new sequence
        else {
            this.hashed.put(hashCode, new HashedSequence(candidate, startAddr, regremap));
        }
    }

    @Override
    protected void removeUnique() {

        // Remove all sequences which only happen once
        Iterator<Integer> it = this.hashed.keySet().iterator();
        while (it.hasNext()) {
            var addrlist = this.addrs.get(it.next());
            if (addrlist.size() <= 1)
                it.remove();
        }
    }

    @Override
    protected FrequentStaticSequence makeFrequentSequence(Integer hashcode, List<Instruction> ilist) {
        var addrs = this.addrs.get(hashcode);
        return new FrequentStaticSequence(ilist, addrs);
    }
}

// for trace sequence detector:
/*
 *             Integer c = 0;
            if (this.addrs.containsKey(hashCode))
                c = this.addrs.get(hashCode).add(startAddr);

            this.hashed.get(hashCode).addrs.put(startAddr, c + 1);
            */
