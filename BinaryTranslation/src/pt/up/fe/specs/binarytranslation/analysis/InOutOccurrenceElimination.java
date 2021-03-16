package pt.up.fe.specs.binarytranslation.analysis;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.inouts.ASequenceInOuts;
import pt.up.fe.specs.binarytranslation.analysis.inouts.InstructionSets;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class InOutOccurrenceElimination {

    private List<Instruction> insts;
    private BinarySegment bb;
    private Integer idx;
    private InstructionSets inouts;
    private int start;
    private ArrayList<String> outs;

    public InOutOccurrenceElimination(BinarySegment bb, Integer idx, List<Instruction> insts, InstructionSets inouts) {
        this.bb = bb;
        this.idx = idx;
        this.insts = insts;
        this.inouts = inouts;
        this.start = idx + bb.getSegmentLength() + 1;
        this.outs = inouts.setToList(inouts.getOutSet());
    }

    public void eliminate(int window) {
        System.out.println("\nEliminating occurrence " + idx + "-" + start + " of basic block " + bb.getUniqueId());
        System.out.println("Using a window of " + window + " instructions");
        int curr = start;
        for (int i = curr; i < curr + window; i++) {
            var next = insts.get(i);
            for (var op : next.getData().getOperands()) {
                if (op.isRegister() && op.isWrite()) {
                    String regName = ASequenceInOuts.getRegName(op);
                    if (outs.contains(regName))
                        System.out.println("Out register " + regName + " is overwritten before it is used again");
                }
            }       
        }
    }
}
