package pt.up.fe.specs.binarytranslation.utils;

import java.util.function.Function;
import java.util.function.Predicate;

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
}
