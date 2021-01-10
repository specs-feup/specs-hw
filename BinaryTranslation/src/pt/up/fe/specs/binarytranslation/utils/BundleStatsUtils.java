package pt.up.fe.specs.binarytranslation.utils;

import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;

public class BundleStatsUtils {

    /* public static BinaryTranslationOutput gatherStats(SegmentBundle segbundle) {
    
    }*/

    /* public static BinaryTranslationOutput gatherStats(GraphBundle graphbundle) {
    
    }*/

    /*
     * Returns average ideal instructions per clock cycle
     */
    public static float getAverageIPC(GraphBundle gbundle) {
        return getAverageIPC(gbundle, predicate -> true);
    }

    /*
     * Returns average ideal instructions per clock cycle, with predication
     */
    public static float getAverageIPC(GraphBundle gbundle, Predicate<BinarySegmentGraph> predicate) {
        float avg = 0;
        for (BinarySegmentGraph seg : gbundle.getGraphs()) {
            if (predicate.test(seg))
                avg += seg.getEstimatedIPC();
        }

        return avg /= gbundle.getGraphs().size();
    }

    /*
     * Returns average ideal path length
     */
    public static float getAverageCPL(GraphBundle gbundle) {
        return getAverageCPL(gbundle, predicate -> true);
    }

    /*
     *Returns average ideal path length, with predication
     */
    public static float getAverageCPL(GraphBundle gbundle, Predicate<BinarySegmentGraph> predicate) {
        float avg = 0;
        for (BinarySegmentGraph seg : gbundle.getGraphs()) {
            if (predicate.test(seg))
                avg += seg.getCpl();
        }

        return avg /= gbundle.getGraphs().size();
    }
}
