package pt.up.fe.specs.binarytranslation.loopdetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.Operand;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.FrequentSequence;
import pt.up.fe.specs.binarytranslation.interfaces.StaticStream;

public class FrequentStaticSequenceDetector implements SegmentDetector {

    private List<Instruction> insts;
    private StaticStream elfstream;

    // easy lookup of insts by converting addr to index
    private Map<Integer, Integer> addrtoindex;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public FrequentStaticSequenceDetector(StaticStream istream) {
        this.elfstream = istream;
        this.insts = new ArrayList<Instruction>();
        this.addrtoindex = new HashMap<Integer, Integer>();

        int i = 0;
        Instruction inst;
        while ((inst = elfstream.nextInstruction()) != null) {
            insts.add(inst);
            addrtoindex.put(inst.getAddress().intValue(), i++);
        }
    }

    /*
     * Private helper class to hold hash 
     * sequence data, and prevent duplicate work
     */
    private class HashedSequence {
        protected List<Instruction> instlist;
        protected List<Integer> addrs;

        HashedSequence(List<Instruction> instlist, Integer addr) {
            this.instlist = instlist;
            this.addrs = new ArrayList<Integer>(addr);
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
     * Builds register replacement map for a given sequence (assumed valid)
     */
    private Map<Integer, String> makeRegReplaceMap(List<Instruction> ilist) {

        char symbol = 'a';
        Map<Integer, String> regremap = new HashMap<Integer, String>();
        for (Instruction i : ilist) {

            var operands = i.getData().getOperands();
            for (Operand op : operands) {

                // remap register numbering to start from zero
                if (!op.isRegister())
                    continue;

                if (!regremap.containsKey(op.getIntegerValue()))
                    regremap.put(op.getIntegerValue(), String.valueOf(symbol++));
            }
        }

        return regremap;
    }

    /*
     * Makes a copy of each instruction, but turns operands symbolic
     */
    private List<Instruction> makeSymbolic(List<Instruction> ilist, Map<Integer, String> regremap) {

        List<Instruction> symbolic = new ArrayList<Instruction>();

        Integer addr = 0;
        for (Instruction i : ilist) {
            symbolic.add(i.copy());
            symbolic.get(symbolic.size() - 1).makeSymbolic(addr, regremap);
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
            if (sequenceSize > atomicityCountDown) {
                i += atomicityCountDown;
                atomicityCountDown = 0;
            }

            // discard candidate?
            if (!validSequence(candidate))
                continue;

            // make vague
            Map<Integer, String> regremap = makeRegReplaceMap(candidate);

            // need to make a copy of each instruction! so next detections dont break
            List<Instruction> abstractedcandidate = makeSymbolic(candidate, regremap);

            String hashstring = "";
            for (Instruction inst : abstractedcandidate) {

                // make part 1 of hashstring
                hashstring += "_" + Integer.toHexString(inst.getProperties().getOpCode());
                // TODO this unique id (the opcode) will not be unique for arm, since the
                // specific instruction is resolved later with fields that arent being
                // interpreted yet; how to solve?
                // TODO replace getOpCode with getUniqueID()

                // make part 2 of hashstring
                for (Operand op : inst.getData().getOperands()) {
                    // if (op.isRegister()) {
                    hashstring += "_" + op.getStringValue();
                    // }
                }
            }

            Integer hashCode = hashstring.hashCode();
            Integer startAddr = insts.get(i).getAddress().intValue();

            System.out.print(hashstring + "\n");

            // if sequence was complete, add (or add addr to existing equal sequence)
            if (sequences.containsKey(hashCode)) {
                sequences.get(hashCode).addrs.add(startAddr);
            }

            // add new sequence
            else {
                sequences.put(hashCode, new HashedSequence(abstractedcandidate, startAddr));
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

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO pass window size?
        // TODO pass forbidden operations list?

        List<BinarySegment> allsequences = new ArrayList<BinarySegment>();

        /*
         * Construct sequences between given sizes
         */
        for (int size = 2; size < 4; size++) {
            for (HashedSequence seq : getSequences(size)) {
                allsequences.add(new FrequentSequence(seq.instlist, seq.addrs));
            }
        }

        return allsequences;
    }

    @Override
    public void close() throws Exception {
        elfstream.close();
    }
}
