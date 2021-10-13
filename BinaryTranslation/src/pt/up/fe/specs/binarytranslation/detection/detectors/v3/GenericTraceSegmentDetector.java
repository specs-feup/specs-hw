/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
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
        // return this.tracer.nextSuperBlock();
        return this.tracer.nextBasicBlock();
        // return this.tracer.nextInstruction();
    }

    // TODO: pass detection rules to "detectSegments"
    // e.g. only one backwards branch permitted, no stores permited, etc

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
}
