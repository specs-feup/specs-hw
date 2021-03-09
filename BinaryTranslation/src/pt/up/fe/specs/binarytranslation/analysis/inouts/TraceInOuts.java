package pt.up.fe.specs.binarytranslation.analysis.inouts;

import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceInOuts extends ASequenceInOuts {

    private List<BinarySegment> bbs;

    public TraceInOuts(List<Instruction> insts, List<BinarySegment> bbs) {
        super(insts);
        this.bbs = bbs;
    }

    @Override
    public void printResult() {
        // TODO Auto-generated method stub

    }

}
