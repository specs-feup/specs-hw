package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeStaticProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.test.detection.ThreadedSegmentDetectUtils;

public class MicroBlazeBatchFrequentSequenceTest {

    /*
     * Stats from frequent sequences
     */
    @Test
    public void MicroBlazeFrequentSequenceDetect() {

        // System.out.println("elf, window, maxcpl, avgcpl, maxilp, avgilp, avgipc, maxipc");
        System.out.println("elf, window, nrgraphs");

        for (var elf : MicroBlazeLivermoreELFN100.values()) {
            // for (var elf : Arrays.asList(MicroBlazeLivermoreELFN10.innerprod)) {

            var segs = ThreadedSegmentDetectUtils.getSegments(elf, 20,
                    MicroBlazeStaticProvider.class,
                    MicroBlazeElfStream.class,
                    FrequentStaticSequenceDetector.class);

            for (var seg : segs) {
                var gbundle = GraphBundle.newInstance(seg);
                if (gbundle.getSegments().size() == 0) {
                    System.out.println(seg.getApplicationInformation().getAppName()
                            + ", " + 0 + ", " + 0);
                    continue;
                }

                System.out.println(seg.getApplicationInformation().getAppName()
                        + ", " + gbundle.getSegments().get(0).getSegmentLength()
                        + ", " + gbundle.getSegments().size());

                /*
                if (gbundle.getSegments().size() == 0) {
                    System.out.println(seg.getApplicationInformation().getAppName()
                            + ", " + 0 + ", " + 0 + ", " + 0 + ", " + 0 + ", " + 0);
                    continue;
                }
                
                gbundle.generateOutput();
                
                // cpl
                var maxcpl = BundleStatsUtils.getMaxOf(
                        gbundle, func -> Integer.valueOf(func.getCpl()));
                var avgcpl = BundleStatsUtils.getAverageOf(
                        gbundle, func -> Integer.valueOf(func.getCpl()));
                
                // ilp
                var maxilp = BundleStatsUtils.getMaxOf(
                        gbundle, func -> Integer.valueOf(func.getMaxwidth()));
                var avgilp = BundleStatsUtils.getAverageOf(
                        gbundle, func -> Integer.valueOf(func.getMaxwidth()));
                
                // ipc
                var maxipc = BundleStatsUtils.getMaxOf(
                        gbundle, func -> Integer.valueOf(Integer.valueOf((int) (func.getEstimatedIPC() * 10.0))));
                var avgipc = BundleStatsUtils.getAverageOf(
                        gbundle, func -> Integer.valueOf(Integer.valueOf((int) (func.getEstimatedIPC() * 10.0))));
                
                System.out.println(seg.getApplicationInformation().getAppName()
                        + ", " + gbundle.getSegments().get(0).getSegmentLength() + ", "
                        + maxcpl + ", " + avgcpl + ", " + maxilp + ", " + avgilp
                        + ", " + (float) avgipc / 10.0f + ", " + (float) maxipc / 10.0f);
                        */
            }
        }
    }
}
