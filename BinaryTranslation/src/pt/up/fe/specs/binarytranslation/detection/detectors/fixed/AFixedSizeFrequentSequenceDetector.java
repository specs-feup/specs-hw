package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.BinarySegmentDetectionUtils;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
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
    private Boolean validSequence(List<Instruction> insts) {

        // check if this subsequence is at all apt
        for (Instruction inst : insts) {

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

        List<Instruction> window = new ArrayList<Instruction>();

        // make 1st window
        for (int i = 0; i < this.getConfig().getMaxsize(); i++)
            window.add(istream.nextInstruction());

        // process entire stream
        do {

            // adjust to delay of last instruction in window
            var delay = window.get(window.size() - 1).getDelay();
            if (delay > 0) {
                window.remove(0);
                window.add(istream.nextInstruction());
            }

            // discard candidate?
            if (validSequence(window)) {

                // create new candidate hash sequence
                var newseq = BinarySegmentDetectionUtils.hashSequence(window);

                // add sequence to occurrence counters (counting varies between static to trace detection)
                BinarySegmentDetectionUtils.addAddrToList(addrs, newseq);

                // add sequence to map which is indexed by hashCode + startaddr
                BinarySegmentDetectionUtils.addHashSequenceToList(hashed, newseq);
            }

            // shift window by 1
            window.remove(0);
            window.add(istream.nextInstruction());

        } while (istream.hasNext());

        // Remove all sequences which only happen once
        BinarySegmentDetectionUtils.removeUnique(addrs, hashed);
    }
}
