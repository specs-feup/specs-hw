package pt.up.fe.specs.binarytranslation.detection.detectors.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.detectors.ASegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.HashedSequence;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * Detects frequent instruction sequences from either static binary or instruction traces
 * 
 * @author nuno
 *
 */
public abstract class AFrequentSequenceDetector extends ASegmentDetector {

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    protected AFrequentSequenceDetector(DetectorConfiguration config) {
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

    /*
     * For the given window "w", builds all hash sequences from sizes minsize to maxsize
     */
    private List<HashedSequence> hashSequences(List<Instruction> w) {

        var newsequences = new ArrayList<HashedSequence>();

        var maxsize = this.getConfig().getMaxsize();
        var minsize = this.getConfig().getMinsize();

        // compute all hashes for this window, for all sequence sizes
        for (int i = minsize; i <= maxsize; i++) {

            // sub-window
            List<Instruction> candidate = w.subList(0, i);

            // if last inst has any kind of delay, must discard this subcandidate
            // this delays with MicroBlaze delay branch instructions (for now)
            var delay = candidate.get(candidate.size() - 1).getDelay();
            if (delay > 0) {
                i += delay - 1;
                continue;
            }

            // discard candidate?
            if (!validSequence(candidate))
                continue;

            // create new candidate hash sequence
            var newseq = super.hashSequence(candidate);
            newsequences.add(newseq);
        }

        return newsequences;
    }

    /*
     * 
     */
    @Override
    public void processStream(InstructionStream istream, Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        List<Instruction> window = new ArrayList<Instruction>();

        var maxsize = this.getConfig().getMaxsize();

        // make 1st window
        for (int i = 0; i < maxsize; i++)
            window.add(istream.nextInstruction());

        // process entire stream
        do {
            // sequences in this window, from sizes minsize to maxsize
            for (var seq : hashSequences(window)) {

                // add sequence to occurrence counters (counting varies between static to trace detection)
                super.addAddrToList(addrs, seq);

                // add sequence to map which is indexed by hashCode + startaddr
                super.addHashSequenceToList(hashed, seq);
            }

            // shift window (i.e. new window)
            int i = 0;
            int atomicity = window.get(0).getDelay();
            for (i = 0; i < maxsize - 1 - atomicity; i++)
                window.set(i, window.get(i + 1 + atomicity));

            // new instructions in window
            for (; i < maxsize; i++)
                window.set(i, istream.nextInstruction());

        } while (istream.hasNext());

        // Remove all sequences which only happen once
        super.removeUnique(addrs, hashed);
    }

    /*
     * For static: Return the percentage of the ELF code the segments detected represent
     * For trace: Return the percentage of the executed instructions the segments detected represent
     * NOTE: only considers segments of size "segmentSize", to avoid overlap
     */
    /*@Override
        public float getCoverage(int segmentSize) {
        Integer detectedportion = 0;
        for (BinarySegment seg : this.allsequences) {
            if (seg.getSegmentLength() == segmentSize)
                detectedportion += seg.getExecutionCycles();
        }
        return (float) detectedportion / this.totalCycles;
    }*/
    // NOTE: moved this method to {@ SegmentBundle}
}
