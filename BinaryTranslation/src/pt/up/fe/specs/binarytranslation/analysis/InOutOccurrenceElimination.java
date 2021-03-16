package pt.up.fe.specs.binarytranslation.analysis;

import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.inouts.InstructionSets;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class InOutOccurrenceElimination {

    private List<Instruction> insts;
    private BinarySegment bb;
    private Integer idx;
    private InstructionSets inouts;

    public InOutOccurrenceElimination(BinarySegment bb, Integer idx, List<Instruction> insts, InstructionSets inouts) {
        this.bb = bb;
        this.idx = idx;
        this.insts = insts;
        this.inouts = inouts;
    }

    public void eliminate(int window) {
        System.out.println("Eliminating occurrence " + idx + " of basic block " + bb.getUniqueId());
        System.out.println("Using a window of " + window + " instructions");
    }

}
