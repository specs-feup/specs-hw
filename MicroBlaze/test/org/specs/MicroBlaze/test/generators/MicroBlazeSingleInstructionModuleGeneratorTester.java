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
 
package org.specs.MicroBlaze.test.generators;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.singleinstructionmodule.SingleInstructionModuleGenerator;

public class MicroBlazeSingleInstructionModuleGeneratorTester {

    @Test
    public void testAddiUnit() {
        // 248: 20c065e8 addi r6, r0, 26088 // 65e8 <_SDA_BASE_>
        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");
        var singleUnitBuilder = new SingleInstructionModuleGenerator();
        var unit = singleUnitBuilder.generateHarware(addi);
        // unit.emit(System.out);

        // new dotty generation test
        // var dottygen = new DottyGenerator<HardwareNode>();
        // dottygen.generateDotty(unit.getTree().getRoot());
    }
}
