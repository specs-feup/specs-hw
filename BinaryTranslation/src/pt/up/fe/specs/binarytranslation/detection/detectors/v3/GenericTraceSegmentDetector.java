package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;
import java.util.Collections;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.v3.PatternDetectorV2.PatternState;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnit;
import pt.up.fe.specs.binarytranslation.tracer.StreamUnitGenerator;
import pt.up.fe.specs.binarytranslation.utils.SlidingWindow;

public class GenericTraceSegmentDetector implements TraceSegmentDetector {

    private enum TraceSegmentDetectorState {
        WAITING,
        RECORDING
    }

    private final StreamUnitGenerator tracer;
    private final DetectorConfiguration config;
    private final PatternDetectorV2 patdetect;

    public GenericTraceSegmentDetector(TraceInstructionStream tstream, DetectorConfiguration config) {
        this.config = config;
        this.tracer = new StreamUnitGenerator(tstream);
        this.patdetect = new PatternDetectorV2(config.getMaxsize());
    }

    public GenericTraceSegmentDetector(TraceInstructionStream tstream) {
        this(tstream, DetectorConfigurationBuilder.defaultConfig());
    }

    public DetectorConfiguration getConfig() {
        return config;
    }

    private StreamUnit nextUnit() {
        return this.tracer.nextBasicBlock();
        // return this.tracer.nextInstruction();
    }

    @Override
    public ArrayList<StreamUnitPattern> detectSegments() {

        // max pattern size
        var maxsize = this.getConfig().getMaxsize();

        // advance the stream to skip addr
        if (this.getConfig().getSkipToAddr().longValue() != -1)
            this.tracer.advanceTo(this.getConfig().getSkipToAddr().longValue());

        // TODO: Best way to store detected patterns???
        var detectedPatterns = new ArrayList<StreamUnitPattern>();
        StreamUnitPattern currentPattern = null;
        var traceCounter = 0;

        // window of trace units
        var fetchWindow = new SlidingWindow<StreamUnit>(maxsize * 2);

        StreamUnit nextUnit = null;
        while ((nextUnit = this.nextUnit()) != null) {

            // new unit
            var state = this.patdetect.step(nextUnit.hashCode());

            /*
             * Starting
             */
            // if (currentPattern == null && state == PatternState.PATTERN_STARTED) {
            if (state == PatternState.PATTERN_STARTED || state == PatternState.PATTERN_CHANGED_SIZES) {
                var idx = patdetect.getCurrentPatternSize() - 1;
                var list = fetchWindow.getRange(idx, 2 * idx);

                // reverse order
                Collections.reverse(list);

                // sort by lowest start addr
                var lowest = list.stream()
                        .min((a, b) -> a.getStart().getAddress()
                                .compareTo(b.getStart().getAddress()));
                var minidx = list.indexOf(lowest.get());
                Collections.rotate(list, minidx);

                // candidate pattern
                currentPattern = new StreamUnitPattern(list);

                // see if equivalent pattern already stored
                int pidx = detectedPatterns.indexOf(currentPattern);
                if (pidx != -1) {
                    currentPattern = detectedPatterns.get(pidx);
                    currentPattern.incrementOccurenceCounter();
                    currentPattern.incrementOccurenceCounter();
                } else {
                    detectedPatterns.add(currentPattern);
                    currentPattern.incrementOccurenceCounter();
                }
            }

            /*
             * Counting iterations
             */
            else if (state == PatternState.PATTERN_UNCHANGED) {
                traceCounter++;
                if (traceCounter == currentPattern.getPatternSize()) {
                    currentPattern.incrementOccurenceCounter();
                    traceCounter = 0;
                }
            }

            /*
             * Stopping iterations
             */
            else if (state == PatternState.PATTERN_STOPPED) {
                traceCounter = 0;
                currentPattern = null;
            }

            // add unit to window
            fetchWindow.addHead(nextUnit);
        }

        return detectedPatterns;
    }

    // TODO: pass detection rules to "detectSegments"
    // e.g. only one backwards branch permitted, no stores permited, etc

    /*
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
    
     // TODO: Best way to store detected patterns???
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
        }
    
        // add unit to window
        liveWindow.addHead(nextUnit);
        fetchWindow.addHead(nextUnit);
    
    }
    return detectedPatterns; // tmp
    }*/
}
