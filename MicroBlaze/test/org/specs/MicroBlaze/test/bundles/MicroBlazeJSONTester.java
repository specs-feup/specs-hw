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
 
package org.specs.MicroBlaze.test.bundles;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;

public class MicroBlazeJSONTester {

    @Test
    public void bundleStaticSegmentBundle() {
        try (MicroBlazeElfStream el = new MicroBlazeElfStream(MicroBlazeLivermoreN10.cholesky)) {
            var bbd = new FrequentStaticSequenceDetector();
            var bundle = bbd.detectSegments(el);

            // create JSON object
            bundle.toJSON();

            // var pathname = bundle.getJSONName();

            // read into new object
            // var fromjson = BinaryTranslationUtils.fromJSON(pathname, SegmentBundle.class);

            // assertEquals(bundle, fromjson);
        }
    }

    // @Test
    // public void bundleStaticGraphBundle() {
    // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky.txt");
    // fd.deleteOnExit();
    // try (MicroBlazeElfStream el = new MicroBlazeElfStream(fd)) {
    // var bbd = new FrequentStaticSequenceDetector();
    // var bundle = bbd.detectSegments(el);
    // var gbundle = GraphBundle.newInstance(bundle);
    //
    // // create JSON object
    // gbundle.toJSON();
    // }
    // }
}
