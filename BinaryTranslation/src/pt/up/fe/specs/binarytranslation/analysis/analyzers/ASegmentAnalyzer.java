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

package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsLogs;

public abstract class ASegmentAnalyzer {
    protected ATraceInstructionStream stream;
    protected ZippedELFProvider elf;
    protected Class<? extends SegmentDetector> detectorClass = TraceBasicBlockDetector.class;
    private List<List<Instruction>> precalculatedSegments = new ArrayList<>();
    private List<BinarySegment> segments = new ArrayList<>();
    protected int window = 0;

    public ASegmentAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        this.stream = stream;
        this.elf = elf;
        this.window = window;
    }

    public ASegmentAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window,
            Class<? extends SegmentDetector> detectorClass) {
        this.stream = stream;
        this.elf = elf;
        this.window = window;
        this.detectorClass = detectorClass;
    }

    public ASegmentAnalyzer(List<List<Instruction>> basicBlocks) {
        this.precalculatedSegments = basicBlocks;
    }

    protected List<BinarySegment> getSegments() {
        if (this.precalculatedSegments.size() == 0) {
            System.out.println(elf.getFilename() + ": building detector for window size " + window);

            var startAddr = stream.getApp().getKernelStart();
            var stopAddr = stream.getApp().getKernelStop();
            stream.runUntil(startAddr);

            var config = new DetectorConfigurationBuilder()
                    .withMaxWindow(window)
                    .withStartAddr(startAddr)
                    .withPrematureStopAddr(stopAddr)
                    .build();
            try {
                Constructor<? extends SegmentDetector> cons = detectorClass.getConstructor(DetectorConfiguration.class);
                var detector = (SegmentDetector) cons.newInstance(config);
                segments = AnalysisUtils.getSegments(stream, detector);

            } catch (Exception e) {
                SpecsLogs.warn("Error message:\n", e);
            }
        } else {
            // return precalculated
        }
        return segments;
    }

    protected List<List<Instruction>> getSegmentsAsList() {
        if (precalculatedSegments.size() == 0) {
            var bbs = getSegments();
            for (var bb : bbs) {
                precalculatedSegments.add(bb.getInstructions());
            }
        }
        return precalculatedSegments;
    }
}
