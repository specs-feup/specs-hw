/**
 * Copyright 2019 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.FrequentSequence;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;
import pt.up.fe.specs.binarytranslation.instruction.OperandType;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author Nuno
 *
 */
public class FrequentSequenceDetector implements SegmentDetector {

    /*
     * An open instruction stream, either from elf dump, or simulator
     */
    private InstructionStream istream;

    /*
     * min and max size of windows 
     */
    private final int minsize = 2;
    private final int maxsize = 10;

    /*
     * This map holds all hashed sequences for all instruction windows we analyse
     */
    private Map<Integer, HashedSequence> hashed = new HashMap<Integer, HashedSequence>();

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public FrequentSequenceDetector(InstructionStream istream) {
        this.istream = istream;
    }

    /*
     * Private helper class to hold hash 
     * sequence data, and prevent duplicate work
     */
    private class HashedSequence {
        protected Map<String, String> regremap;
        protected List<Instruction> instlist;
        protected List<Integer> addrs;
        protected int size;

        HashedSequence(List<Instruction> instlist, Integer addr, Map<String, String> regremap) {
            this.instlist = instlist;
            this.size = instlist.size();
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
            // do not form frequent sequences memory accesses
            if (inst.isUnknown() || inst.isJump() || inst.isMemory()) {
                return false;
            }
        }

        return true;
    }

    /*hashed
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
        for (int i = this.minsize; i < this.maxsize; i++) {

            // sub-window
            List<Instruction> candidate = w.subList(0, i);

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
            Integer startAddr = w.get(0).getAddress().intValue();

            // if sequence was complete, add (or add addr to existing equal sequence)
            if (this.hashed.containsKey(hashCode))
                this.hashed.get(hashCode).addrs.add(startAddr);

            // add new sequence
            else
                this.hashed.put(hashCode, new HashedSequence(candidate, startAddr, regremap));
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

        List<Instruction> window = new ArrayList<Instruction>(this.maxsize); // fixed sized window

        // make 1st window
        for (int i = 0; i < this.maxsize; i++)
            window.set(i, this.istream.nextInstruction());

        // process entire stream
        while (istream.hasNext()) {

            // sequences in this window, from sizes minsize to maxsize
            hashSequences(window);

            // shift window (i.e. new window)
            for (int i = 0; i < this.maxsize - 1; i++)
                window.set(i, window.get(i + 1));

            // one new instruction in window
            window.set(this.maxsize - 1, this.istream.nextInstruction());

            // TODO must deal with atomicity when constructing new candidate window

            /*
            // count down if any atom in progress
             * int atomicityCountDown = 0;
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
            */
        }

        /*
         * Remove all sequences which only happen once
         */
        Iterator<Integer> it = this.hashed.keySet().iterator();
        while (it.hasNext()) {
            var size = this.hashed.get(it.next()).addrs.size();
            if (size <= 1)
                it.remove();
        }

        // for all unique sequences which occur more than once, symbolify and add to output
        List<BinarySegment> allsequences = new ArrayList<BinarySegment>();

        for (HashedSequence seq : this.hashed.values()) {
            List<Instruction> symbolicseq = makeSymbolic(seq.instlist, seq.regremap);
            allsequences.add(new FrequentSequence(symbolicseq, seq.addrs));
        }

        return allsequences;
    }

    @Override
    public void close() throws Exception {
        istream.close();
    }
}
