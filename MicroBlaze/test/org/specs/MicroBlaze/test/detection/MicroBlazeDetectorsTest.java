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
 
package org.specs.MicroBlaze.test.detection;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;

import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTester;

/**
 * Segment detection test cases; txt files are used so that the backend tools can run on Jenkins without need for
 * installing GNU utils for the MicroBlaze architecture
 * 
 * @author nuno
 *
 */
public class MicroBlazeDetectorsTest extends SegmentDetectTester {

    /*
     * Static Frequent Sequence
     */
    @Test
    public void testFrequentStaticSequenceDetector() {
        testFrequentStaticSequenceDetector(MicroBlazeLivermoreN100.cholesky.toStaticStream());
    }

    /*
     * Trace Frequent Sequence
     */
    @Test
    public void testFrequentTraceSequenceDetector() {
        testFrequentTraceSequenceDetector(MicroBlazeLivermoreN100.cholesky.toTraceStream());
    }

    /*
     * Static Basic Block
     */
    @Test
    public void testStaticBasicBlockDetector() {
        testStaticBasicBlockDetector(MicroBlazeLivermoreN100.cholesky.toStaticStream());
    }

    /*
     * Trace Basic Block
     */
    @Test
    public void testTraceBasicBlockDetector() {
        testTraceBasicBlockDetector(MicroBlazeLivermoreN100.cholesky.toTraceStream());
    }

    /*
     * Megablock
     
    @Test
    public void testMegablock() {
        var app = MicroBlazeLivermoreN10.matmul.toApplication();
        var builder = new DetectorConfigurationBuilder();
        builder.withMaxBlocks(10)
                .withMaxWindow(1000)
                .withStartAddr(app.getKernelStart())
                .withStopAddr(app.getKernelStop());
    nordicsemi.com/
        var bundle = SegmentDetectTester.detect(app,
                MicroBlazeTraceStream.class,
                FixedSizeMegablockDetector.class,
                builder.build());
        SegmentDetectTester.printBundle(bundle);
    }*/

    /*
     * GenericTraceSegment (v3 detector WIP)
     */
    @Test
    public void testGenericTraceDetector() {
        // var istream = MicroBlazePolyBenchMiniInt.floydwarshall.toTraceStream();
        // var istream = MicroBlazePolyBenchMiniInt.floydwarshall.asTraceTxtDump().toTraceStream();
        // var istream = MicroBlazePolyBenchMiniInt.gemm.asTraceTxtDump().toTraceStream();
        var istream = MicroBlazeLivermoreN100.innerprod.asTraceTxtDump().toTraceStream();
        testGenericTraceDetector(istream);
    }
}
