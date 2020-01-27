package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.Iterator;
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
     * max size of window for capture of basicblock 
     */
    private final int maxsize = 20;

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

    private void hashSequences(List<Instruction> window) {

        var baseaddr = window.get(0).getAddress().intValue();
        Iterator<Instruction> it = window.iterator();

        // look for backwards branches in window, which has size up to this.maxsize
        while (it.hasNext()) {
            var is = it.next();

            if (is.isBackwardsJump() == false)
                continue;

            if (is.isConditionalJump() == false)
                continue;

            if (is.isRelativeJump() == false)
                continue;

            // Address of the backwards branch
            int thisAddr = is.getAddress().intValue();
            int startAddr = is.getBranchTarget().intValue();

            // backwards branch has left the window
            if (startAddr < baseaddr)
                continue;

            // compute indexes
            int sidx = (startAddr - baseaddr) / this.istream.getInstructionWidth();
            int eidx = (thisAddr - baseaddr) / this.istream.getInstructionWidth();

            // cant get candidate if delay goes beyond window
            int delay = is.getDelay();
            if (eidx + delay > window.size())
                continue;

            // sub-window
            List<Instruction> candidate = window.subList(sidx, eidx);

            // create new candidate hash sequence
            var newseq = BinarySegmentDetectionUtils.hashSequence(candidate);

            // add sequence to occurrence counters (counting varies between static to trace detection)
            BinarySegmentDetectionUtils.addAddrToList(this.addrs, newseq);

            // add sequence to map which is indexed by hashCode + startaddr
            BinarySegmentDetectionUtils.addHashSequenceToList(this.hashed, newseq);
        }

    }

    @Override
    public List<BinarySegment> detectSegments() {

        // ALTERNATIVE:
        // read entire elf like the static detector
        // count only the backwards branch frequencies
        // then fetch the basic blocks using the StaticDetectorCode?

        List<Instruction> window = new ArrayList<Instruction>();

        // make 1st window
        for (int i = 0; i < this.maxsize; i++)
            window.add(this.istream.nextInstruction());

        // process entire stream
        do {
            // sequences in this window, from sizes minsize to maxsize
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
