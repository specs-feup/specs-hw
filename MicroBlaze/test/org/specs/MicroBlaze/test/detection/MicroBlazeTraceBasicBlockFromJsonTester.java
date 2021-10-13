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
