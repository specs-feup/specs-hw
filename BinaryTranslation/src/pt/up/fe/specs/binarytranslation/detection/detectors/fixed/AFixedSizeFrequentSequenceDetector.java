package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.tracer.InstructionWindow;

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
    @Override
    protected Boolean validSequence(InstructionWindow window) {

        // check precendent requirements first!
        if (super.validSequence(window) == false)
            return false;

        // check if this subsequence is at all apt
        for (Instruction inst : window.getWindow()) {

            // TODO fail with stream instructions

            // do not form sequences with unknown instructions
            // do not form frequent sequences containing jumps
            // TODO: TEMPORARY RESTRICTION ON MEMORY AND FLOAT FOR HDL GENERATION
            if (inst.isUnknown() || inst.isJump() || inst.isMemory() || inst.isFloat()) {
                return false;
            }
        }

        return true;
    }
}
