package pt.up.fe.specs.binarytranslation.analysis;

import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.induction.InductionVariableDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class InductionVariableAnalyzer extends ATraceAnalyzer {

    public InductionVariableAnalyzer(ATraceInstructionStream stream) {
        super(stream);
    }

    public void analyze() {
        var det = new TraceBasicBlockDetector();
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<Instruction> insts = det.getProcessedInsts();
        
        for (var bb : segs) {
            bb.printSegment();
            var ivd = new InductionVariableDetector(bb, insts);
            //ivd.printDifferences();
            ivd.findAddressRegisterChain();
        }
    }
}
