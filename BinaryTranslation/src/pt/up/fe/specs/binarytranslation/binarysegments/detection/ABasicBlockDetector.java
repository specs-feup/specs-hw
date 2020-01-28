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
 * 
 * @author Nuno
 *
 */
public abstract class ABasicBlockDetector implements SegmentDetector {

    /*
    * Static or trace instruction stream
    */
    protected InstructionStream istream;

    /*
     * Final list of detected basic blocks
     */
    protected List<BinarySegment> loops = null;

    /*
     * This map holds all hashed sequences for all instruction windows we analyze
     * Map: <hashcode_startaddr, hashedsequence>
     */
    protected Map<String, HashedSequence> hashed = new HashMap<String, HashedSequence>();

    /*
     * This map holds all hash codes and list of occurring addresses for each
     * Map: <hashcode, list of addresses>
     */
    protected Map<Integer, List<Integer>> addrs = new HashMap<Integer, List<Integer>>();

    /*
     * Stuff for statistics (TODO: add more) TODO: move to abstract ABinarySegment 
     */
    protected long totalCycles;
    protected long numInsts;

    /*
     * max size of window for capture of basicblock 
     */
    protected final int maxsize = 40;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public ABasicBlockDetector(InstructionStream istream) {
        this.istream = istream;
    }

    /*
     * Must be implemented by children
     */
    protected abstract BinarySegment makeBasicBlock(List<Instruction> symbolicseq, List<SegmentContext> contexts);

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
     * For a window of this.maxsize, check for valid candidate, and hash if valid
     * Should only be necessary to check for one valid sequence for the last branch in the window
     */
    protected void checkCandidate(List<Instruction> window, Instruction backbranch) {

        // Address of the backwards branch
        var baseaddr = window.get(0).getAddress().intValue();
        int startAddr = backbranch.getBranchTarget().intValue();

        // backwards branch has left the window
        if (startAddr < baseaddr)
            return;

        // compute indexes
        int sidx = 0;
        while (window.get(sidx).getAddress().intValue() != startAddr) {
            sidx++;
        }

        int eidx = 0;
        while (window.get(eidx) != backbranch) {
            eidx++;
        }
        eidx += 1;

        // sub-window
        List<Instruction> candidate = window.subList(sidx, eidx + backbranch.getDelay());

        // discard candidate?
        if (!validSequence(candidate))
            return;

        // create new candidate hash sequence
        var newseq = BinarySegmentDetectionUtils.hashSequence(candidate);

        // add sequence to occurrence counters (counting varies between static to trace detection)
        BinarySegmentDetectionUtils.addAddrToList(this.addrs, newseq);

        // add sequence to map which is indexed by hashCode + startaddr
        BinarySegmentDetectionUtils.addHashSequenceToList(this.hashed, newseq);
    }

    /*
     * For all valid hashcodes, make the symbolic basic block and its in/out contexts
     */
    protected void makeBasicBlocks() {

        // make list of segments
        this.loops = new ArrayList<BinarySegment>();

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

            // Create the block
            var newbb = makeBasicBlock(symbolicseq, contexts);
            newbb.setAppName(this.istream.getApplicationName());
            newbb.setCompilationFlags(this.istream.getCompilationInfo());

            // Add to return list
            loops.add(newbb);
        }
    }

    /*
     * 
     */
    @Override
    public void close() throws Exception {
        istream.close();
    }
}
