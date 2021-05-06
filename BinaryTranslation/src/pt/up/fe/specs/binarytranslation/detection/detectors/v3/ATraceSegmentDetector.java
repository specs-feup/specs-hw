package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnit;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGenerator;

public abstract class ATraceSegmentDetector implements TraceSegmentDetector {

    private final StreamUnitGenerator tracer;
    private final InstructionStream tstream;
    private final DetectorConfiguration config;

    public ATraceSegmentDetector(TraceInstructionStream tstream, DetectorConfiguration config) {
        this.config = config;
        this.tstream = tstream;
        this.tracer = new StreamUnitGenerator(tstream);
    }

    public DetectorConfiguration getConfig() {
        return config;
    }

    /*
    public enum DetectState {
        PATTERN_STOPED,
        PATTERN_STARTED,
        PATTERN_CHANGED_SIZES,
        PATTERN_UNCHANGED,
        NO_PATTERN
    }
    
    
    private List<StreamUnit> getPattern(SlidingWindow<StreamUnit> window) {
        var maxsize = this.getConfig().getMaxsize();
        var minsize = this.getConfig().getMinsize();
    
        for (int i = minsize; i <= maxsize; i++) {
    
        }
    }*/

    @Override
    public SegmentBundle detectSegments() {

        // window of trace units
        var traceWindow = new SlidingWindow<StreamUnit>(this.getConfig().getMaxsize());

        // windows of respective hashcodes of all size ranges
        /*
        var maxsize = this.getConfig().getMaxsize();
        var hashWindows = new ArrayList<SlidingWindow<Integer>>();
        for (int i = 0; i < maxsize; i++)
            hashWindows.add(new SlidingWindow<Integer>(i));
            */

        // subHashes of several sizes (i.e, index one is subhash for exiting pattern of size 1, etc)
        var maxsize = this.getConfig().getMaxsize();
        var hashList = new ArrayList<Integer>(maxsize);

        while (tracer.hasNext()) {

            // add unit to window
            var nextUnit = tracer.nextBasicBlock();
            traceWindow.add(nextUnit);

            // compare to existing (prioritize larger pattern)
            for (int i = maxsize; i >= 0; i--) {
                var newSubHash = traceWindow.getRangeHash(0, i);
                if(newSubHash == hashList.get(i))
                    
            }
            
            // update sub hashes
            var subHash = 0;
            for (int i = 0; i < maxsize; i++) {
                subHash += traceWindow.get(i).hashCode();
                hashList.set(i, subHash);
            }

            /*
            // add hash to all window sizes
            var hash = nextUnit.hashCode();
            for (var hWindow : hashWindows)
                hWindow.add(hash);
            
            // compare
            for (var hWindow : hashWindows) {
                var subHash = traceWindow
            }
            */

            // if pattern, turn pattern into graph
            /*if (getPattern(traceWindow)) {
                // TODO: StreamUnitGraphGenerator? after a pattern is detected?
                // useful for multipath?
            }*/

        }
    }
}
