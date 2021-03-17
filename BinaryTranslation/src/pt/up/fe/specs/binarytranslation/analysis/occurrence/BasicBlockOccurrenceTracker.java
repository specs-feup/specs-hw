package pt.up.fe.specs.binarytranslation.analysis.occurrence;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class BasicBlockOccurrenceTracker {
    
    private BinarySegment bb;
    private ArrayList<BasicBlockOccurrence> occurrences;
    
    public BasicBlockOccurrenceTracker(BinarySegment bb, List<Instruction> insts) {
        this.bb = bb;
        this.occurrences = new ArrayList<>();
        
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
                var newOccurrence = new BasicBlockOccurrence(id, bb, newIdx);
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
}
