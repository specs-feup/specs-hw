package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;
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
    public List<BinarySegment> allsequences = null;

    /*
     * An open instruction stream, either from elf dump, or simulator
     */
    private InstructionStream istream;

    /*
     * Stuff for statistics (TODO: add more) 
     */
    protected long totalCycles;
    protected long numInsts;
    // protected int totalMemoryInsts;
    // protected int totalLoads;
    // protected int totalStores;
    // protected int totalBranchInsts;
    // protected int totalTakenBranches;
    // protected int totalNonTakenBranches;

    /*
     * min and max size of windows 
     */
    protected final int minsize = 2;
    protected final int maxsize = 20;

    /*
     * This map holds all hashed sequences for all instruction windows we analyze
     * Map: <hashcode_startaddr, hashedsequence>
     */
    protected Map<String, HashedSequence> hashed = new HashMap<String, HashedSequence>();

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
    protected abstract void listHashedSequence(Integer hashCode, Integer startAddr);

    /*
     * Remove sequences that only happen once
     */
    protected abstract void removeUnique();

    /*
     * 
     */
    protected abstract List<Integer> getAddressList(Integer hashcode);

    /*
     * 
     */
    protected abstract Iterator<Integer> getHashIterator();

    /*
     * 
     */
    protected abstract BinarySegment makeFrequentSequence(List<Instruction> symbolicseq, List<SegmentContext> contexts);

    // no point in starting to build hashes if sequence will fail at some point
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
     * Builds operand value replacement map for a given sequence (assumed valid)
     */
    private Map<String, String> makeRegReplaceMap(List<Instruction> ilist) {

        Map<OperandType, Character> counter = new HashMap<OperandType, Character>();
        Map<String, String> regremap = new HashMap<String, String>();
        for (Instruction i : ilist) {

            var operands = i.getData().getOperands();
            for (Operand op : operands) {

                // register must not be special (e.g. stack pointer in ARM)
                if (op.isSpecial())
                    continue;

                if (!regremap.containsKey(op.getRepresentation())) {

                    // get current count
                    char c;

                    // OperandType
                    var typeid = op.getProperties().getMainType();

                    // TODO should be an exception here if operand is symbolic
                    // must be non symbolic REGISTER or IMMEDIATE

                    if (!counter.containsKey(typeid))
                        counter.put(typeid, 'a');
                    else {
                        c = counter.get(typeid).charValue();
                        counter.put(typeid, Character.valueOf(++c));
                    }

                    // remap
                    c = counter.get(typeid).charValue();
                    regremap.put(op.getRepresentation(), String.valueOf(c));
                }
            }

            // TODO implement imm remapping strategies here??
        }

        return regremap;
    }

    /*
     * For the given window "w", builds all hash sequences from
     * sizes minsize to maxsize
     * Constructs a map with <instruction addr, sequence hash>
     */
    private void hashSequences(List<Instruction> w) {

        // compute all hashes for this window, for all sequence sizes
        for (int i = this.minsize; i <= this.maxsize; i++) {

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

            // make register replacement map (for hash building)
            Map<String, String> regremap = makeRegReplaceMap(candidate);
            // TODO the replacement function should be a parameter here

            String hashstring = "";
            for (Instruction inst : candidate) {

                // make part 1 of hash string
                hashstring += "_" + Integer.toHexString(inst.getProperties().getOpCode());
                // TODO this unique id (the opcode) will not be unique for arm, since the
                // specific instruction is resolved later with fields that arent being
                // interpreted yet; how to solve?
                // TODO replace getOpCode with getUniqueID() (as a string)

                // make part 2 of hash string
                for (Operand op : inst.getData().getOperands()) {
                    hashstring += "_" + regremap.get(op.getRepresentation());
                    // at this point, imms have either been (or not) all (or partially) symbolified
                }
            }

            // if sequence was complete, add (or add addr to existing equal sequence)
            Integer hashCode = hashstring.hashCode();

            // sequence start addr
            int startAddr = candidate.get(0).getAddress().intValue();

            // add sequence to occurrence counters (counting varies between static to trace detection)
            listHashedSequence(hashCode, startAddr);

            // add sequence to map which is indexed by hashCode
            var keyval = hashCode.toString() + "_" + Integer.toString(startAddr);
            if (!this.hashed.containsKey(keyval))
                this.hashed.put(keyval, new HashedSequence(hashCode, candidate, regremap));
            else
                this.hashed.get(keyval).incrementOccurences();
            // useful for traces
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
        Iterator<Integer> it = getHashIterator();

        // this.addrs.keySet().iterator();

        // for each hashcode
        while (it.hasNext()) {

            // get hashcode
            var hashcode = it.next();

            // get all start addrs of all sequences with this hashcode
            var addrlist = getAddressList(hashcode);

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
            this.allsequences.add(makeFrequentSequence(symbolicseq, contexts));
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
        // 3. can demand that all are equal in all ocurrences of sequence (all are non symbolic and hence all are
        // hardware specilaized literals)

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
        removeUnique();

        // Make all valid sequences
        makeFrequentSequences();

        // finally, init some stats
        this.totalCycles = istream.getCycles();
        this.numInsts = istream.getNumInstructions();

        return this.allsequences;
    }

    /*
     * For static: Return the percentage of the ELF code the segments detected represent
     * For trace: Return the percentage of the executed instructions the segments detected represent
     * NOTE: only considers segments of size "segmentSize", to avoid overlap
     */
    @Override
    public float getCoverage(int segmentSize) {
        Integer detectedportion = 0;
        for (BinarySegment seg : this.allsequences) {
            if (seg.getSegmentLength() == segmentSize)
                detectedportion += seg.getExecutionCycles();
        }
        return (float) detectedportion / this.totalCycles;
    }

    @Override
    public void close() throws Exception {
        istream.close();
    }
}
