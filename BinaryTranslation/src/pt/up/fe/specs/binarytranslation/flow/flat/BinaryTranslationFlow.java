package pt.up.fe.specs.binarytranslation.flow.flat;

/*
 * Class should hold all the desired steps, including multiple transformation steps on IR (?)
 */
public class BinaryTranslationFlow {
    /*
    // TODO: the detectors and stream profilers should receive a notification of a new incoming instruction and read it,
    // instead of causing the instruction stream to advance by themselves. this allows for multiple detectors and
    // profilers to run in paralell
    
    //
    private final String filename;
    private final BinarySegmentType segtype;
    
    // stream stage
    private final InstructionStream istream;
    
    // detect stage
    private final SegmentDetector detector;
    
    // TODO: transformation steps
    // private final List<GraphTransformation> gtransforms;
    
    // TODO: hardware gen steps
    // private final HardwareGenerator hwgen;
    
    // TODO ISSUE: the stream and detector cant be passed as already initialized...
    // I think I really need enums or a config list then call the constructors based on that step list??
    
    public BinaryTranslationFlow(String filename, Class<?> streamClass, Class<?> detectorClass) {
        this.filename = filename;
        this.segtype = segtype;
        this.istream = this.detector = SegmentDetectorBuilder.buildDetector(this.istream, segtype);
    }
    
    public BinaryTranslationFlow(InstructionStream istream, SegmentDetector detector) {
        this.istream = istream;
        this.detector = detector;
    }
    
    public InstructionStream getIstream() {
        return istream;
    }
    
    public SegmentDetector getDetector() {
        return detector;
    }*/
}
