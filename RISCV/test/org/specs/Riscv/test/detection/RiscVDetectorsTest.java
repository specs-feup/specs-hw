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
 
package org.specs.Riscv.test.detection;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;

import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTester;

public class RiscVDetectorsTest extends SegmentDetectTester {

    /*
     * Static Frequent Sequence
     */
    @Test
    public void testFrequentStaticSequenceDetector() {
        testFrequentStaticSequenceDetector(RiscvLivermoreN100im.cholesky.toStaticStream());
    }

    /*
     * Trace Frequent Sequence
     */
    @Test
    public void testFrequentTraceSequenceDetector() {
        testFrequentTraceSequenceDetector(RiscvLivermoreN100im.cholesky.toTraceStream());
    }

    /*
     * Static Basic Block
     */
    @Test
    public void testStaticBasicBlockDetector() {
        testStaticBasicBlockDetector(RiscvLivermoreN100im.cholesky.toStaticStream());
    }

    /*
     * Trace Basic Block
     */
    @Test
    public void testTraceBasicBlockDetector() {
        testTraceBasicBlockDetector(RiscvLivermoreN100im.cholesky.toTraceStream());
    }

    /*
     * GenericTraceSegment (v3 detector WIP)
     */
    @Test
    public void testGenericTraceDetector() {
        // var istream = MicroBlazePolyBenchMiniInt.floydwarshall.toTraceStream();
        // var istream = MicroBlazePolyBenchMiniInt.floydwarshall.asTraceTxtDump().toTraceStream();
        // var istream = MicroBlazePolyBenchMiniInt.gemm.asTraceTxtDump().toTraceStream();
        var istream = RiscvLivermoreN100im.innerprod.toTraceStream();
        testGenericTraceDetector(istream);
    }
}
