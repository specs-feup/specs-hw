package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * Detects frequent instruction sequences from either static binary or instruction traces
 * 
 * @author nuno
 *
 */
public abstract class AFrequentSequenceDetector implements SegmentDetector {

    /*
     * The list this detector will construct
     */
    private List<BinarySegment> allsequences = null;

    /*
     * An open instruction stream, either from elf dump, or simulator
     */
    private InstructionStream istream;

    /*
     * Stuff for statistics (TODO: add more) 
     */
    // protected long totalCycles;
    // protected long numInsts;
    // protected int totalMemoryInsts;
    // protected int totalLoads;
    // protected int totalStores;
    // protected int totalBranchInsts;
    // protected int totalTakenBranches;
    // protected int totalNonTakenBranches;

    /*
     * min and max size of windows 
     */
    protected final int minsize = 4;
    protected final int maxsize = 20;

    /*
     * This map holds all hashed sequences for all instruction windows we analyze
     * Map: <hashcode_startaddr, hashedsequence>
     */
    protected Map<String, HashedSequence> hashed = new HashMap<String, HashedSequence>();

    /*
     * This map holds all hash codes and list of occurring addresses for each
     * Map: <hashcode, list of addresses>
     */
    private Map<Integer, List<Integer>> addrs = new HashMap<Integer, List<Integer>>();

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    protected AFrequentSequenceDetector(InstructionStream istream) {
        this.istream = istream;
    }

    /*
     * Must be implemented by children
     */
    protected abstract BinarySegment makeFrequentSequence(List<Instruction> symbolicseq, List<SegmentContext> contexts);

    /*
     * Check if candidate sequence is valid
     */
    private Boolean validSequence(List<Instruction> insts) {

        // check if this subsequence is at all apt
        for (Instruction inst : insts) {

            // TODO fail with stream instructions

            // do not form sequences with unknown instructions
            // do not form frequent sequences containing jumps
            // do not form frequent sequences memory accesses
            // if (inst.isUnknown() || inst.isJump() || inst.isMemory()) {
            // return false;
            // }

            if (inst.isUnknown() || inst.isJump()) {
                return false;
            }
        }

        return true;
    }

    /*
     * For the given window "w", builds all hash sequences from
     * sizes minsize to maxsize
     * Constructs a map with <instruction addr, sequence hash>
     */
    private void hashSequences(List<Instruction> w) {

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

            // add sequence to occurrence counters (counting varies between static to trace detection)
            BinarySegmentDetectionUtils.addAddrToList(this.addrs, newseq);

            // add sequence to map which is indexed by hashCode + startaddr
            BinarySegmentDetectionUtils.addHashSequenceToList(this.hashed, newseq);
        }

        return;
    }

    /*
     * For all valid hashcodes, make the symbolic sequence and its in/out contexts
     */
    private void makeFrequentSequences() {

        // for all sequences which occur more than once, symbolify and add to output
        this.allsequences = new ArrayList<BinarySegment>();

        // all start addrs grouped by hashcode
        Iterator<Integer> it = this.addrs.keySet().iterator();

        // for each hashcode
        while (it.hasNext()) {

            // get hashcode
            var hashcode = it.next();

            // get all start addrs of all sequences with this hashcode
            var addrlist = this.addrs.get(hashcode);

            // get a list of the sequences by their hashcode_startaddr key
            var seqlist = new ArrayList<HashedSequence>();
            for (Integer startaddr : addrlist) {
                var keyval = hashcode.toString() + "_" + Integer.toString(startaddr);
                seqlist.add(this.hashed.get(keyval));
            }

            // use first sequence with this hash code to create symbolic sequence
            var symbolicseq = seqlist.get(0).makeSymbolic();

            // Create all contexts
            var contexts = new ArrayList<SegmentContext>();
            for (HashedSequence seq : seqlist)
                contexts.add(new SegmentContext(seq));

            // make the frequent sequence
            var newseq = makeFrequentSequence(symbolicseq, contexts);
            newseq.setAppName(this.istream.getApplicationName());
            newseq.setCompilationFlags(this.istream.getCompilationInfo());
            this.allsequences.add(newseq);
        }
    }

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO return an exception or something else
        if (this.allsequences != null)
            return null;

        // IMPORTANT
        // TODO: imms can be treated very differently!
        // 1. can either allow them to be all different (not part of the hash)
        // 2. can try to find common imms and leave only those to be literal (i.e., non symbolic)
        // 3. can demand that all are equal in all occurrences of sequence (all are non symbolic and hence all are
        // hardware specialized literals)

        List<Instruction> window = new ArrayList<Instruction>();

        // make 1st window
        for (int i = 0; i < this.maxsize; i++)
            window.add(this.istream.nextInstruction());

        // process entire stream
        do {
            // sequences in this window, from sizes minsize to maxsize
            hashSequences(window);

            // shift window (i.e. new window)
            int i = 0;
            int atomicity = window.get(0).getDelay();
            for (i = 0; i < this.maxsize - 1 - atomicity; i++)
                window.set(i, window.get(i + 1 + atomicity));

            // new instructions in window
            for (; i < this.maxsize; i++)
                window.set(i, this.istream.nextInstruction());

        } while (istream.hasNext());

        // Remove all sequences which only happen once
        BinarySegmentDetectionUtils.removeUnique(this.addrs, this.hashed);

        // Make all valid sequences
        makeFrequentSequences();

        // finally, init some stats
        // this.totalCycles = istream.getCycles();
        // this.numInsts = istream.getNumInstructions();

        return this.allsequences;
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

    @Override
    public void close() throws Exception {
        istream.close();
    }
}
