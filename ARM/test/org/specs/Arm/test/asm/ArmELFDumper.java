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
 
package org.specs.Arm.test.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN100;
import org.specs.Arm.provider.ArmPolyBenchMiniFloat;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.asm.ELFDumper;

public class ArmELFDumper extends ELFDumper {

    @Test
    public void sequentialDump() {
        var test = new ArrayList<ELFProvider>();
        // test.addAll((Arrays.asList(ArmLivermoreN100.values())));
        // test.addAll((Arrays.asList(ArmPolyBenchMiniInt.values())));
        test.addAll((Arrays.asList(ArmPolyBenchMiniFloat.values())));
        sequentialDump(test);
    }

    @Test
    public void parallelDump() {
        var test = new ArrayList<ELFProvider>();
        test.addAll((Arrays.asList(ArmLivermoreN100.values())));
        parallelDump(test);
    }

}
