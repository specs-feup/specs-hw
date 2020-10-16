package pt.up.fe.specs.binarytranslation.detection.detectors;

import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.detection.segments.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

/**
 * Detects all basic blocks in an instruction trace; trace must be provided by any implementation of
 * {@link ATraceInstructionStream}
 * 
 * @author NunoPaulino
 *
 */
public class TraceBasicBlockDetector extends ABasicBlockDetector {

    /*
     * 
     */
    public TraceBasicBlockDetector() {
        super();
    }

    /*
     * 
     */
    @Override
    protected BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts,
            Application app) {
        return new TraceBasicBlock(symbolicseq, contexts, app);
    }
}
