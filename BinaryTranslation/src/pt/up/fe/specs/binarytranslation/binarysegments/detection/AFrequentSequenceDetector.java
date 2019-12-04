package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.binarysegments.AFrequentSequence;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;
import pt.up.fe.specs.binarytranslation.instruction.SimpleInstruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * Detects frequent instruction sequences from either static binary or instruction traces
 * 
 * @author nuno
 *
 */
public abstract class AFrequentSequenceDetector implements SegmentDetector {

    /*
     * An open instruction stream, either from elf dump, or simulator
     */
    private InstructionStream istream;

    /*
     * min and max size of windows 
     */
    protected final int minsize = 2;
    protected final int maxsize = 10;

    /*
     * This map holds all hashed sequences for all instruction windows we analyse
     */
    protected Map<Integer, HashedSequence> hashed = new HashMap<Integer, HashedSequence>();

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
    protected abstract void addHashedSequence(Integer hashCode,
            List<Instruction> candidate, Map<String, String> regremap);

    /*
     * Remove sequences that only happen once
     */
    protected abstract void removeUnique();

    /*
     * Add detected frequent sequence
     */
    protected abstract AFrequentSequence makeFrequentSequence(Integer hashcode, List<Instruction> ilist);

    // no point in starting to build hashes if sequence will fail at some point
    private Boolean validSequence(List<Instruction> insts) {

        // check if this subsequence is at all apt
        for (Instruction inst : insts) {

            // TODO fail with stream instructions

            // do not form sequences with unknown instructions
            // do not form frequent sequences containing jumps
            // do not form frequent sequences memory accesses
            if (inst.isUnknown() || inst.isJump() || inst.isMemory()) {
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
            // if (!validSequence(candidate))
            // continue;

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
            addHashedSequence(hashCode, candidate, regremap);
        }

        return;
    }

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

        // for all sequences which occur more than once, symbolify and add to output
        List<BinarySegment> allsequences = new ArrayList<BinarySegment>();

        Iterator<Integer> it = this.hashed.keySet().iterator();
        while (it.hasNext()) {
            var hashcode = it.next();
            var sequence = this.hashed.get(hashcode);

            var rebuiltI = new ArrayList<Instruction>();
            for (SimpleInstruction i : sequence.getInstlist())
                rebuiltI.add(i.rebuild());

            List<Instruction> symbolicseq = makeSymbolic(rebuiltI, sequence.getRegremap());
            allsequences.add(makeFrequentSequence(hashcode, symbolicseq));
        }

        return allsequences;
    }

    @Override
    public void close() throws Exception {
        istream.close();
    }
}
