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
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.binarysegments.StaticBasicBlock;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.legacy.StaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * Detects all basic blocks in an ELF dump (i.e., static code) ElF dump must be provided by any implementation of
 * {@link StaticInstructionStream}
 * 
 * @author NunoPaulino
 *
 */
public class StaticBasicBlockDetector extends ABasicBlockDetector {

    private List<BinarySegment> loops = null;
    private List<Instruction> insts;
    private List<Integer> backBranchesIdx;

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
    public StaticBasicBlockDetector(InstructionStream istream) {
        super(istream);

        // save entire dump
        Instruction inst = null;
        this.insts = new ArrayList<Instruction>();
        while ((inst = istream.nextInstruction()) != null) {
            insts.add(inst);
            // save all instructions
        }
    }

    /*
     * Gathers all backwards branches in the ELF dump
     */
    private void listBackwardsBranches() {

        // identify all backwards branches, save indexes in list
        this.backBranchesIdx = new ArrayList<Integer>();
        Integer idx = 0;
        Iterator<Instruction> it = insts.iterator();
        while (it.hasNext()) {
            var inst = it.next();
            if (inst.isBackwardsJump() && inst.isConditionalJump() && inst.isRelativeJump())
                backBranchesIdx.add(idx);
            idx++;
        }
    }

    /*
     * For all valid hashcodes, make the symbolic basic block and its in/out contexts
     */
    private void makeStaticBasicBlocks() {

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
            var newbb = new StaticBasicBlock(symbolicseq, contexts);
            newbb.setAppName(this.istream.getApplicationName());
            newbb.setCompilationFlags(this.istream.getCompilationInfo());

            // Add to return list
            loops.add(newbb);
        }

    }

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO: treat in another fashion
        if (loops != null)
            return this.loops;

        // identify all backwards branches, save indexes in list
        listBackwardsBranches();

        // make list of segments
        this.loops = new ArrayList<BinarySegment>();

        // starting from each target, add every following instruction to
        // the list of that Basic Block, until branch itself is reached
        for (Integer i : backBranchesIdx) {

            // The backwards branch
            Instruction is = insts.get(i);

            // Address of the backwards branch
            long thisAddr = is.getAddress().longValue();
            long startAddr = is.getBranchTarget().longValue();

            // actually, each ISA should have a method to getBranchTarget, for all of its branch types!
            // every instruction has a targetaddr its +4 for every instruction, except branches

            // Size of the Basic Block + any delay gaps from branch instruction
            int bbSize = (int) ((thisAddr - startAddr) / istream.getInstructionWidth());
            int delay = is.getDelay();

            // candidate
            List<Instruction> candidate = insts.subList(i - bbSize, i + 1 + delay);

            // validate sequence
            if (!isValid(candidate))
                continue;

            // create new candidate hash sequence
            var newseq = BinarySegmentDetectionUtils.hashSequence(candidate);

            // add sequence to occurrence counters (counting varies between static to trace detection)
            BinarySegmentDetectionUtils.addAddrToList(this.addrs, newseq);

            // add sequence to map which is indexed by hashCode + startaddr
            BinarySegmentDetectionUtils.addHashSequenceToList(this.hashed, newseq);
        }

        // Remove all sequences which only happen once
        BinarySegmentDetectionUtils.removeUnique(this.addrs, this.hashed);

        // for all valid hashed sequences, make the StatiBasicBlock objects
        makeStaticBasicBlocks();

        // finally, init some stats
        this.totalCycles = istream.getCycles();
        this.numInsts = istream.getNumInstructions();

        return loops;
    }

    @Override
    public float getCoverage(int segmentSize) {
        Integer detectedportion = 0;
        for (BinarySegment seg : this.loops) {
            detectedportion += seg.getExecutionCycles();
        }
        return (float) detectedportion / this.totalCycles;
    }
}
