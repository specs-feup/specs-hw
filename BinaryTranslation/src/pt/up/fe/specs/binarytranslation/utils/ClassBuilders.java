package pt.up.fe.specs.binarytranslation.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class ClassBuilders {

    public static SegmentDetector buildDetector(Class<?> detectorClass,
            DetectorConfiguration config)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

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
         
        SegmentDetector detector = null;
        try {
            detector = (SegmentDetector) cons.newInstance(config);
        
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

        // return detector;
        return (SegmentDetector) cons.newInstance(config);
    }

    /*
     * If i want to pass a specific file
     */
    public static InstructionStream buildStream(
            Class<?> streamClass, File elfile)
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

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
        
        InstructionStream stream;
        try {
            stream = (InstructionStream) cons.newInstance(elfile);
        
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
         */

        return (InstructionStream) cons.newInstance(elfile);
    }

    /*
     * Defaults to elf file in ELFProvider
     */
    public static InstructionStream buildStream(Class<?> streamClass, ELFProvider elf)
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        return ClassBuilders.buildStream(streamClass, BinaryTranslationUtils.getFile(elf));
    }

    public static InstructionProducer buildProducer(Class<?> producerClass, ELFProvider elf)
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

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
        
        InstructionProducer producer;
        try {
            producer = (InstructionProducer) cons.newInstance(BinaryTranslationUtils.getFile(elf));
        
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
         */

        return (InstructionProducer) cons.newInstance(BinaryTranslationUtils.getFile(elf));
    }
}
