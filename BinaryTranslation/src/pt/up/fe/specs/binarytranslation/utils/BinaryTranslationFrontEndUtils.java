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
 
package pt.up.fe.specs.binarytranslation.utils;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.elf.ELFProvider;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;

/**
 * Helper class for the front-end of the web based demonstrator. Receives elf filename, a type of stream class, and type
 * of detector class. Returns a @{GraphBundle}.
 * 
 * @author nuno
 *
 */
public class BinaryTranslationFrontEndUtils {

    public static GraphBundle doBackend(ELFProvider elf, Class<?> streamClass, Class<?> detectorClass) {

        //
        var istream = elf.toStaticStream();
        var builder = new DetectorConfigurationBuilder();
        builder.withSkipToAddr(istream.getApp().getKernelStart());
        builder.withPrematureStopAddr(istream.getApp().getKernelStop());
        builder.withStartAddr(istream.getApp().getKernelStart());
        builder.withStopAddr(istream.getApp().getKernelStop());

        SegmentDetector detector = null;
        if (detectorClass == FrequentStaticSequenceDetector.class) {
            detector = new FrequentStaticSequenceDetector(builder.withMaxWindow(3).build());

        } else if (detectorClass == StaticBasicBlockDetector.class) {
            detector = new StaticBasicBlockDetector(builder.withMaxWindow(6).build());
        }

        // get segment bundle
        var bundle = detector.detectSegments(istream);

        // transform into graph bundle
        var graphs = GraphBundle.newInstance(bundle);

        return graphs;
    }
}
