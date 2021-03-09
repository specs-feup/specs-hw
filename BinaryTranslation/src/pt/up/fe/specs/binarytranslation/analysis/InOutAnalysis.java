package pt.up.fe.specs.binarytranslation.analysis;

import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.inouts.BasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.inouts.TraceInOuts;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class InOutAnalysis {
    
    private ATraceInstructionStream stream;
    private TraceBasicBlockDetector det;

    public InOutAnalysis(ATraceInstructionStream stream) {
        this.stream = stream;
        this.det = new TraceBasicBlockDetector();
    }
    
    public void analyse(boolean complete) {
        SegmentBundle bun = det.detectSegments(stream);
        if (bun.getSegments().size() == 0) {
            System.out.println("No basic blocks were detected");
            return;
        }
        
        System.out.println(bun.getSummary());
        List<BinarySegment> bbs = bun.getSegments();
        
        if (complete) {
            List<Instruction> insts = det.getProcessedInsts();
            System.out.println(insts.size() + " instructions detected");
            TraceInOuts inouts = new TraceInOuts(insts, bbs);
            inouts.calculateInOuts();
            inouts.printSequenceInOuts();
        } else {
            for (BinarySegment bb : bbs) {
                BasicBlockInOuts inouts = new BasicBlockInOuts(bb);
                inouts.calculateInOuts();
                inouts.printSequenceInOuts();
                inouts.printResult();
            }
        }
    }
}
