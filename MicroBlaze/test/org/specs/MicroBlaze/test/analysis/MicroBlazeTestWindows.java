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

package org.specs.MicroBlaze.test.analysis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniFloat;
import org.specs.MicroBlaze.provider.MicroBlazeTraceDumpProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;

public class MicroBlazeTestWindows {
    @Test
    public void testDetectBasicBlock() {
        var elfs = MicroBlazeBasicBlockInfo.getPolybenchSmallFloatKernels();

        for (var elf : elfs.keySet()) {
            System.out.println("ELF: " + elf.getFilename());
            var windows = new ArrayList<Integer>(Arrays.asList(elfs.get(elf)));

            if (windows.size() == 0) {
                for (var i = 4; i < 20; i++)
                    windows.add(i);
            }

            for (var window : windows) {
                var istream1 = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
                istream1.silent(true);

                System.out.println("Looking for segments of size: " + window);

                var detector1 = new TraceBasicBlockDetector(
                        new DetectorConfigurationBuilder().withMaxWindow(window).build());
                var result1 = detector1.detectSegments(istream1);
                if (result1.getSegments().size() == 0)
                    continue;
                else {
                    result1.printBundle();
                    System.out.println(result1.getSummary());

                }
            }
        }
    }
    
    @Test
    public void testFindBasicBlockSizes() throws IOException {
        var res = new HashMap<MicroBlazePolyBenchMiniFloat, ArrayList<String>>();
        for (var elf : MicroBlazePolyBenchMiniFloat.values()) {
            var arr = new ArrayList<String>();
            res.put(elf, arr);
        }
        
        FileWriter f = new FileWriter("windows.txt");

        for (var elf : MicroBlazePolyBenchMiniFloat.values()) {
            int minWindow = 4;
            int maxWindow = 50;
            System.out.println("ELF: " + elf.getFilename());
            
            for (int window = minWindow; window <= maxWindow; window++) {
                var istream1 = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
                istream1.silent(true);

                var detector1 = new TraceBasicBlockDetector(
                        new DetectorConfigurationBuilder().withMaxWindow(window).build());
                var result1 = detector1.detectSegments(istream1);
                
                if (result1.getSegments().size() == 0) {
                    System.out.println("Window " + window + ": -----");
                }
                else {
                    System.out.println("Window " + window + ": FOUND");
                    res.get(elf).add("" + window);
                }
            }
            System.out.println("-------------------");
            var sb = new StringBuilder("elfs.put(MicroBlazePolyBenchMiniFloat.");
            sb.append(elf.toString());
            sb.append(", new Integer[] { ");
            sb.append(String.join(", ", res.get(elf)));
            sb.append(" });");
            f.write(sb.toString());
        }
        
        for (var elf : MicroBlazePolyBenchMiniFloat.values()) {
            var sb = new StringBuilder("elfs.put(MicroBlazePolyBenchMiniFloat.");
            sb.append(elf.toString());
            sb.append(", new Integer[] { ");
            sb.append(String.join(", ", res.get(elf)));
            sb.append(" });");
            System.out.println(sb.toString());
        }
        
        f.close();
    }
}
