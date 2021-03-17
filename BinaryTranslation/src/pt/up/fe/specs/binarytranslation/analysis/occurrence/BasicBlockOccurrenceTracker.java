package pt.up.fe.specs.binarytranslation.analysis.occurrence;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;

public class BasicBlockOccurrenceTracker {
    
    private BinarySegment bb;
    private ArrayList<BasicBlockOccurrence> occurrences;

    public BasicBlockOccurrenceTracker(BinarySegment bb, ArrayList<Integer> positions) {
        this.bb = bb;
        this.occurrences = new ArrayList<>();
        int id = 0;
        for (Integer i : positions) {
            var o = new BasicBlockOccurrence(id, bb, i);
            occurrences.add(o);
            id++;
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
