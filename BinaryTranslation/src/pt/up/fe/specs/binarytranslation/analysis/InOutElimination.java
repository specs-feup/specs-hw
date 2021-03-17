package pt.up.fe.specs.binarytranslation.analysis;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.inouts.SimpleBasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class InOutElimination {

    private BinarySegment bb;
    private List<Instruction> insts;

    public InOutElimination(BinarySegment bb, List<Instruction> insts) {
        this.bb = bb;
        this.insts = insts;
    }

    public void eliminate(int windowSize) {
        var occur = findBBOccurrences(bb);
        var sbbio = new SimpleBasicBlockInOuts(bb);
        sbbio.calculateInOuts();
        var inouts = sbbio.getInouts();
        
        for (var o : occur.getOccurrences()) {
            var elim = new InOutOccurrenceElimination(o, occur, insts, inouts);
            elim.eliminate(windowSize);
        }
    }

    public BasicBlockOccurrenceTracker findBBOccurrences(BinarySegment bb) {
        ArrayList<Integer> occur = new ArrayList<>();

        long start = bb.getInstructions().get(0).getAddress();
        long end = bb.getInstructions().get(bb.getInstructions().size() - 1).getAddress();
        boolean expectingEnd = false;
        for (int i = 0; i < insts.size(); i++) {
            Instruction inst = insts.get(i);
            if (inst.getAddress() == start && !expectingEnd) {
                expectingEnd = true;
            }
            if (inst.getAddress() == end && expectingEnd) {
                expectingEnd = false;
                occur.add(i);
            }
        }
        return new BasicBlockOccurrenceTracker(bb, occur);
    }
}
