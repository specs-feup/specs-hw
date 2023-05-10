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
 
package org.specs.Riscv.test.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;
import org.specs.Riscv.provider.RiscvPolyBenchMiniFloat;

import pt.up.fe.specs.binarytranslation.asm.ELFDumper;
import pt.up.fe.specs.binarytranslation.elf.ELFProvider;

public class RIscvELFDumper extends ELFDumper {

    @Test
    public void sequentialDump() {
        var test = new ArrayList<ELFProvider>();
        test.addAll((Arrays.asList(RiscvLivermoreN100im.values())));
        // test.addAll((Arrays.asList(RiscvLivermoreN100imaf.values())));
        // test.addAll((Arrays.asList(RiscvPolyBenchMiniInt.values())));
        // test.addAll((Arrays.asList(RiscvPolyBenchMiniFloat.values())));
        sequentialDump(test);
    }

    @Test
    public void parallelDump() {
        var test = new ArrayList<ELFProvider>();
        // test.addAll((Arrays.asList(RiscvLivermoreN100im.values())));
        // test.addAll((Arrays.asList(RiscvLivermoreN100imaf.values())));
        // test.addAll((Arrays.asList(RiscvPolyBenchMiniInt.values())));
        test.addAll((Arrays.asList(RiscvPolyBenchMiniFloat.values())));
        parallelDump(test);
    }
}
