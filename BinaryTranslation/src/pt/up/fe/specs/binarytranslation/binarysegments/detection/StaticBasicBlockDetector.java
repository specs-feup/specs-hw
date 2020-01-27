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
import java.util.Iterator;
import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.binarysegments.StaticBasicBlock;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * Detects all basic blocks in an ELF dump (i.e., static code) ElF dump must be provided by any implementation of
 * {@link AStaticInstructionStream}
 * 
 * @author NunoPaulino
 *
 */
public class StaticBasicBlockDetector extends ABasicBlockDetector {

    private List<Instruction> insts;
    private List<Integer> backBranchesIdx;

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

    @Override
    protected BinarySegment makeBasicBlock(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new StaticBasicBlock(symbolicseq, contexts);
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

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO: treat in another fashion
        if (loops != null)
            return this.loops;

        // identify all backwards branches, save indexes in list
        listBackwardsBranches();

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

        // for all valid hashed sequences, make the StaticBasicBlock objects
        makeBasicBlocks();

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
