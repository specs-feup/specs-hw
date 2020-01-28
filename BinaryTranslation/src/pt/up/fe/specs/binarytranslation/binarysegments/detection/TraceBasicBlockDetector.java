package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.binarysegments.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author nuno
 *
 */
public class TraceBasicBlockDetector extends ABasicBlockDetector {

    /*
     * 
     */
    public TraceBasicBlockDetector(InstructionStream istream) {
        super(istream);
    }

    /*
     * Must be implemented by children: StaticBasicBlockDetector, and TraceBasicBlockDetector
     */
    @Override
    protected BinarySegment makeBasicBlock(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new TraceBasicBlock(symbolicseq, contexts);
    }

    @Override
    public List<BinarySegment> detectSegments() {

        // TODO: treat in another fashion
        if (loops != null)
            return this.loops;

        List<Instruction> window = new ArrayList<Instruction>();

        // process entire stream
        do {

            var is = this.istream.nextInstruction();
            window.add(is);

            // "is" isn't a possible backwards branch that could generate a basic block
            if (!is.isBackwardsJump() || !is.isConditionalJump() || !is.isRelativeJump())
                continue;

            // try to hash it
            else {

                // absorb as many instructions as there are in the delay slot
                var delay = is.getDelay();
                while (delay > 0) {
                    window.add(this.istream.nextInstruction());
                    delay--;
                }

                // try to hash the possible candidate terminated by backwards branch "is"
                checkCandidate(window, is);
            }

        } while (istream.hasNext());

        // for all valid hashed sequences, make the StaticBasicBlock objects
        makeBasicBlocks();

        // finally, init some stats
        this.totalCycles = istream.getCycles();
        this.numInsts = istream.getNumInstructions();

        return loops;
    }

    @Override
    public float getCoverage(int segmentSize) {
        // TODO Auto-generated method stub
        return 0;
    }
}
