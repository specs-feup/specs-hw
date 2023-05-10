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
 
package pt.up.fe.specs.binarytranslation.test.detection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.elf.ELFProvider;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.utils.BundleStatsUtils;
import pt.up.fe.specs.binarytranslation.utils.ClassBuilders;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.threadstream.ProducerEngine;

public class ThreadedSegmentDetectUtils {

    public static List<SegmentBundle> getSegments(ELFProvider elf, int minsize, int maxWindowSize,
            Class<?> producerClass, Class<?> streamClass, Class<?> detectorClass) {

        try {
            var app = elf.toApplication();
            var iproducer = ClassBuilders.buildProducer(producerClass, app);

            /*
             * Stream constructor
             */
            var cons = streamClass.getConstructor(File.class, ChannelConsumer.class);

            /*
             *  host for threads
             */
            var streamengine = new ProducerEngine<>(iproducer, op -> op.nextInstruction(),
                    cc -> {
                        InstructionStream stream = null;
                        try {
                            stream = (InstructionStream) cons.newInstance(BinaryTranslationUtils.getFile(elf), cc);
                            stream.silent(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return stream;
                    });

            // for all window sizes
            for (int i = minsize; i <= maxWindowSize; i++) {

                // detector
                var detector = ClassBuilders.buildDetector(detectorClass,
                        new DetectorConfigurationBuilder()
                                .withMaxWindow(i)
                                .withStartAddr(app.getKernelStart())
                                .withStopAddr(app.getKernelStop())
                                .withSkipToAddr(app.getKernelStart())
                                .withPrematureStopAddr(app.getKernelStop())
                                .build());

                streamengine.subscribe(istream -> detector.detectSegments((InstructionStream) istream));
            }

            // launch all threads (blocking)
            streamengine.launch();

            var listOfSegs = new ArrayList<SegmentBundle>();
            for (var consumer : streamengine.getConsumers()) {
                var results1 = (SegmentBundle) consumer.getConsumeResult();
                listOfSegs.add(results1);
            }

            return listOfSegs;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * 
     */
    public static void BatchDetect(ELFProvider elfs[], int minsize, int maxsize,
            Class<?> provider, Class<?> stream, Class<?> detector) {

        var pTime = System.nanoTime();

        for (var elf : elfs) {
            System.out.println("Processing ELF: " + elf.getResource());
            var segs = ThreadedSegmentDetectUtils.getSegments(
                    elf, minsize, maxsize,
                    provider, stream, detector);

            BundleStatsUtils.bundleStatsDump(elf, segs, true);
        }

        pTime = (long) ((System.nanoTime() - pTime) / 1E9);
        System.out.println("runtime: " + pTime);
    }
}
