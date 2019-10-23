package pt.up.fe.specs.binarytranslation.loopdetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.Operand;
import pt.up.fe.specs.binarytranslation.OperandType;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.FrequentSequence;
import pt.up.fe.specs.binarytranslation.interfaces.StaticStream;

public class FrequentStaticSequenceDetector implements SegmentDetector {

    private List<Instruction> insts;
    private StaticStream elfstream;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public FrequentStaticSequenceDetector(StaticStream istream) {
        this.elfstream = istream;
        this.insts = new ArrayList<Instruction>();

        Instruction inst = null;
        while ((inst = elfstream.nextInstruction()) != null) {
            insts.add(inst);
        }
    }

    /*
     * Private helper class to hold hash 
     * sequence data, and prevent duplicate work
     */
    private class HashedSequence {
        protected Map<String, String> regremap;
        protected List<Instruction> instlist;
        protected List<Integer> addrs;

        HashedSequence(List<Instruction> instlist, Integer addr, Map<String, String> regremap) {
            this.instlist = instlist;
            this.addrs = new ArrayList<Integer>();
            this.addrs.add(addr);
            this.regremap = regremap;
        }
    }

    // no point in starting to build hashes if sequence will fail at some point
    private Boolean validSequence(List<Instruction> insts) {

        // check if this subsequence is at all apt
        for (Instruction inst : insts) {

            // TODO fail with stream instructions

            // do not form frequent sequences containing jumps
            if (inst.isJump()) {
                return false;
            }

            // do not form frequent sequences memory accesses
            else if (inst.isMemory()) {
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
     * Makes a copy of each instruction, but turns operands symbolic
     */
    private List<Instruction> makeSymbolic(List<Instruction> ilist, Map<String, String> regremap) {

        // Hard copy
        List<Instruction> symbolic = new ArrayList<Instruction>();
        for (Instruction i : ilist) {
            symbolic.add(i.copy());
        }

        // Symbolify
        Integer addr = 0;
        for (Instruction i : symbolic) {
            i.makeSymbolic(addr, regremap);
            addr += 4;
        }

        return symbolic;
    }

    /*
     * Constructs a map with <instruction addr, sequence hash>
     * Only returns hashes for sequences which happens more than once
     * Returns a map with <sequence hash, List<instruction start addr(s)>>
     */
    private List<HashedSequence> getSequences(int sequenceSize) {

        // Generate a hash code list of all sequences of size "sequenceSize"
        Map<Integer, HashedSequence> sequences = new HashMap<Integer, HashedSequence>();
        int atomicityCountDown = 0;
        for (int i = 0; (i + sequenceSize) < insts.size(); i++) {

            List<Instruction> candidate = insts.subList(i, i + sequenceSize);

            // count down if any atom in progress
            if (atomicityCountDown > 0)
                atomicityCountDown--;

            // if last instruction in previous candidate
            // had delay, next block must be atomic
            // (e.g., sequence cannot span non atomic bounds)
            if (i > 0)
                atomicityCountDown = insts.get(i - 1).getDelay();

            // if candidate sequence window starts
            // breaking atomicity window, then advance
            if (atomicityCountDown > 0)
                if (sequenceSize > atomicityCountDown) {
                    i += atomicityCountDown;
                    atomicityCountDown = 0;
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

            Integer hashCode = hashstring.hashCode();
            Integer startAddr = insts.get(i).getAddress().intValue();

            // if sequence was complete, add (or add addr to existing equal sequence)
            if (sequences.containsKey(hashCode)) {
                sequences.get(hashCode).addrs.add(startAddr);
            }

            // add new sequence
            else {
                sequences.put(hashCode, new HashedSequence(candidate, startAddr, regremap));
            }
        }

        /*
         * Remove all sequences which only happen once
         */
        Iterator<Integer> it = sequences.keySet().iterator();
        while (it.hasNext()) {
            var size = sequences.get(it.next()).addrs.size();
            if (size <= 1)
                it.remove();
        }

        return new ArrayList<HashedSequence>(sequences.values());
    }

    /*
     * Returns frequently occurring instruction sequences
     */
    @Override
    public List<BinarySegment> detectSegments() {

        // TODO pass window size?
        // TODO pass forbidden operations list?

        // IMPORTANT
        // TODO: imms can be treated very differently!
        // 1. can either allow them to be all different (not part of the hash)
        // 2. can try to find common imms and leave only those to be literal (i.e., non symbolic)
        // 3. can demand that all are equal in all ocurrences of sequence (all are non symbolic and hence all are
        // hardware specilaized literals)

        List<BinarySegment> allsequences = new ArrayList<BinarySegment>();

        // Construct sequences between given sizes
        for (int size = 2; size < 10; size++) {
            for (HashedSequence seq : getSequences(size)) {
                List<Instruction> symbolicseq = makeSymbolic(seq.instlist, seq.regremap);
                allsequences.add(new FrequentSequence(symbolicseq, seq.addrs));
            }
        }

        return allsequences;
    }

    @Override
    public void close() throws Exception {
        elfstream.close();
    }
}
