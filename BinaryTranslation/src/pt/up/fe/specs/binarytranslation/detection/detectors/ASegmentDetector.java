package pt.up.fe.specs.binarytranslation.detection.detectors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class ASegmentDetector implements SegmentDetector {

    private InstructionStream currentStream = null;
    private DetectorConfiguration config;

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

    public ASegmentDetector(DetectorConfiguration config) {
        this.config = config;
    }

    public DetectorConfiguration getConfig() {
        return config;
    }

    protected void setCurrentStream(InstructionStream currentStream) {
        this.currentStream = currentStream;
    }

    protected InstructionStream getCurrentStream() {
        return currentStream;
    }

    /*
     * Must be implemented by children
     */
    protected abstract BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts);

    /*
     * For all valid hashcodes, make the symbolic sequence and its in/out contexts
     */
    protected List<BinarySegment> makeSegments(Map<String, HashedSequence> hashed,
            Map<Integer, List<Integer>> addrs) {

        /*
         * The list this detector will construct
         */
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
    }

    /*
    @Override
    public SegmentBundle detectSegments(InstructionStream istream) {
    
        /*
         * This map holds all hashed sequences for all instruction windows we analyze
         * Map: <hashcode_startaddr, hashedsequence>
         
        Map<String, HashedSequence> hashed = new HashMap<String, HashedSequence>();
    
        
         * This map holds all hash codes and list of occurring addresses for each
         * Map: <hashcode, list of addresses>
         
        Map<Integer, List<Integer>> addrs = new HashMap<Integer, List<Integer>>();
    
        List<Instruction> window = new ArrayList<Instruction>();
    
        // process entire stream
        int insertcount = 0;
        Instruction is = null;
        while ((is = istream.nextInstruction()) != null) {
    
            window.add(is);
            insertcount++;
    
            // "is" is a possible backwards branch that could generate a basic block, so, try to hash it
            if (is.isBackwardsJump() && is.isConditionalJump() && is.isRelativeJump()) {
    
                // absorb as many instructions as there are in the delay slot
                var delay = is.getDelay();
                while (delay-- > 0) {
                    window.add(istream.nextInstruction());
                    insertcount++;
                }
    
                // try to hash the possible candidate terminated by backwards branch "is"
                checkCandidate(window, is);
            }
    
            // pop one
            while (insertcount > 0 && window.size() > this.getConfig().getMaxsize()) {
                insertcount--;
                window.remove(0);
            }
    
        }
    
        // for all valid hashed sequences, make the StaticBasicBlock objects
        var segments = this.makeSegments(hashed, addrs);
    
        // finally, init some stats
        SegmentBundle bundle = new SegmentBundle(segments, istream);
        return bundle;
    }*/
}
