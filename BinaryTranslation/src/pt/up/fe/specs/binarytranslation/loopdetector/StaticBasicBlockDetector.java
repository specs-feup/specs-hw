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

    private List<BasicBlock> loops;
    private List<Instruction> insts;
    private List<Instruction> backwardsBranches;
    private InstructionStream elfstream;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public StaticBasicBlockDetector(InstructionStream istream) {
        this.elfstream = istream;
        this.loops = new ArrayList<BasicBlock>();
        this.insts = new ArrayList<Instruction>();
        this.backwardsBranches = new ArrayList<Instruction>();
    }

    @Override
    public List<BinarySegment> detectLoops() {

        // identify all backwards branches
        Instruction inst = null;
        while ((inst = elfstream.nextInstruction()) != null) {
            if (inst.isBackwardsBranch()) // TODO Check if branch is conditional, and not a return from routine etc
                backwardsBranches.add(inst);
        }

        // starting from each target, add every following instruction to
        // the list of that Basic Block, until branch itself is reached
        for (Instruction is : backwardsBranches) {
            // long startAddr = is.getTargetAddr();

            // TODO check for Imm...
            // actually, each ISA should have a method to getBranchTarget,
            // for all of its branch types!

            // every instruction has a targetaddr
            // its +4 for every instruction, except branches

        }

        return null;

    }

    @Override
    public void close() throws Exception {
        close();
    }

    @Override
    public void step(Instruction instruction) {
        // TODO Auto-generated method stub

    }

}
