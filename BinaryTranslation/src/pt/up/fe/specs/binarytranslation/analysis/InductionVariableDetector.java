package pt.up.fe.specs.binarytranslation.analysis;

import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;

public class InductionVariableDetector {
    private BasicBlockOccurrenceTracker tracker;

    public InductionVariableDetector(BasicBlockOccurrenceTracker tracker) {
        this.tracker = tracker;
    }
    
    public void printOccurrenceRegisters() {
        for (var o : tracker.getOccurrences()) {
            var regs = o.getRegisters();
        }
    }
}
