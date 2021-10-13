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

import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.instruction.MicroBlazeInstructionProperties;
import org.specs.MicroBlaze.parsing.MicroBlazeAsmFieldType;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.singleinstructionmodule.SingleInstructionModuleGenerator;

/**
 * Test the generation of SingleInstructionUnit modules for the entire MicroBlaze ISA.
 * 
 * @author nuno
 *
 */
public class MicroBlazeInstructionTranslationTester {

    @Test
    public void testTypeB() {

        var singleUnitBuilder = new SingleInstructionModuleGenerator();

        // for (var props : MicroBlazeInstructionProperties.values()) {
        for (var props : Arrays.asList(MicroBlazeInstructionProperties.addic)) {
            if (props.getCodeType() == MicroBlazeAsmFieldType.TYPE_B) {
                var inst = MicroBlazeInstruction.newInstance("0", Integer.toHexString(props.getOpCode()));
                var unit = singleUnitBuilder.generateHarware(inst);
                unit.emit(System.out);
            }
        }
    }
}
