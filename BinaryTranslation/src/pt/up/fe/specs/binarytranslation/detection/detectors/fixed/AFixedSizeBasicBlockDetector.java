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

/**
 * Basic block detector which only detects blocks of a specific size
 * 
 * @author nuno
 *
 */
public abstract class AFixedSizeBasicBlockDetector extends ASegmentDetector {

    /*
     * Constructor
     */
    public AFixedSizeBasicBlockDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * 
     */
    @Override
    public void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        List<Instruction> window = new ArrayList<Instruction>();

        // make 1st window
        for (int i = 0; i < this.getConfig().getMaxsize() - 1; i++)
            window.add(istream.nextInstruction());

        // process entire stream
        Instruction is = null;
        while ((is = istream.nextInstruction()) != null) {

            // insert one
            window.add(is);

            // "is" is a possible backwards branch that could generate a basic block, so, try to hash it
            if (is.isBackwardsJump() && is.isConditionalJump() && is.isRelativeJump()) {

                // absorb as many instructions as there are in the delay slot
                var delay = is.getDelay();
                while (delay-- > 0) {
                    window.remove(0);
                    window.add(istream.nextInstruction());
                }

                // compute target of backwards branch
                int targetAddr = is.getBranchTarget().intValue();
                int firstAddr = window.get(0).getAddress().intValue();

                // target isn't start of window, skip this candidate
                if (targetAddr != firstAddr)
                    continue;

                // else, create new candidate hash sequence
                var newseq = BinarySegmentDetectionUtils.hashSequence(window);

                // add sequence to occurrence counters
                // (counting varies between static to trace detection)
                BinarySegmentDetectionUtils.addAddrToList(addrs, newseq);

                // add sequence to map which is indexed by hashCode + startaddr
                BinarySegmentDetectionUtils.addHashSequenceToList(hashed, newseq);
            }

            // pop one
            while (window.size() >= this.getConfig().getMaxsize()) {
                window.remove(0);
            }
        }
    }
}
