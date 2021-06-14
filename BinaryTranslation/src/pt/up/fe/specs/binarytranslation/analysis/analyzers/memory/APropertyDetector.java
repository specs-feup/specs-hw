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
