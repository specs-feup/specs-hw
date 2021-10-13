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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.provider.MicroBlazeTraceDumpProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FixedSizeMegablockDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;

public class MicroBlazeTestWindows {

    @Test
    public void testFindSuiteBasicBlockSizes() throws IOException {
        int minWindow = 4;
        int maxWindow = 60;
        boolean megaBlock = true;
        var arr = new ArrayList<String>();
        // var elfs = MicroBlazeLivermoreN100.values();
        var elfs = List.of(MicroBlazeLivermoreN100.matmul);

        for (var elf : elfs) {
            var windows = findBlockSizes(elf, minWindow, maxWindow, megaBlock);
            var code = generateCode(windows, elf.getClass().getSimpleName(), elf.toString());
            arr.add(code);
        }
        System.out.println("------------------------------");
        for (var s : arr)
            System.out.println(s);
    }

    @Test
    public void testVerifyFoundWindows() throws IOException {
        var elfs = MicroBlazeBasicBlockInfo.getPolybenchMiniFloatKernels();
        var cntExpected = 0;
        var cntActual = 0;
        boolean megaBlock = false;

        for (var elf : elfs.keySet()) {
            var windows = elfs.get(elf);
            cntExpected += windows.length;

            for (var window : windows) {
                var found = findBlockSizes(elf, window, window, megaBlock);
                cntActual += (found.size() != 0) ? 1 : 0;
            }
        }
        System.out.println("Expected: " + cntExpected);
        System.out.println("Actual:   " + cntActual);
    }

    public List<String> findBlockSizes(ZippedELFProvider elf, int minWindow, int maxWindow, boolean megaBlock)
            throws IOException {

        var arr = new ArrayList<String>();
        System.out.println("ELF: " + elf.getELFName());

        for (int window = minWindow; window <= maxWindow; window++) {
            var istream1 = new MicroBlazeTraceStream(new MicroBlazeTraceDumpProvider((MicroBlazeELFProvider) elf));
            istream1.silent(true);

            var startAddr = istream1.getApp().getKernelStart();
            var stopAddr = istream1.getApp().getKernelStop();
            istream1.runUntil(startAddr);

            SegmentBundle result1 = null;
            if (!megaBlock) {
                var detector1 = new TraceBasicBlockDetector(
                        new DetectorConfigurationBuilder()
                                .withMaxWindow(window)
                                .withStartAddr(startAddr)
                                .withPrematureStopAddr(stopAddr)
                                .build());
                result1 = detector1.detectSegments(istream1);
            } else {
                var detector1 = new FixedSizeMegablockDetector(
                        new DetectorConfigurationBuilder()
                                .withMaxWindow(window)
                                .withStartAddr(startAddr)
                                .withPrematureStopAddr(stopAddr)
                                .build());
                result1 = detector1.detectSegments(istream1);
            }

            if (result1.getSegments().size() == 0) {
                System.out.println("Window " + window + ": -----");
            } else {
                System.out.println("Window " + window + ": FOUND");
                arr.add("" + window);
                result1.printBundle();
            }
            istream1.close();
        }
        System.out.println("-------------------");
        return arr;

    }

    public String generateCode(List<String> windows, String className, String elf) {
        var sb = new StringBuilder("elfs.put(");
        sb.append(className);
        sb.append(".");
        sb.append(elf);
        sb.append(", new Integer[] { ");
        sb.append(String.join(", ", windows));
        sb.append(" });");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
