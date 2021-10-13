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
 
package pt.up.fe.specs.binarytranslation.analysis.analyzers.memory;

import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.ocurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public abstract class APropertyDetector {
    public BasicBlockOccurrenceTracker tracker;

    public APropertyDetector(BinarySegment bb, List<Instruction> insts) {
        this.tracker = new BasicBlockOccurrenceTracker(bb, insts);
    }
    
    public APropertyDetector(BasicBlockOccurrenceTracker tracker) {
        this.tracker = tracker;
    }

    public BasicBlockOccurrenceTracker getTracker() {
        return tracker;
    }

    public void setTracker(BasicBlockOccurrenceTracker tracker) {
        this.tracker = tracker;
    }
}
