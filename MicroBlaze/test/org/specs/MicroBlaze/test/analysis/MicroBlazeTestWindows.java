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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniFloat;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniInt;
import org.specs.MicroBlaze.provider.MicroBlazeTraceDumpProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.util.SpecsLogs;

public class MicroBlazeTestWindows {
    @Test
    public void testDetectBasicBlock() {
        var elfs = MicroBlazeBasicBlockInfo.getPolybenchMiniFloatKernels();

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
    public void testFindSuiteBasicBlockSizes() throws IOException {
        int minWindow = 4;
        int maxWindow = 50;
        var arr = new ArrayList<String>();
        //var elfs = MicroBlazePolyBenchMiniFloat.values();
        var elfs = List.of(MicroBlazePolyBenchMiniFloat.trmm);
        
        for (var elf : elfs) {
            var s = testFindBasicBlockSizes(elf, elf.getClass().getSimpleName(), minWindow, maxWindow);
            arr.add(s);
        }
        System.out.println("------------------------------");
        for (var s : arr)
            System.out.println(s);
    }
    
    public String testFindBasicBlockSizes(MicroBlazeELFProvider elf, String className, int minWindow, int maxWindow) throws IOException {

        var arr = new ArrayList<String>();
        System.out.println("ELF: " + elf.getELFName());

        for (int window = minWindow; window <= maxWindow; window++) {
            var istream1 = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
            istream1.silent(true);

            var detector1 = new TraceBasicBlockDetector(
                    new DetectorConfigurationBuilder().withMaxWindow(window).build());
            var result1 = detector1.detectSegments(istream1);

            if (result1.getSegments().size() == 0) {
                System.out.println("Window " + window + ": -----");
            } else {
                System.out.println("Window " + window + ": FOUND");
                arr.add("" + window);
            }
            istream1.close();
        }
        System.out.println("-------------------");
        
        var sb = new StringBuilder("elfs.put(");
        sb.append(className);
        sb.append(".");
        sb.append(elf.toString());
        sb.append(", new Integer[] { ");
        sb.append(String.join(", ", arr));
        sb.append(" });");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
