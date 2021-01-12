package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeStaticProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;
import pt.up.fe.specs.binarytranslation.utils.BundleStatsUtils;

public class MicroBlazeBatchFrequentSequenceTest {

    /*
     * Stats from frequent sequences
     */
    @Test
    public void MicroBlazeFrequentSequenceDetect() {

        for (var elf : MicroBlazeLivermoreELFN100.values()) {
            // for (var elf : Arrays.asList(MicroBlazeLivermoreELFN100.innerprod100)) {

            var segs = ThreadedSegmentDetectUtils.getSegments(elf, 12,
                    MicroBlazeStaticProvider.class,
                    MicroBlazeElfStream.class,
                    FrequentStaticSequenceDetector.class);

            BundleStatsUtils.bundleStatsDump(elf, segs);
        }
    }
}
