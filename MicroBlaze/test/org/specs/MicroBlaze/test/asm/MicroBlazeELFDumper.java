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

package org.specs.MicroBlaze.test.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniFloat;
import org.specs.MicroBlaze.provider.MicroBlazeRosetta;

import pt.up.fe.specs.binarytranslation.asm.ELFDumper;
import pt.up.fe.specs.binarytranslation.elf.ELFProvider;

public class MicroBlazeELFDumper extends ELFDumper {

    @Test
    public void sequentialDump() {
        var test = new ArrayList<ELFProvider>();
        // test.addAll((Arrays.asList(MicroBlazeLivermoreN100.values())));
        // test.addAll((Arrays.asList(MicroBlazePolyBenchMiniInt.values())));
        test.addAll((Arrays.asList(MicroBlazePolyBenchMiniFloat.values())));
        sequentialDump(test);
    }

    @Test
    public void parallelDump() {
        var test = new ArrayList<ELFProvider>();
        test.addAll((Arrays.asList(MicroBlazeRosetta.values())));
        // test.addAll((Arrays.asList(MicroBlazeLivermoreN100.values())));
        // test.addAll((Arrays.asList(MicroBlazePolyBenchMiniInt.values())));
        // test.addAll((Arrays.asList(MicroBlazePolyBenchMiniFloat.values())));
        parallelDump(test);
    }
}
