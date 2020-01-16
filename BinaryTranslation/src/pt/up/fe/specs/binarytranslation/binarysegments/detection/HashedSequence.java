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
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.SimpleInstruction;

/**
 * Holds a dumbed down list of instruction that comprise a valid candidate sequence
 * 
 * @author nuno
 *
 */
public class HashedSequence {

    private int hashcode;
    private Number startaddr;
    private List<SimpleInstruction> instlist;
    private Map<String, String> regremap;

    HashedSequence(int hashcode, List<Instruction> instlist, Map<String, String> regremap) {

        this.hashcode = hashcode;
        this.regremap = regremap;
        this.startaddr = instlist.get(0).getAddress();

        // save only instruction as hex, and addr too
        this.instlist = new ArrayList<SimpleInstruction>();
        for (Instruction i : instlist)
            this.instlist.add(new SimpleInstruction(i));
    }

    public int getHashcode() {
        return hashcode;
    }

    public Integer getStartAddresss() {
        return this.startaddr.intValue();
    }

    public Map<String, String> getRegremap() {
        return regremap;
    }

    /*
     * regenerates this hashed sequence as a symbolic instruction list
     */
    public List<Instruction> makeSymbolic() {

        // rebuilt complete instructions from SimpleInstructions
        var rebuiltI = new ArrayList<Instruction>();
        for (SimpleInstruction i : this.instlist)
            rebuiltI.add(i.rebuild());

        // Symbolify
        Integer addr = 0;
        for (Instruction i : rebuiltI) {
            i.makeSymbolic(addr, this.regremap);
            addr += 4;
        }

        return rebuiltI;
    }
}
