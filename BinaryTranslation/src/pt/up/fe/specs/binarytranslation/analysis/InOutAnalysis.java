package pt.up.fe.specs.binarytranslation.analysis;

import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.inouts.BasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.inouts.SimpleBasicBlockInOuts;
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
    
    public void analyse(InOutMode mode) {
        SegmentBundle bun = det.detectSegments(stream);
        if (bun.getSegments().size() == 0) {
            System.out.println("No basic blocks were detected");
            return;
        }
        
        System.out.println(bun.getSummary());
        List<BinarySegment> bbs = bun.getSegments();
        
        if (mode == InOutMode.TRACE) {
            List<Instruction> insts = det.getProcessedInsts();
            System.out.println(insts.size() + " instructions detected");
            TraceInOuts inouts = new TraceInOuts(insts, bbs);
            inouts.calculateInOuts();
            
            inouts.printUseDefRegisters();
            inouts.printSequenceInOuts();
        }
        if (mode == InOutMode.BASIC_BLOCK) {
            for (BinarySegment bb : bbs) {
                BasicBlockInOuts inouts = new BasicBlockInOuts(bb);
                inouts.calculateInOuts();

                inouts.printUseDefRegisters();
                inouts.printSequenceInOuts();
                inouts.printResult();
            }
        }
        if (mode == InOutMode.SIMPLE_BASIC_BLOCK) {
            for (BinarySegment bb : bbs) {
                SimpleBasicBlockInOuts inouts = new SimpleBasicBlockInOuts(bb);
                inouts.calculateInOuts();
                inouts.printResult();
                
            }
        }
        if (mode == InOutMode.ELIMINATION) {
            for (BinarySegment bb : bbs) {
                System.out.println("Running elimination analysis for detected Basic Block");
                bb.printSegment();
                System.out.println("");
                var elim = new InOutElimination(bb, det.getProcessedInsts());
                elim.eliminate(15);
            }
        }
    }
}
