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
        Integer idx = 0;
        Instruction inst = null;
        this.insts = new ArrayList<Instruction>();
        this.backBranchesIdx = new ArrayList<Integer>();

        while ((inst = istream.nextInstruction()) != null) {

            // save all instructions
            insts.add(inst);

            // record backwards branches
            if (inst.isBackwardsJump() && inst.isConditionalJump() && inst.isRelativeJump())
                backBranchesIdx.add(idx);
            idx++;
        }
    }

    @Override
    protected BinarySegment makeBasicBlock(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new StaticBasicBlock(symbolicseq, contexts);
    }

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO: treat in another fashion
        if (loops != null)
            return this.loops;

        // starting from each target, add every following instruction to
        // the list of that Basic Block, until branch itself is reached
        for (Integer i : backBranchesIdx) {

            // window
            List<Instruction> window = insts.subList(i - this.maxsize, i);

            // sequences in this window
            hashSequences(window);
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
