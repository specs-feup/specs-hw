package org.specs.MicroBlaze.test.detection;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeStaticProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.utils.ClassBuilders;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.threadstream.ProducerEngine;

public class MicroBlazeBatchFrequentSequenceTest {

    private List<SegmentBundle> getSegments(ELFProvider elf, int maxWindowSize,
            Class<?> producerClass, Class<?> streamClass, Class<?> detectorClass) {

        var iproducer = ClassBuilders.buildProducer(producerClass, elf);

        /*
         * Stream constructor
         */
        Constructor<?> cons;
        try {
            cons = streamClass.getConstructor(File.class, ChannelConsumer.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // host for threads
        var streamengine = new ProducerEngine<>(iproducer, op -> op.nextInstruction(),
                cc -> {
                    InstructionStream stream = null;
                    try {
                        stream = (InstructionStream) cons.newInstance(elf.getResource(), cc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return stream;
                });

        // for all window sizes
        for (int i = 2; i < maxWindowSize; i++) {

            // detector
            var detector = ClassBuilders.buildDetector(detectorClass,
                    new DetectorConfigurationBuilder().withMaxWindow(i).build());

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
    }

    @Test
    public void MicroBlazeFrequentSequenceDetect() {

        // Class<?> producers[] = { MicroBlazeStaticProvider.class /*, MicroBlazeTraceProvider.class*/ };

        /*        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(FrequentStaticSequenceDetector.class);
        profilerList.add(InstructionHistogram.class);*/

        // for (var file : MicroBlazeLivermoreELFN100.values()) {
        for (var elf : Arrays.asList(MicroBlazeLivermoreELFN10.innerprod)) {

            var segs = getSegments(elf, 2,
                    MicroBlazeStaticProvider.class,
                    MicroBlazeElfStream.class,
                    FrequentStaticSequenceDetector.class);

            for (var seg : segs) {
                var gbundle = GraphBundle.newInstance(seg);
                gbundle.toJSON();
            }

            /*// for (var file : Arrays.asList(MicroBlazeLivermoreELFN100.linrec100)) {
            for (var producer : producers) {
            
                var result = InstructionStreamProfilingUtils.profile(
                        file.getResource(), producer, AUXMAP.get(producer),
                        profilerList, file.getKernelStart(), file.getKernelStop());
            
                for (var r : result) {
                    r.toJSON();
                }
            }*/
        }
    }
}
