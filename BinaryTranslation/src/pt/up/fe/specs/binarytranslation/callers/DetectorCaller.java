package pt.up.fe.specs.binarytranslation.callers;

import java.util.concurrent.Callable;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;

public class DetectorCaller implements Callable<SegmentBundle> {

    private final SegmentDetector detector;

    public DetectorCaller(SegmentDetector detector) {
        this.detector = detector;
    }

    @Override
    public SegmentBundle call() throws Exception {
        return this.detector.detectSegments();
    }
}
