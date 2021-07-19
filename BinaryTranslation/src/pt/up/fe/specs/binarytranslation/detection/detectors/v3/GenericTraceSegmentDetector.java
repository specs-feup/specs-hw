package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;
import java.util.HashMap;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnit;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGenerator;

public class GenericTraceSegmentDetector implements TraceSegmentDetector {

    private final StreamUnitGenerator tracer;
    private final InstructionStream tstream;
    private final DetectorConfiguration config;

    public GenericTraceSegmentDetector(TraceInstructionStream tstream, DetectorConfiguration config) {
        this.config = config;
        this.tstream = tstream;
        this.tracer = new StreamUnitGenerator(tstream);
    }

    public GenericTraceSegmentDetector(TraceInstructionStream tstream) {
        this(tstream, DetectorConfigurationBuilder.defaultConfig());
    }

    public DetectorConfiguration getConfig() {
        return config;
    }

    /*private void trackPattern(List<StreamUnit> pattern) {
        
    }*/

    @Override
    public SegmentBundle detectSegments() {

        // max pattern size
        var maxsize = this.getConfig().getMaxsize();

        // window of trace units
        var liveWindow = new SlidingWindow<StreamUnit>(maxsize);
        var fetchWindow = new SlidingWindow<StreamUnit>(maxsize * 2);

        // comparison lists
        var fifos = new ArrayList<SlidingWindow<Boolean>>(maxsize);
        for (int i = 0; i < maxsize - 1; i++) {
            fifos.add(new SlidingWindow<Boolean>(i + 1));
            fifos.get(i).add(false);
        }

        // match bits
        var matchBits = new ArrayList<Boolean>(maxsize);
        for (int i = 0; i < maxsize; i++)
            matchBits.add(false);

        // aux
        var detected = new HashMap<ArrayList<StreamUnit>, Integer>();
        var iterations = new HashMap<ArrayList<StreamUnit>, Integer>();

        while (tracer.hasNext()) {

            // new unit
            var nextUnit = tracer.nextBasicBlock();

            // compare to existing (prioritize larger pattern)
            for (int i = 0; i < liveWindow.getCurrentSize(); i++) {
                var ithPrevUnit = liveWindow.get(i);
                var currentCompare = nextUnit.equals(ithPrevUnit);
                if (i == 0) {
                    matchBits.set(i, currentCompare && fifos.get(i).getLast());
                } else {
                    var i2 = i - 1;
                    matchBits.set(i, currentCompare && fifos.get(i2).getLast());
                    fifos.get(i2).add(currentCompare);
                }
            }

            // look for largest pattern match
            for (int i = maxsize - 1; i >= 0; i--) {
                if (matchBits.get(i)) {
                    var pattern = fetchWindow.getRange(i, 2 * i);
                    System.out.println("haha");
                }
            }

            // add unit to window
            liveWindow.add(nextUnit);
            fetchWindow.add(nextUnit);

        }
        return null; // tmp
    }
}
