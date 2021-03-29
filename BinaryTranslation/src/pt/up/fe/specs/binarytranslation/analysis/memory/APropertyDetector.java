package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public abstract class APropertyDetector {
    protected BasicBlockOccurrenceTracker tracker;

    public APropertyDetector(BinarySegment bb, List<Instruction> insts) {
        this.tracker = new BasicBlockOccurrenceTracker(bb, insts);
    }
}
