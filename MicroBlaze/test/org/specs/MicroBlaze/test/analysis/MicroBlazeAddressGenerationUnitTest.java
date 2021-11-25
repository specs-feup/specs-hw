/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.specs.MicroBlaze.test.analysis;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.reporters.ReporterAddressGenerationUnit;
import pt.up.fe.specs.util.SpecsLogs;

public class MicroBlazeAddressGenerationUnitTest {
    
    @Test
    public void testAddressGenerationUnits() {
        var elfs = MicroBlazeBasicBlockInfo.getPolybenchMiniFloatKernels();
        var streams = MicroBlazeBasicBlockSchedulingTest.buildStreams(elfs);
        var analyzer = new ReporterAddressGenerationUnit(elfs, streams);
        
        try {
            analyzer.analyze();
        } catch (Exception e) {
            SpecsLogs.warn("Error message:\n", e);
        }
    }
}
