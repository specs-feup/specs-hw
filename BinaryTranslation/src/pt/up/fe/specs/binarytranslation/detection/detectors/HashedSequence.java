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

package pt.up.fe.specs.binarytranslation.detection.detectors;

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
    private int ocurrences;
    private List<SimpleInstruction> instlist;
    private Map<String, String> regremap;

    public HashedSequence(int hashcode, List<Instruction> instlist, Map<String, String> regremap) {

        this.hashcode = hashcode;
        this.regremap = regremap;
        this.ocurrences = 1;
        this.startaddr = instlist.get(0).getAddress();

        // save only instruction as hex, and addr too
        this.instlist = new ArrayList<SimpleInstruction>();
        for (Instruction i : instlist) {
            this.instlist.add(new SimpleInstruction(i));
        }
    }

    public void incrementOccurences() {
        this.ocurrences++;
    }

    public int getOcurrences() {
        return ocurrences;
    }

    public int getHashcode() {
        return hashcode;
    }

    public Integer getStartAddress() {
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
        for (SimpleInstruction i : this.instlist) {
            var inst = i.rebuild();
            rebuiltI.add(inst);
        }

        // Symbolify
        Integer addr = 0;
        for (Instruction i : rebuiltI) {

            try {
                i.makeSymbolic(addr, this.regremap);
                // TODO: use a future SymbolicInstruction class, where
                // the "String instruction" field can be taken from
                // the InstructionEncoding enum?

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            addr += 4;
            // TODO Fix this width...
        }

        return rebuiltI;
    }
}
