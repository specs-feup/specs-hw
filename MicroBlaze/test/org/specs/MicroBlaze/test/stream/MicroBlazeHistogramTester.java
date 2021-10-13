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
 
package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;

public class MicroBlazeHistogramTester {

    public void testMicroBlazeHistogram(MicroBlazeELFProvider elf) {
        var istream = new MicroBlazeTraceStream(elf);
        var profiler = new InstructionTypeHistogram();
        var result = profiler.profile(istream);
        result.toJSON();
    }

    @Test
    public void singleHistogram() {
        this.testMicroBlazeHistogram(MicroBlazeLivermoreN10.cholesky);
    }

    @Test
    public void MicroBlazeHistogramBatch() {
        for (var elf : MicroBlazeLivermoreN10.values()) {
            this.testMicroBlazeHistogram(elf);
        }
    }
}
