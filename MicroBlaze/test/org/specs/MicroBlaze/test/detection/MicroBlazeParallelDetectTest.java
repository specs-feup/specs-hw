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
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProducer;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.threadstream.ProducerEngine;

/**
 * Comparison runs between multiple detectors in sequence, versus threaded run; txt files are used so that the backend
 * tools can run on Jenkins without need for installing GNU utils for the MicroBlaze architecture
 * 
 * @author nuno
 *
 */
public class MicroBlazeParallelDetectTest {

    private void testParallelDetectors(MicroBlazeELFProvider elf) {

        int minwindow = 4, maxwindow = 20;

        var app = elf.toApplication();

        // producer
        var iproducer = new MicroBlazeTraceProducer(app);
        // var iproducer = new MicroBlazeStaticProvider(app);

        // host for threads
        var streamengine = new ProducerEngine<>(iproducer, op -> op.nextInstruction(),
                // cc -> new MicroBlazeElfStream(fd, cc));
                cc -> new MicroBlazeTraceStream(app, cc));

        // for all window sizes
        for (int i = minwindow; i <= maxwindow; i++) {

            // var detector1 = new FrequentTraceSequenceDetector(
            // var detector1 = new FrequentStaticSequenceDetector(
            var detector1 = new TraceBasicBlockDetector(
                    new DetectorConfigurationBuilder()
                            .withMaxWindow(i)
                            .withStartAddr(app.getKernelStart())
                            .withStopAddr(app.getKernelStop())
                            .build());
            streamengine.subscribe(istream -> detector1.detectSegments((InstructionStream) istream));
        }

        // launch all threads (blocking)
        streamengine.launch();

        for (var consumer : streamengine.getConsumers()) {
            var result1 = (SegmentBundle) consumer.getConsumeResult();
            // SegmentDetectTestUtils.printBundle(result1);
            var gbundle = GraphBundle.newInstance(result1);
            if (gbundle.getSegments().size() != 0)
                gbundle.generateOutput();
        }

        // results1.toJSON();
        // results2.toJSON();
    }

    @Test
    public void testParallelDetectors() {
        for (var file : MicroBlazeLivermoreN100.values()) {
            // for (var file : Arrays.asList(MicroBlazeLivermoreELFN100.innerprod100)) {
            this.testParallelDetectors(file);
        }
    }
}
