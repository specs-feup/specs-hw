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
 
package org.specs.Riscv.test.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;
import org.specs.Riscv.stream.RiscvTraceProducer;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;

public class RiscvProfileTester {

    /*
     * map providers to streams
     */
    private static final Map<Class<?>, Class<?>> AUXMAP = new HashMap<>();
    static {
        AUXMAP.put(RiscvTraceProducer.class, RiscvTraceStream.class);
    }

    @Test
    public void RiscvProfile() {

        Class<?> producers[] = { /*RiscvStaticProvider.class ,*/ RiscvTraceProducer.class };

        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(InstructionTypeHistogram.class);
        profilerList.add(InstructionHistogram.class);

        for (var elf : RiscvLivermoreN100im.values()) {
            // for (var file : Arrays.asList(RiscvLivermoreELFN100iamf.innerprod100)) {
            for (var producer : producers) {

                var app = elf.toApplication();
                var result = InstructionStreamProfilingUtils.profile(
                        app, producer, AUXMAP.get(producer),
                        profilerList, app.getKernelStart(), app.getKernelStop());

                for (var r : result) {
                    r.toJSON();
                }
            }
        }
    }

}
