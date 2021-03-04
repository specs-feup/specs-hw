package pt.up.fe.specs.binarytranslation.analysis.basicblock;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class InOutAnalysis {
    
    private ATraceInstructionStream stream;
    private TraceBasicBlockDetector det;

    public InOutAnalysis(ATraceInstructionStream stream) {
        this.stream = stream;
        this.det = new TraceBasicBlockDetector();
    }
    
    public void analyse() {
        SegmentBundle bun = det.detectSegments(stream);
        System.out.println(bun.getSummary());
        for (var seg : bun.getSegments()) {
            processSegment(seg);
        }
    }

    private void processSegment(BinarySegment seg) {
        var bbio = new BasicBlockInOuts(seg);
        bbio.calculateInOuts();
    }
}
