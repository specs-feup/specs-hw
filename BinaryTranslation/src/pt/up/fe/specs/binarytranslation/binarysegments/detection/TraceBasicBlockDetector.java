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

        // make 1st window
        for (int i = 0; i < this.maxsize; i++)
            window.add(this.istream.nextInstruction());

        // process entire stream
        do {
            // sequences in this window
            hashSequences(window);

            // shift window (i.e. new window)
            int i = 0;
            int atomicity = window.get(0).getDelay();
            for (i = 0; i < this.maxsize - 1 - atomicity; i++)
                window.set(i, window.get(i + 1 + atomicity));

            // new instructions in window
            for (; i < this.maxsize; i++)
                window.set(i, this.istream.nextInstruction());

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
