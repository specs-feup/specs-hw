/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.memory.AddressGenerationUnit;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.memory.InductionVariablesDetector;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class StreamingAnalyzer extends ASegmentAnalyzer {

    public StreamingAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        super(stream, elf, window);
    }

    public void analyze(int iter) {
        var segs = this.getSegments();

        for (var bb : segs) {
            var mad = new MemoryAddressDetector(bb);
            var graphs = mad.detectGraphs();
            var ivd = new InductionVariablesDetector(bb);
            var indVars = ivd.detectVariables(graphs, false);
            
            for (var graph : graphs) {
                var agu = new AddressGenerationUnit(graph, indVars);
                var map = agu.getInputMap();
                
                for (var key : map.keySet()) {
                    var rand = ThreadLocalRandom.current().nextInt(4000, 6000);
                    rand = (rand / 4) * 4;  //Ensure it's a multiple of 4
                    map.put(key, rand);
                }
                for (int i = 0; i < iter; i++) {
                    String msg = getInputString(i, map);
                    var addr = agu.next(map);
                    System.out.println("{" + msg + "} -> AGU -> 0x" + Long.toHexString(addr));
                }
                System.out.println("--------");
            }
        }
    }

    private String getInputString(int i, Map<String, Integer> map) {
        var arr = new ArrayList<String>();
        arr.add("i = " + i);
        for (var key : map.keySet()) {
            arr.add(key + " = 0x" + Long.toHexString(map.get(key)));
        }
        return String.join(", ", arr);
    }
}
