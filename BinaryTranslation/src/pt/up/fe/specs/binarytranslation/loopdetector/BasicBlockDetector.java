package pt.up.fe.specs.binarytranslation.loopdetector;

import java.util.List;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.InstructionStream;
import pt.up.fe.specs.binarytranslation.loops.BasicBlock;
import pt.up.fe.specs.binarytranslation.loops.BinarySegment;

public final class BasicBlockDetector implements LoopDetector {

    private List<BasicBlock> loops;

    public BasicBlockDetector() {

    }

    @Override
    public List<BinarySegment> detectLoops(InstructionStream istream) {
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
