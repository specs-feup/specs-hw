package pt.up.fe.specs.binarytranslation.loopdetector;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.InstructionStream;
import pt.up.fe.specs.binarytranslation.binarysegments.BasicBlock;
import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;

/*
 * Detects all basic blocks in an ELF dump (i.e., static code)
 */
public final class StaticBasicBlockDetector implements LoopDetector {

    private List<BinarySegment> loops;
    private List<Instruction> insts;
    private List<Integer> backBranchesIdx;
    private InstructionStream elfstream;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public StaticBasicBlockDetector(InstructionStream istream) {
        this.elfstream = istream;
        this.loops = new ArrayList<BinarySegment>();
        this.insts = new ArrayList<Instruction>();
        this.backBranchesIdx = new ArrayList<Integer>();
    }

    @Override
    public List<BinarySegment> detectLoops() {

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

        // starting from each target, add every following instruction to
        // the list of that Basic Block, until branch itself is reached
        for (Integer i : backBranchesIdx) {

            // The backwards branch
            Instruction is = insts.get(i);

            // Address of the backwards branch
            long thisAddr = is.getAddress().longValue();

            long startAddr = is.getBranchTarget().longValue();
            // TODO check for Imm...
            // OR accept that we cant capture basicblocks above 32k jumps

            // actually, each ISA should have a method to getBranchTarget,
            // for all of its branch types!
            // every instruction has a targetaddr
            // its +4 for every instruction, except branches

            // Size of the Basic Block
            int bbSize = (int) ((thisAddr - startAddr) / elfstream.getInstructionWidth());

            // Create the block
            BasicBlock bb = new BasicBlock(insts.subList(i, i + bbSize));

            // Add to return list
            loops.add(bb);
        }

        return loops;
    }

    @Override
    public void close() throws Exception {
        close();
    }
}
