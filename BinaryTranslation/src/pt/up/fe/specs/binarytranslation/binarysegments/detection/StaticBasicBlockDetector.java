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
public final class StaticBasicBlockDetector implements SegmentDetector {

    private List<BinarySegment> loops = null;
    private List<Instruction> insts;
    private List<Integer> backBranchesIdx;
    private InstructionStream elfstream;

    /*
     * Stuff for statistics (TODO: add more) TODO: move to abstract ABinarySegment 
     */
    protected long totalCycles;
    protected long numInsts;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public StaticBasicBlockDetector(InstructionStream istream) {
        this.elfstream = istream;
        this.insts = new ArrayList<Instruction>();
        this.backBranchesIdx = new ArrayList<Integer>();
    }

    /*
     * Basic block can only have one branch back, and zero branches forward
     */
    private boolean isValid(List<Instruction> candidate) {

        int numbranches = 0;
        for (Instruction i : candidate) {

            if (i.isJump())
                numbranches++;

            if (i.isForwardsJump())
                return false;
        }

        return (numbranches > 1) ? false : true;
    }

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO: treat in another fashion
        if (loops != null)
            return this.loops;

        // identify all backwards branches, save indexes in list
        Integer idx = 0;
        Instruction inst = null;
        while ((inst = elfstream.nextInstruction()) != null) {
            if (inst.isBackwardsJump() && inst.isConditionalJump() && inst.isRelativeJump())
                backBranchesIdx.add(idx);
            idx++;
            insts.add(inst);
            // save all instructions
        }

        // make list
        this.loops = new ArrayList<BinarySegment>();

        // starting from each target, add every following instruction to
        // the list of that Basic Block, until branch itself is reached
        for (Integer i : backBranchesIdx) {

            // The backwards branch
            Instruction is = insts.get(i);

            // Address of the backwards branch
            long thisAddr = is.getAddress().longValue();
            long startAddr = is.getBranchTarget().longValue();
            // these values are in bytes

            // TODO check for Imm...
            // OR accept that we cant capture basicblocks above 32k jumps

            // actually, each ISA should have a method to getBranchTarget,
            // for all of its branch types!
            // every instruction has a targetaddr
            // its +4 for every instruction, except branches

            // TODO
            // solution for imm: during elfstream parsing, if absorbed instruction
            // is imm, then absorb another, and set the imm value as a field
            // in the absorbed instruction

            // Size of the Basic Block + any delay gaps from branch instruction
            int bbSize = (int) ((thisAddr - startAddr) / elfstream.getInstructionWidth());
            int delay = is.getDelay();

            // candidate
            List<Instruction> candidate = insts.subList(i - bbSize, i + 1 + delay);

            // validate sequence
            if (!isValid(candidate))
                continue;

            // Create the block
            StaticBasicBlock bb = new StaticBasicBlock(insts.subList(i - bbSize, i + 1 + delay));

            // Add to return list
            loops.add(bb);
        }

        // finally, init some stats
        this.totalCycles = elfstream.getCycles();
        this.numInsts = elfstream.getNumInstructions();

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

    @Override
    public void close() throws Exception {
        elfstream.close();
    }
}
