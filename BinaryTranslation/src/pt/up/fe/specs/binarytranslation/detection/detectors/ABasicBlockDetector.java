package pt.up.fe.specs.binarytranslation.detection.detectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author Nuno
 *
 */
public abstract class ABasicBlockDetector extends ASegmentDetector {

    /*
     * Stuff for statistics (TODO: add more) TODO: move to abstract ABinarySegment 
     */
    // protected long totalCycles;
    // protected long numInsts;

    /*
     * max size of window for capture of basicblock 
     */
    private final int maxsize = 40;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public ABasicBlockDetector() {
        super(DetectorConfiguration.defaultConfig());
    }

    /*
     * Basic block can only have one branch back, and zero branches forward
     */
    protected boolean validSequence(List<Instruction> candidate) {

        int numbranches = 0;
        for (Instruction i : candidate) {

            if (i.isJump())
                numbranches++;

            if (i.isForwardsJump())
                return false;
        }

        return (numbranches > 1) ? false : true;
    }

    /*
     * For a window of this.maxsize, check for valid candidate, and return it
     */
    private List<Instruction> checkCandidate(List<Instruction> window, Instruction backbranch) {

        // try to find start of basic block in window, and its index
        int sidx = window.size() - 1;
        Instruction target = null;
        int startAddr = backbranch.getBranchTarget().intValue();

        try {
            while (sidx >= 0 && target == null) {
                var is = window.get(sidx);
                if (is.getAddress().intValue() == startAddr)
                    target = is;
                sidx--;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        sidx += 1;

        // wasn't there
        if (target == null)
            return null;

        // find index of branch instruction
        int eidx = 0;
        while (window.get(eidx) != backbranch) {
            eidx++;
        }
        eidx += 1;

        // sub-window
        List<Instruction> candidate = window.subList(sidx, eidx + backbranch.getDelay());

        // discard candidate?
        if (!validSequence(candidate))
            return null;

        // valid basic block
        return candidate;
    }

    /*
     * For all valid hashcodes, make the symbolic basic block and its in/out contexts
     
    private List<BinarySegment> makeBasicBlocks(InstructionStream istream) {
    
        //The list this detector will construct         
        List<BinarySegment> allsequences = new ArrayList<BinarySegment>();
    
        // all start addrs grouped by hashcode
        Iterator<Integer> it = this.getAddrs().keySet().iterator();
    
        // for each hashcode
        while (it.hasNext()) {
    
            // get hashcode
            var hashcode = it.next();
    
            // get all start addrs of all sequences with this hashcode
            var addrlist = this.getAddrs().get(hashcode);
    
            // get a list of the sequences by their hashcode_startaddr key
            var seqlist = new ArrayList<HashedSequence>();
            for (Integer startaddr : addrlist) {
                var keyval = hashcode.toString() + "_" + Integer.toString(startaddr);
                seqlist.add(this.getHashed().get(keyval));
            }
    
            // use first sequence with this hash code to create symbolic sequence
            var symbolicseq = seqlist.get(0).makeSymbolic();
    
            // Create all contexts
            var contexts = new ArrayList<SegmentContext>();
            for (HashedSequence seq : seqlist)
                contexts.add(new SegmentContext(seq));
    
            // Create the block
            var newbb = makeSegment(symbolicseq, contexts);
            allsequences.add(newbb);
        }
    
        return allsequences;
    }*/

    /*
     * 
     */
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
                List<Instruction> candidate = null;
                if ((candidate = checkCandidate(window, is)) != null) {

                    // create new candidate hash sequence
                    var newseq = BinarySegmentDetectionUtils.hashSequence(candidate);

                    // add sequence to occurrence counters
                    // (counting varies between static to trace detection)
                    BinarySegmentDetectionUtils.addAddrToList(addrs, newseq);

                    // add sequence to map which is indexed by hashCode + startaddr
                    BinarySegmentDetectionUtils.addHashSequenceToList(hashed, newseq);
                }
            }

            // pop one
            while (insertcount > 0 && window.size() > this.getConfig().getMaxsize()) {
                insertcount--;
                window.remove(0);
            }
        }

        // for all valid hashed sequences, make the StaticBasicBlock objects
        var basicblocks = super.makeSegments(hashed, addrs);

        // finally, init some stats
        SegmentBundle bundle = new SegmentBundle(basicblocks, istream);
        return bundle;
    }
}
