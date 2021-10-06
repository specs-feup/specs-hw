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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeTraceDumpProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.reporters.ReporterDataFlow;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.reporters.ReporterScheduling;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.reporters.ReporterSummary;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsLogs;

public class MicroBlazeDataFlowTest {

    @Test
    public void testUnrollingBasicBlockDataFlow() {
        int factors[] = { 1, 2, 3, 4, 5 };

        var elfs = MicroBlazeBasicBlockInfo.getPolybenchMiniFloatKernels();
        var streams = buildStreams(elfs);
        var analyzer = new ReporterDataFlow(elfs, streams);
        
        try {
            analyzer.analyze(factors, "");
        } catch (Exception e) {
            SpecsLogs.warn("Error message:\n", e);
        }
    }

    @Test
    public void testScheduling() {
        int factors[] = { 1, 2, 3, 4, 5 };
        boolean dependencies = true;
        
        var alus = new ArrayList<Integer>(List.of(1, 2, 4, 8, 200));
        var memPorts = new ArrayList<Integer>(List.of(1, 2, 4, 8, 200));
        var elfs = MicroBlazeBasicBlockInfo.getPolybenchMiniFloatKernels();
        var streams = buildStreams(elfs);
        var analyzer = new ReporterScheduling(elfs, streams, alus, memPorts, dependencies);
        
        try {
            analyzer.analyze(factors, "");
        } catch (Exception e) {
            SpecsLogs.warn("Error message:\n", e);
        }
    }
    
    @Test
    public void testBenchmarkStatistics() {
        var elfs = MicroBlazeBasicBlockInfo.getPolybenchMiniFloatKernels();
        var streams = buildStreams(elfs);
        var analyzer = new ReporterSummary(elfs, streams);
        
        analyzer.analyze(1, "_Summary");
    }
    

    private HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>> buildStreams(Map<ZippedELFProvider, Integer[]> elfs) {
        var streams = new HashMap<ZippedELFProvider, HashMap<Integer, ATraceInstructionStream>>();
        for (var elf : elfs.keySet()) {
            var map = new HashMap<Integer, ATraceInstructionStream>();
            for (var i : elfs.get(elf)) {
                var stream = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
                map.put(i, stream);
            }
            streams.put(elf, map);
        }
        return streams;
    }
}
