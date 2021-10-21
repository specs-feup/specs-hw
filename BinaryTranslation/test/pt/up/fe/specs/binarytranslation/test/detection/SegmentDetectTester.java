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

package pt.up.fe.specs.binarytranslation.test.detection;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.v3.GenericTraceSegmentDetector;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public class SegmentDetectTester {

    /*
     * common options (Children may override this at leisure)
     */
    protected static DetectorConfigurationBuilder getConfig(InstructionStream istream) {
        var builder = new DetectorConfigurationBuilder();
        builder.withSkipToAddr(istream.getApp().getKernelStart());
        builder.withPrematureStopAddr(istream.getApp().getKernelStop());
        builder.withStartAddr(istream.getApp().getKernelStart());
        builder.withStopAddr(istream.getApp().getKernelStop());
        return builder;
    }

    /*
     * Static Frequent Sequence
     */
    protected static void testFrequentStaticSequenceDetector(StaticInstructionStream istream) {
        var detector = new FrequentStaticSequenceDetector(getConfig(istream).withMaxWindow(3).build());
        var bundle = detector.detectSegments(istream);
        bundle.printBundle();
    }

    /*
     * Trace Frequent Sequence
     */
    protected static void testFrequentTraceSequenceDetector(TraceInstructionStream istream) {
        var detector = new FrequentTraceSequenceDetector(getConfig(istream).withMaxWindow(10).build());
        var bundle = detector.detectSegments(istream);
        bundle.printBundle();
    }

    /*
     * Static Basic Block
     */
    protected static void testStaticBasicBlockDetector(StaticInstructionStream istream) {
        var detector = new StaticBasicBlockDetector(getConfig(istream).withMaxWindow(6).build());
        var bundle = detector.detectSegments(istream);
        bundle.printBundle();
    }

    /*
     * Trace Basic Block
     */
    protected static void testTraceBasicBlockDetector(TraceInstructionStream istream) {
        var detector = new TraceBasicBlockDetector(getConfig(istream).withMaxWindow(12).build());
        var bundle = detector.detectSegments(istream);
        bundle.printBundle();
    }

    /*
     * GenericTraceSegment (v3 detector WIP)
     */
    public static void testGenericTraceDetector(TraceInstructionStream istream) {
        var config = getConfig(istream).withMaxWindow(20).build();
        var detector = new GenericTraceSegmentDetector(istream, config);
        var list = detector.detectSegments();
        for (var el : list) {
            System.out.println(el.toString());
        }
    }
}
