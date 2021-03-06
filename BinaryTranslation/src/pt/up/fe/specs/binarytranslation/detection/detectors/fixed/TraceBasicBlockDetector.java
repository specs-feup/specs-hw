package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
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
public class TraceBasicBlockDetector extends AFixedSizeBasicBlockDetector {

    /*
     * 
     */
    public TraceBasicBlockDetector() {
        super(DetectorConfigurationBuilder.defaultConfig());
    }

    public TraceBasicBlockDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * 
     */
    @Override
    protected BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new TraceBasicBlock(symbolicseq, contexts, this.getCurrentStream().getApp());
    }
}
