package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author Nuno
 *
 */
public abstract class ABasicBlockDetector implements SegmentDetector {

    protected InstructionStream istream;

    /*
     * Stuff for statistics (TODO: add more) TODO: move to abstract ABinarySegment 
     */
    protected long totalCycles;
    protected long numInsts;

    /*
     * Since list needs revisiting, absorb all instructions in
     * the static dump into StaticBasicBlockDetector class instance
     */
    public ABasicBlockDetector(InstructionStream istream) {
        this.istream = istream;
    }

    /*
     * Basic block can only have one branch back, and zero branches forward
     */
    protected boolean isValid(List<Instruction> candidate) {

        int numbranches = 0;
        for (Instruction i : candidate) {

            if (i.isJump())
                numbranches++;

            if (i.isForwardsJump())
                return false;
        }

        return (numbranches > 1) ? false : true;
    }

    /*
     * 
     */
    @Override
    public void close() throws Exception {
        istream.close();
    }
}
