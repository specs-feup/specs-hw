package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.detection.trace.InstructionWindow;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class ASimpleSegmentDetector extends ASegmentDetector {

    /*
     * 
     */
    public ASimpleSegmentDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * Check if candidate sequence is valid
     */
    protected Boolean validSequence(InstructionWindow window) {

        // cant end on an incomplete "atomic" instruction
        if (window.getLast().getDelay() > 0)
            return false;

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
        while (true) {

            // discard candidate?
            if (validSequence(window)) {

                // create new candidate hash sequence
                var newseq = super.hashSequence(window.getWindow());

                // add sequence to occurrence counters (counting varies between static to trace detection)
                super.addAddrToList(addrs, newseq);

                // add sequence to map which is indexed by hashCode + startaddr
                super.addHashSequenceToList(hashed, newseq);
            }

            // done
            if (!istream.hasNext())
                break;

            // shift window by 1
            else
                window.add(istream.nextInstruction());
        }

        // Remove all sequences which only happen once
        super.removeUnique(addrs, hashed);
    }
}
