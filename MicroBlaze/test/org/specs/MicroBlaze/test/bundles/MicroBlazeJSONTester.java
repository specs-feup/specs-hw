package org.specs.MicroBlaze.test.bundles;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;

public class MicroBlazeJSONTester {

    @Test
    public void bundleStaticSegmentBundle() {
        try (MicroBlazeElfStream el = new MicroBlazeElfStream(MicroBlazeLivermoreELFN10.cholesky)) {
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
