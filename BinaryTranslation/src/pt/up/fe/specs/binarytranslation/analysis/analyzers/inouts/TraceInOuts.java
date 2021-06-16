package pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts;

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
    public void printSequenceInOuts() {
        for (BinarySegment bb : bbs) {
            long start = bb.getInstructions().get(0).getAddress();
            long end = bb.getInstructions().get(bb.getInstructions().size() - 1).getAddress();
            boolean expectingEnd = false;
            
            System.out.println("Displaying in/outs for all occurrences of basic block");
            System.out.println("-----------------------------------------------------");
            for (int i = 0; i < insts.size(); i++) {
                Instruction inst = insts.get(i);
                if (inst.getAddress() == start && !expectingEnd) {
                    printInstWithSets(i);
                    System.out.println("[basic block body...]");
                    expectingEnd = true;
                }
                if (inst.getAddress() == end && expectingEnd) {
                    printInstWithSets(i);
                    expectingEnd = false;
                    System.out.println("-----------------------------------------------------");
                }
            }
        }
    }

    @Override
    public void printResult() {
        // TODO Auto-generated method stub

    }

}
