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
 
package org.specs.MicroBlaze.test.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProducer;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;
import pt.up.fe.specs.util.utilities.heapwindow.HeapWindow;

public class MicroBlazeProfileTester {

    /*
     * map providers to streams
     */
    private static final Map<Class<?>, Class<?>> AUXMAP = new HashMap<>();
    static {
        AUXMAP.put(MicroBlazeTraceProducer.class, MicroBlazeTraceStream.class);
    }

    @Test
    public void MicroBlazeProfile() {
        (new HeapWindow()).run();

        Class<?> producers[] = { /*MicroBlazeStaticProvider.class ,*/ MicroBlazeTraceProducer.class };

        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(InstructionTypeHistogram.class);
        profilerList.add(InstructionHistogram.class);

        for (var elf : MicroBlazeLivermoreN100.values()) {
            // for (var file : Arrays.asList(MicroBlazeLivermoreELFN100.linrec100)) {

            for (var producer : producers) {

                var app = elf.toApplication();
                var result = InstructionStreamProfilingUtils.profile(
                        app, producer, AUXMAP.get(producer),
                        profilerList, app.getKernelStart(), app.getKernelStop());

                for (var r : result) {
                    r.toJSON();
                }
            }
            break;
        }
    }

    /*
     * just to count total cycles (Cause i didnt before)
     */
    @Test
    public void MicroBlazeSimulate() {

        for (var elf : MicroBlazeLivermoreN100.values()) {

            try (var istream = new MicroBlazeTraceStream(elf)) {

                // sim
                istream.silent(true);
                while ((istream.nextInstruction()) != null)
                    ;

                System.out.println(elf.getFilename() + ": " + istream.getNumInstructions());
            }
        }
    }
}
