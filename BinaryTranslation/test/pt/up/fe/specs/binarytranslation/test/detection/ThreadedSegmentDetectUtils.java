package pt.up.fe.specs.binarytranslation.test.detection;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.utils.ClassBuilders;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.threadstream.ProducerEngine;

public class ThreadedSegmentDetectUtils {

    public static List<SegmentBundle> getSegments(ELFProvider elf, int maxWindowSize,
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
                        stream = (InstructionStream) cons.newInstance(BinaryTranslationUtils.getFile(elf), cc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return stream;
                });

        // for all window sizes
        for (int i = 2; i <= maxWindowSize; i++) {

            // detector
            var detector = ClassBuilders.buildDetector(detectorClass,
                    new DetectorConfigurationBuilder()
                            .withMaxWindow(i)
                            .withStartAddr(elf.getKernelStart())
                            .withStartAddr(elf.getKernelStop())
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
    }
}
