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

package org.specs.MicroBlaze.test.analysis;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.reporters.ReporterDataFlow;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.reporters.ReporterScheduling;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegmentType;
import pt.up.fe.specs.util.SpecsLogs;

public class MicroBlazeMegaBlockSchedulingTest {
    
    @Test
    public void testUnrollingMegaBlockDataFlow() {
        int factors[] = { 1, 2, 3, 4, 5 };

        var elfs = MicroBlazeBasicBlockInfo.getPolybenchMiniFloatKernels();
        var streams = MicroBlazeBasicBlockSchedulingTest.buildStreams(elfs);
        var analyzer = new ReporterDataFlow(elfs, streams);
        
        try {
            analyzer.analyze(factors);
        } catch (Exception e) {
            SpecsLogs.warn("Error message:\n", e);
        }
    }
    
    @Test
    public void testSchedulingMegaBlock() {
        int factors[] = { 1/*, 2, 3, 4, 5*/ };
        boolean dependencies = false;
        
        var alus = new ArrayList<Integer>(List.of(2));
        var memPorts = new ArrayList<Integer>(List.of(2));
        var elfs = MicroBlazeMegaBlockInfo.getLivermoreN100Kernels();
        var streams = MicroBlazeBasicBlockSchedulingTest.buildStreams(elfs);
        var analyzer = new ReporterScheduling(elfs, streams, alus, memPorts, dependencies, BinarySegmentType.MEGA_BLOCK);
        
        try {
            analyzer.analyze(factors);
        } catch (Exception e) {
            SpecsLogs.warn("Error message:\n", e);
        }
    }
}
