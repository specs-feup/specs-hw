package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.trace.InstructionWindow;

public abstract class AFixedSizeFrequentSequenceDetector extends ASimpleSegmentDetector {

    /*
     * Constructor
     */
    public AFixedSizeFrequentSequenceDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Check if candidate sequence is valid
     */
    protected Boolean validSequence(InstructionWindow window) {
        return super.validSequence(window);
        // TODO: add stuff?
    }
}
