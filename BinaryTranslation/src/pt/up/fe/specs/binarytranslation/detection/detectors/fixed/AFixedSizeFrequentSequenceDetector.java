package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.BinarySegmentDetectionUtils;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.detection.trace.InstructionWindow;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class AFixedSizeFrequentSequenceDetector extends ASegmentDetector {

    /*
     * Constructor
     */
    public AFixedSizeFrequentSequenceDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Check if candidate sequence is valid
     */
    private Boolean validSequence(InstructionWindow window) {

        // check if this subsequence is at all apt
        for (Instruction inst : window.getWindow()) {

            // TODO fail with stream instructions

            // do not form sequences with unknown instructions
            // do not form frequent sequences containing jumps
            if (inst.isUnknown() || inst.isJump()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        var window = new InstructionWindow(this.getConfig().getMaxsize());

        // make 1st window
        while (!window.isFull())
            window.add(istream.nextInstruction());

        // process entire stream
        do {

            // adjust to delay of last instruction in window
            var delay = window.getLast().getDelay();
            while (delay-- > 0)
                window.add(istream.nextInstruction());

            // discard candidate?
            if (validSequence(window)) {

                // create new candidate hash sequence
                var newseq = BinarySegmentDetectionUtils.hashSequence(window.getWindow());

                // add sequence to occurrence counters (counting varies between static to trace detection)
                BinarySegmentDetectionUtils.addAddrToList(addrs, newseq);

                // add sequence to map which is indexed by hashCode + startaddr
                BinarySegmentDetectionUtils.addHashSequenceToList(hashed, newseq);
            }

            // shift window by 1
            window.add(istream.nextInstruction());

        } while (istream.hasNext());

        // Remove all sequences which only happen once
        BinarySegmentDetectionUtils.removeUnique(addrs, hashed);
    }
}
