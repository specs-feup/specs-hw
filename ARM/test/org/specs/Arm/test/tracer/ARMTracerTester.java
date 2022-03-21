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

package org.specs.Arm.test.tracer;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN10;
import org.specs.Arm.provider.ArmLivermoreN100;

import pt.up.fe.specs.binarytranslation.test.tracer.TraceGraphTestUtils;

public class ARMTracerTester {

    @Test
    public void testTraceGraphingAllELFs() {
        /*for (var file : ArmLivermoreELFN10.values()) {
            TraceGraphTestUtils.testTraceStreamGraph(file, ArmTraceStream.class);
        }*/
        TraceGraphTestUtils.testTraceStreamGraph(
                ArmLivermoreN100.hydro.toTraceStream());
    }

    /*
     * Get BasicBlockTraceUnit's from trace stream 
     */
    @Test
    public void testBasicBlockTraceUnit() {
        TraceGraphTestUtils.testBasicBlockTraceUnit(
                ArmLivermoreN10.innerprod.toTraceStream());
    }
}
