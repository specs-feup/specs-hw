package pt.up.fe.specs.binarytranslation.analysis.inouts.elimination;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.inouts.SimpleBasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class OutElimination {

    private BinarySegment bb;
    private List<Instruction> insts;

    public OutElimination(BinarySegment bb, List<Instruction> insts) {
        this.bb = bb;
        this.insts = insts;
    }

    public void eliminate(int windowSize) {
        var occur = new BasicBlockOccurrenceTracker(bb, insts);
        var sbbio = new SimpleBasicBlockInOuts(bb);
        sbbio.calculateInOuts();
        var inouts = sbbio.getInouts();

        for (var o : occur.getOccurrences()) {
            var elim = new OutEliminationOccurrence(o, occur, insts, inouts);
            var elimList = elim.eliminate(windowSize);
            printOccurrenceResult(elimList, windowSize);
        }
    }
    
    private void printOccurrenceResult(ArrayList<String> regs, int window) {
        var sb = new StringBuilder();
        sb.append(String.join(",", regs));
        sb.append("; window=").append(window);
        System.out.println(sb.toString());
    }
}
