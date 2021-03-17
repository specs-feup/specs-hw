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
        var occur = new BasicBlockOccurrenceTracker(bb, insts);
        var sbbio = new SimpleBasicBlockInOuts(bb);
        sbbio.calculateInOuts();
        var inouts = sbbio.getInouts();
        
        for (var o : occur.getOccurrences()) {
            var elim = new InOutOccurrenceElimination(o, occur, insts, inouts);
            elim.eliminate(windowSize);
        }
    }
}
