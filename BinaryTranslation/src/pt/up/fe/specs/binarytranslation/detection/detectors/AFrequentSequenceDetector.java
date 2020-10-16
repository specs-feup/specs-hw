package pt.up.fe.specs.binarytranslation.detection.detectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected AFrequentSequenceDetector() {
        super(DetectorConfiguration.defaultConfig());
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
            var newseq = BinarySegmentDetectionUtils.hashSequence(candidate);
            newsequences.add(newseq);
        }

        return newsequences;
    }

    /*
     * For all valid hashcodes, make the symbolic sequence and its in/out contexts
     
    private List<BinarySegment> makeFrequentSequences(Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {
    
        //The list this detector will construct         
        List<BinarySegment> allsequences = new ArrayList<BinarySegment>();
    
        // all start addrs grouped by hashcode
        Iterator<Integer> it = addrs.keySet().iterator();
    
        // for each hashcode
        while (it.hasNext()) {
    
            // get hashcode
            var hashcode = it.next();
    
            // get all start addrs of all sequences with this hashcode
            var addrlist = addrs.get(hashcode);
    
            // get a list of the sequences by their hashcode_startaddr key
            var seqlist = new ArrayList<HashedSequence>();
            for (Integer startaddr : addrlist) {
                var keyval = hashcode.toString() + "_" + Integer.toString(startaddr);
                seqlist.add(hashed.get(keyval));
            }
    
            // use first sequence with this hash code to create symbolic sequence
            var symbolicseq = seqlist.get(0).makeSymbolic();
    
            // Create all contexts
            var contexts = new ArrayList<SegmentContext>();
            for (HashedSequence seq : seqlist)
                contexts.add(new SegmentContext(seq));
    
            // make the frequent sequence
            var newseq = this.makeSegment(symbolicseq, contexts);
            allsequences.add(newseq);
        }
    
        return allsequences;
    }*/

    @Override
    public SegmentBundle detectSegments(InstructionStream istream) {

        /*
         * This map holds all hashed sequences for all instruction windows we analyze
         * Map: <hashcode_startaddr, hashedsequence>
         */
        Map<String, HashedSequence> hashed = new HashMap<String, HashedSequence>();

        /*
         * This map holds all hash codes and list of occurring addresses for each
         * Map: <hashcode, list of addresses>
         */
        Map<Integer, List<Integer>> addrs = new HashMap<Integer, List<Integer>>();

        // IMPORTANT
        // TODO: imms can be treated very differently!
        // 1. can either allow them to be all different (not part of the hash)
        // 2. can try to find common imms and leave only those to be literal (i.e., non symbolic)
        // 3. can demand that all are equal in all occurrences of sequence (all are non symbolic and hence all are
        // hardware specialized literals)

        /*
         * 
         */
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
                BinarySegmentDetectionUtils.addAddrToList(addrs, seq);

                // add sequence to map which is indexed by hashCode + startaddr
                BinarySegmentDetectionUtils.addHashSequenceToList(hashed, seq);
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
        BinarySegmentDetectionUtils.removeUnique(addrs, hashed);

        // Make all valid sequences
        var sequences = super.makeSegments(hashed, addrs);

        // finally, init some stats
        SegmentBundle bundle = new SegmentBundle(sequences, istream);
        return bundle;
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
