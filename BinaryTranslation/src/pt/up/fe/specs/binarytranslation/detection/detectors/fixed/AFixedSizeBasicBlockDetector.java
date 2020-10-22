package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.trace.InstructionWindow;

/**
 * Basic block detector which only detects blocks of a specific size
 * 
 * @author nuno
 *
 */
public abstract class AFixedSizeBasicBlockDetector extends ASimpleSegmentDetector {

    /*
     * Constructor
     */
    public AFixedSizeBasicBlockDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Check if candidate sequence is valid
     */
    @Override
    protected Boolean validSequence(InstructionWindow window) {

        // check precendent requirements first!
        if (super.validSequence(window) == false)
            return false;

        // must obey these conditions
        var last = window.getLast();
        if (!(last.isBackwardsJump() && last.isConditionalJump() && last.isRelativeJump()))
            return false;

        // target isn't start of window, skip this candidate
        int targetAddr = last.getBranchTarget().intValue(); // TODO what if branch is based on register values?
        int firstAddr = window.get(0).getAddress().intValue();
        if (targetAddr != firstAddr)
            return false;

        return true;
    }
}
