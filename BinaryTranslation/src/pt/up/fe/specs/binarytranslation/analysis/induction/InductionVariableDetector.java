package pt.up.fe.specs.binarytranslation.analysis.induction;

import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class InductionVariableDetector {
    private BasicBlockOccurrenceTracker tracker;

    public InductionVariableDetector(BinarySegment bb, List<Instruction> insts) {
        this.tracker = new BasicBlockOccurrenceTracker(bb, insts);
    }
    
    public void printOccurrenceRegisters() {
        for (var o : tracker.getOccurrences()) {
            var regs = o.getRegisters();
            regs.prettyPrint();
            AnalysisUtils.printSeparator(40);
        }
    }
}
