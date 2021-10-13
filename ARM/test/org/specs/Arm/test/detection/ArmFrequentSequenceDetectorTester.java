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
 
package org.specs.Arm.test.detection;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;

public class ArmFrequentSequenceDetectorTester {

    @Test
    public void testStatic() {
        var elfstream = ArmLivermoreN10.cholesky.toStaticStream();
        var bbdetect = new FrequentStaticSequenceDetector();
        var bundle = bbdetect.detectSegments(elfstream);
        bundle.printBundle(segment -> segment.getSegmentLength() == 4);
    }

    @Test
    public void testTrace() {
        var tracestream = ArmLivermoreN10.cholesky.toTraceStream();
        var bbdetect = new FrequentTraceSequenceDetector();
        var bundle = bbdetect.detectSegments(tracestream);
        bundle.printBundle();
    }
}
