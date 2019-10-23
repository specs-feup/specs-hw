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
public class FrequentSequence extends ABinarySegment {

    private SegmentType segtype = SegmentType.STATIC_FREQUENT;
    private List<Integer> startAddresses;

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    private FrequentSequence(List<Instruction> ilist) {
        super();
        this.instlist = ilist;
        buildLiveInsAndLiveOuts();
    }

    /*
     * Constructor builds the sequence on the spot with an existing list
     */
    public FrequentSequence(List<Instruction> ilist, List<Integer> startAddresses) {
        this(ilist);
        this.startAddresses = startAddresses;
    }

    @Override
    public void printSegment() {
        List<String> hexes = new ArrayList<String>();
        for (Integer addr : this.startAddresses)
            hexes.add("0x" + Integer.toHexString(addr));

        System.out.print("Sequence occurs at=" + hexes + "\n");
        for (Instruction inst : this.instlist) {
            inst.printInstruction();
        }
    }
}
