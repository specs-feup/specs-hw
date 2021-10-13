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
 
package pt.up.fe.specs.binarytranslation.analysis.analyzers.ocurrence;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class BasicBlockOccurrenceTracker {
    
    private BinarySegment bb;
    private ArrayList<BasicBlockOccurrence> occurrences;
    private List<Instruction> trace;
    
    public BasicBlockOccurrenceTracker(BinarySegment bb, List<Instruction> insts) {
        this.bb = bb;
        this.occurrences = new ArrayList<>();
        this.trace = insts;
        
        int id = 0;
        long start = bb.getInstructions().get(0).getAddress();
        long end = bb.getInstructions().get(bb.getInstructions().size() - 1).getAddress();
        boolean expectingEnd = false;
        int newIdx = 0;
        
        for (int i = 0; i < insts.size(); i++) {
            Instruction inst = insts.get(i);
            if (inst.getAddress() == start && !expectingEnd) {
                expectingEnd = true;
                newIdx = i;
            }
            if (inst.getAddress() == end && expectingEnd) {
                expectingEnd = false;
                var subList = trace.subList(newIdx, i);
                var newOccurrence = new BasicBlockOccurrence(id, bb, newIdx, subList);
                occurrences.add(newOccurrence);
                id++;
            }
        }
    }

    public BinarySegment getBasicBlock() {
        return bb;
    }

    public ArrayList<BasicBlockOccurrence> getOccurrences() {
        return occurrences;
    }
    
    public BasicBlockOccurrence basicBlockFromPosition(int instPos) {
        for (var o : occurrences) {
            if (o.belongsToOccurrence(instPos))
                return o;
        }
        return null;
    }

    public List<Instruction> getTrace() {
        return trace;
    }
    
    public List<Instruction> getBasicBlockInsts() {
        return bb.getInstructions();
    }

    public ArrayList<String> getRegisters() {
        return AnalysisUtils.findAllRegistersOfSeq(getBasicBlockInsts());
    }
}
