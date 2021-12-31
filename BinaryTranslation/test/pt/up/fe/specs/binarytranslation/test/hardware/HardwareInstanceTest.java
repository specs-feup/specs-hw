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

package pt.up.fe.specs.binarytranslation.test.hardware;

import java.util.Arrays;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.cla.CLAFunctionalUnit;
import pt.up.fe.specs.crispy.coarse.Adder;
import pt.up.fe.specs.crispy.lib.MuxNto1;
import pt.up.fe.specs.crispy.lib.ShiftRegister;

public class HardwareInstanceTest {

    @Test
    public void testCLAFunctionalUniter() {

        var unit = new CLAFunctionalUnit(
                new Adder(8),
                new ShiftRegister(4, 8),
                Arrays.asList(new MuxNto1(2, 8), new MuxNto1(2, 8)));

        unit.emit();
    }
}
