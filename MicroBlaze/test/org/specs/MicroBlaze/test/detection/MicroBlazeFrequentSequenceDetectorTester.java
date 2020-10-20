/**
 * Copyright 2019 SPeCS.
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

package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

public class MicroBlazeFrequentSequenceDetectorTester {

    @Test
    public void testStatic() {
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(6);

        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/matmul.elf",
                MicroBlazeElfStream.class, FrequentStaticSequenceDetector.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }

    @Test
    public void testTrace() {
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxWindow(10).withMinWindow(2);

        var bundle = SegmentDetectTestUtils.detect("org/specs/MicroBlaze/asm/matmul.elf",
                MicroBlazeTraceStream.class, FrequentTraceSequenceDetector.class, builder.build());
        SegmentDetectTestUtils.printBundle(bundle);
    }
}
