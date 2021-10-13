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
 
package org.specs.Arm.test.stream;

import org.junit.Test;
import org.specs.Arm.provider.ArmELFProvider;
import org.specs.Arm.provider.ArmLivermoreN10;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;

public class ArmHistogramTester {

    public void testInstructionTypeHistogram(ArmELFProvider elfprovider) {
        var istream = new ArmTraceStream(elfprovider);
        var profiler = new InstructionTypeHistogram();
        var result = profiler.profile(istream);
        result.toJSON();
    }

    public void testInstructionHistogram(ArmELFProvider elfprovider) {
        var istream = new ArmTraceStream(elfprovider);
        var profiler = new InstructionHistogram();
        var result = profiler.profile(istream);
        result.toJSON();
    }

    @Test
    public void singleHistogram(ArmELFProvider elfprovider) {
        this.testInstructionTypeHistogram(elfprovider);
        this.testInstructionHistogram(elfprovider);
    }

    @Test
    public void ArmHistogramBatch() {
        for (var elf : ArmLivermoreN10.values()) {
            this.singleHistogram(elf);
        }
    }
}
