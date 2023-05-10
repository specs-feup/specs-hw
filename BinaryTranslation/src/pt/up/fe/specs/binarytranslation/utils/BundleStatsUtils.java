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

import java.io.File;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.elf.ELFProvider;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;

public class BundleStatsUtils {

    /*
     * Returns an average of a given getValue metric
     */
    public static float getAverageOf(GraphBundle gbundle,
            Function<BinarySegmentGraph, Integer> getvalue) {
        return getAverageOf(gbundle, getvalue, predicate -> true);
    }

    /*
     * Returns an average of a given getValue metric, with predication
     */
    public static float getAverageOf(GraphBundle gbundle,
            Function<BinarySegmentGraph, Integer> getvalue,
            Predicate<BinarySegmentGraph> predicate) {

        float avg = 0;
        for (BinarySegmentGraph seg : gbundle.getGraphs()) {
            if (predicate.test(seg))
                avg += getvalue.apply(seg);
        }

        return avg /= gbundle.getGraphs().size();
    }

    /*
     * Returns the max of a given getValue metric
     */
    public static float getMaxOf(GraphBundle gbundle,
            Function<BinarySegmentGraph, Integer> getvalue) {
        return getMaxOf(gbundle, getvalue, predicate -> true);
    }

    /*
     * Returns the max of a given getValue metric, with predication
     */
    public static float getMaxOf(GraphBundle gbundle,
            Function<BinarySegmentGraph, Integer> getvalue,
            Predicate<BinarySegmentGraph> predicate) {

        float max = 0;
        for (BinarySegmentGraph seg : gbundle.getGraphs()) {
            if (predicate.test(seg))
                if (max < getvalue.apply(seg))
                    max = getvalue.apply(seg);
        }

        return max;
    }

    /*
     * Returns the min of a given getValue metric
     */
    public static float getMinOf(GraphBundle gbundle,
            Function<BinarySegmentGraph, Integer> getvalue) {
        return getMinOf(gbundle, getvalue, predicate -> true);
    }

    /*
     * Returns the min of a given getValue metric, with predication
     */
    public static float getMinOf(GraphBundle gbundle,
            Function<BinarySegmentGraph, Integer> getvalue,
            Predicate<BinarySegmentGraph> predicate) {

        float min = 0;
        for (BinarySegmentGraph seg : gbundle.getGraphs()) {
            if (predicate.test(seg))
                if (min < getvalue.apply(seg))
                    min = getvalue.apply(seg);
        }

        return min;
    }

    /*
     * 
     */
    public static void bundleStatsDump(ELFProvider elf, List<SegmentBundle> segs) {
        BundleStatsUtils.bundleStatsDump(elf, segs, false);
    }

    /*
     * 
     */
    public static void bundleStatsDump(ELFProvider elf, List<SegmentBundle> segs, boolean fileOutput) {

        System.out.println("elf, window, nrgraphs, "
                + "avgcontexts, maxcontexts, "
                + "avgcpl, maxcpl, "
                + "avgilp, maxilp, "
                + "avgipc, maxipc, "
                + "avglds, maxlds, "
                + "avgsts, maxsts, "
                + "avgII, maxII");

        for (var seg : segs) {
            var gbundle = GraphBundle.newInstance(seg);
            if (gbundle.getSegments().size() == 0) {
                // System.out.println(seg.getApplicationInformation().getAppName()
                // + ", " + 0 + ", " + 0);
                continue;
            }

            if (fileOutput == true) {
                var parentfolder = new File("./output/" + elf.getFilename()
                        + "/" + gbundle.getSegments().get(0).getSegmentType().toString()
                        + "/windowSize" + gbundle.getSegments().get(0).getSegmentLength());
                gbundle.generateOutput(parentfolder);
            }

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

            // contexts
            var maxctx = BundleStatsUtils.getMaxOf(
                    gbundle, func -> Integer.valueOf(func.getSegment().getContexts().size()));
            var avgctx = BundleStatsUtils.getAverageOf(
                    gbundle, func -> Integer.valueOf(func.getSegment().getContexts().size()));

            // lds
            var maxlds = BundleStatsUtils.getMaxOf(
                    gbundle, func -> Integer.valueOf(func.getNumLoads()));
            var avglds = BundleStatsUtils.getAverageOf(
                    gbundle, func -> Integer.valueOf(func.getNumLoads()));

            // sts
            var maxsts = BundleStatsUtils.getMaxOf(
                    gbundle, func -> Integer.valueOf(func.getNumStores()));
            var avgsts = BundleStatsUtils.getAverageOf(
                    gbundle, func -> Integer.valueOf(func.getNumStores()));

            // II
            var avgII = BundleStatsUtils.getMaxOf(
                    gbundle, func -> Integer.valueOf(func.getInitiationInterval()));
            var maxII = BundleStatsUtils.getAverageOf(
                    gbundle, func -> Integer.valueOf(func.getInitiationInterval()));

            System.out.println(gbundle.getApplicationInformation().getAppName()
                    + ", " + gbundle.getSegments().get(0).getSegmentLength()
                    + ", " + gbundle.getSegments().size()
                    + ", " + avgctx + ", " + maxctx
                    + ", " + avgcpl + ", " + maxcpl
                    + ", " + avgilp + ", " + maxilp
                    + ", " + avgipc / 10.0f + ", " + maxipc / 10.0f
                    + ", " + avglds + ", " + maxlds
                    + ", " + avgsts + ", " + maxsts
                    + ", " + avgII + ", " + maxII);
        }
    }
}
