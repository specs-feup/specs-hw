package pt.up.fe.specs.binarytranslation.flow.tree;

import java.lang.reflect.Constructor;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class BinaryTranslationDetectStep extends BinaryTranslationStep {

    private Class<?> detectorClass;
    private SegmentDetector detector;
    private SegmentBundle bundle;

    public BinaryTranslationDetectStep(Class<?> detectorClass) {
        this.detectorClass = detectorClass;
    }

    @Override
    public void execute() {

        /*
         * Get the istream from parent node (?)
         */
        InstructionStream istream = null;
        var parent = this.getParent();
        if (parent instanceof BinaryTranslationStreamOpen) {
            istream = ((BinaryTranslationStreamOpen) parent).getStream();

            // TODO fix! create new BinaryTranslationRunException (?)
        } else {
            throw new RuntimeException();
        }

        /*
         * Construct the detector
         */
        Constructor<?> consDetector;
        try {
            consDetector = detectorClass.getConstructor(InstructionStream.class);
            this.detector = (SegmentDetector) consDetector.newInstance(istream);

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        /*
         * Execute it
         */
        this.bundle = detector.detectSegments();
    }

    public SegmentBundle getBundle() {
        return this.bundle;
    }
}
