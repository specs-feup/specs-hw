package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnit;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGenerator;
import pt.up.fe.specs.binarytranslation.utils.SlidingWindow;

public class GenericTraceSegmentDetector implements TraceSegmentDetector {

    private final StreamUnitGenerator tracer;
    private final DetectorConfiguration config;

    public GenericTraceSegmentDetector(TraceInstructionStream tstream, DetectorConfiguration config) {
        this.config = config;
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

    // TODO: pass detection rules to "detectSegments"
    // e.g. only one backwards branch permitted, no stores permited, etc

    @Override
    public ArrayList<StreamUnitPattern> detectSegments() {

        // max pattern size
        var maxsize = this.getConfig().getMaxsize();

        // window of trace units
        var liveWindow = new SlidingWindow<StreamUnit>(maxsize);
        var fetchWindow = new SlidingWindow<StreamUnit>(maxsize * 2);

        // init comparison lists
        var fifos = new ArrayList<SlidingWindow<Boolean>>(maxsize);
        for (int i = 0; i < maxsize - 1; i++) {
            fifos.add(new SlidingWindow<Boolean>(i + 1));
            fifos.get(i).addHead(false);
        }

        // match bits
        var matchBits = new ArrayList<Boolean>(maxsize);
        for (int i = 0; i < maxsize; i++)
            matchBits.add(false);

        // advance the stream to skip addr
        if (this.getConfig().getSkipToAddr().longValue() != -1)
            this.tracer.advanceTo(this.getConfig().getSkipToAddr().longValue());

        /*
         * TODO: Best way to store detected patterns???
         */
        var detectedPatterns = new ArrayList<StreamUnitPattern>();

        while (tracer.hasNext()) {

            // new unit
            var nextUnit = tracer.nextBasicBlock();

            // compare to existing
            for (int i = 0; i < liveWindow.getCurrentSize(); i++) {
                var ithPrevUnit = liveWindow.get(i);
                var currentCompare = nextUnit.equals(ithPrevUnit);
                if (i == 0) {
                    matchBits.set(i, currentCompare); // && fifos.get(i).getLast());
                } else {
                    var i2 = i - 1;
                    matchBits.set(i, currentCompare && fifos.get(i2).getLast());
                    fifos.get(i2).addHead(currentCompare);
                }
            }

            // TODO: store only streamunit start addr and re-read later???

            // look for smallest pattern match (unroll later with transforms)
            for (int i = 0; i < maxsize; i++) {
                if (matchBits.get(i)) {
                    var pattern = new StreamUnitPattern(fetchWindow.getRange(i, 2 * i));
                    var idx = detectedPatterns.indexOf(pattern);
                    if (idx == -1)
                        detectedPatterns.add(pattern);
                    else
                        detectedPatterns.get(idx).incrementOccurenceCounter();

                    // done
                    break;
                }
            }

            /*
            // look for largest pattern match
            for (int i = maxsize - 1; i >= 0; i--) {
                if (matchBits.get(i)) {
                    var pattern = new StreamUnitPattern(fetchWindow.getRange(i, 2 * i));
                    var idx = detectedPatterns.indexOf(pattern);
                    if (idx == -1)
                        detectedPatterns.add(pattern);
                    else
                        detectedPatterns.get(idx).incrementOccurenceCounter();
            
                    // TODO: class for DetectedPattern, with a hash, so i can
                    // detect many times during execution and increase a counter
            
                    // System.out.println("haha");
                }
            }*/

            // add unit to window
            liveWindow.addHead(nextUnit);
            fetchWindow.addHead(nextUnit);

        }
        return detectedPatterns; // tmp
    }
}
