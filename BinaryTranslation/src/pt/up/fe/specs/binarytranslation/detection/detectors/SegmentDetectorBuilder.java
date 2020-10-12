package pt.up.fe.specs.binarytranslation.detection.detectors;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegmentType;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class SegmentDetectorBuilder {

    /*
     * 
     */
    public static SegmentDetector buildDetector(InstructionStream istream, BinarySegmentType segtype) {

        SegmentDetector detector = null;
        switch (segtype) {
        case STATIC_FREQUENT_SEQUENCE:
            detector = new FrequentStaticSequenceDetector(istream);
            break;

        case TRACE_FREQUENT_SEQUENCE:
            detector = new FrequentTraceSequenceDetector(istream);
            break;

        case STATIC_BASIC_BLOCK:
            detector = new StaticBasicBlockDetector(istream);
            break;

        case TRACE_BASIC_BLOCK:
            detector = new TraceBasicBlockDetector(istream);
            break;

        case MEGA_BLOCK:
            break;

        default:
            break;
        }

        return detector;
    }
}
