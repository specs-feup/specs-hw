package org.specs.MicroBlaze.test.detection;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.detection.segments.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeTraceBasicBlockFromJsonTester {

    @Test
    public void readFromJson() {
        var tracebb = (TraceBasicBlock) BinaryTranslationUtils.fromJSON(
                "output/TRACE_BASIC_BLOCK_13012021"
                        + "/hydro2d_N100.elf/TRACE_BASIC_BLOCK/windowSize17/"
                        + "bsg/BinarySegmentGraph_477289012_13012021/"
                        + "TraceBasicBlock_366590980_13012021/TraceBasicBlock366590980.json",
                TraceBasicBlock.class);

        var graph = BinarySegmentGraph.newInstance(tracebb);
    }

    //
}
