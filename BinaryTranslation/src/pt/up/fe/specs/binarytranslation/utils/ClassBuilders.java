package pt.up.fe.specs.binarytranslation.utils;

import java.io.File;
import java.lang.reflect.Constructor;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class ClassBuilders {

    public static SegmentDetector buildDetector(Class<?> detectorClass, DetectorConfiguration config) {

        /*
         * Detector constructor
         */
        Constructor<?> cons;
        try {
            cons = detectorClass.getConstructor(DetectorConfiguration.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*
         * Construct detector
         */
        SegmentDetector detector = null;
        try {
            detector = (SegmentDetector) cons.newInstance(config);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return detector;
    }

    public static InstructionStream buildStream(Class<?> streamClass, ELFProvider elf) {

        /*
         * Stream constructor
         */
        Constructor<?> cons;
        try {
            cons = streamClass.getConstructor(File.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*
         * Construct Stream
         */
        InstructionStream stream;
        try {
            stream = (InstructionStream) cons.newInstance(BinaryTranslationUtils.getFile(elf));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return stream;
    }

    public static InstructionProducer buildProducer(Class<?> producerClass, ELFProvider elf) {

        /*
         * Producer constructor
         */
        Constructor<?> cons;
        try {
            cons = producerClass.getConstructor(File.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*
         * Construct producer
         */
        InstructionProducer producer;
        try {
            producer = (InstructionProducer) cons.newInstance(BinaryTranslationUtils.getFile(elf));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return producer;
    }
}
