package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;
import java.util.HashMap;

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

    /*private void trackPattern(List<StreamUnit> pattern) {
        
    }*/

    @Override
    public SegmentBundle detectSegments() {

        // window of trace units
        var traceWindow = new SlidingWindow<StreamUnit>(this.getConfig().getMaxsize());

        // subHashes of several sizes (i.e, index one is subhash for exiting pattern of size 1, etc)
        var maxsize = this.getConfig().getMaxsize();
        var hashList = new ArrayList<Integer>(maxsize);

        // aux
        var detected = new HashMap<ArrayList<StreamUnit>, Integer>();

        while (tracer.hasNext()) {

            // add unit to window
            var nextUnit = tracer.nextBasicBlock();
            traceWindow.add(nextUnit);

            // compare to existing (prioritize larger pattern)
            for (int i = maxsize; i >= 0; i--) {
                var newSubHash = traceWindow.getRangeHash(0, i);
                if (newSubHash == hashList.get(i)) {

                    // problem... when i detect the pattern, the traceWindow has the second repetition
                    // meaning I dotn have the initial register values...
                }
            }

            // update sub hashes
            var subHash = 0;
            for (int i = 0; i < maxsize; i++) {
                subHash += traceWindow.get(i).hashCode();
                hashList.set(i, subHash);
            }

        }
    }
}
