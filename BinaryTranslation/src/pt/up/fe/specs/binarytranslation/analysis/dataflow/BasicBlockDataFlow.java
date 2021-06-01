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

package pt.up.fe.specs.binarytranslation.analysis.dataflow;

import java.util.ArrayList;
import java.util.List;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class BasicBlockDataFlow extends ASegmentDataFlow {

    public BasicBlockDataFlow(BasicBlockOccurrenceTracker tracker) {
        super(getTransformedBasicBlock(tracker));
    }

    private static List<Instruction> getTransformedBasicBlock(BasicBlockOccurrenceTracker tracker) {
        var<Instruction> newBB = new ArrayList<Instruction>();
        var<Instruction> oldBB = tracker.getBasicBlock().getInstructions();
        var size = oldBB.size();

        newBB.add(oldBB.get(size - 1));
        newBB.addAll(oldBB.subList(0, size - 1));
        return newBB;
    }
}
