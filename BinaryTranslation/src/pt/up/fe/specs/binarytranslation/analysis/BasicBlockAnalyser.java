package pt.up.fe.specs.binarytranslation.analysis;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class BasicBlockAnalyser {
    
    private ATraceInstructionStream stream;
    private TraceBasicBlockDetector det;

    public BasicBlockAnalyser(ATraceInstructionStream stream) {
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
        seg.printSegment();
    }
}
