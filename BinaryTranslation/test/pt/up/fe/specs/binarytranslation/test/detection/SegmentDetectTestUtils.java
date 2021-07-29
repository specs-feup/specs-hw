package pt.up.fe.specs.binarytranslation.test.detection;

import java.io.File;
import java.util.function.Predicate;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.utils.ClassBuilders;

public class SegmentDetectTestUtils {

    /*
     * overload 1: elf as ELFprovider, no config given
     */
    public static SegmentBundle detect(ELFProvider elf, Class<?> streamClass, Class<?> detectorClass) {
        return SegmentDetectTestUtils.detect(
                BinaryTranslationUtils.getFile(elf), streamClass, detectorClass,
                DetectorConfigurationBuilder.defaultConfig());
    }

    /*
     * overload 2: elf as ELFprovider, config given
     */
    public static SegmentBundle detect(ELFProvider elf, Class<?> streamClass,
            Class<?> detectorClass, DetectorConfiguration config) {
        return SegmentDetectTestUtils.detect(BinaryTranslationUtils.getFile(elf),
                streamClass, detectorClass, config);
    }

    /*
     * overload 3: elf as file, no config given
     */
    public static SegmentBundle detect(File elffile, Class<?> streamClass, Class<?> detectorClass) {
        return SegmentDetectTestUtils.detect(elffile, streamClass, detectorClass,
                DetectorConfigurationBuilder.defaultConfig());
    }

    /*
     * "real method": elf as ELFprovider, config given
     */
    public static SegmentBundle detect(File elffile, Class<?> streamClass, Class<?> detectorClass,
            DetectorConfiguration config) {

        try {
            var stream = ClassBuilders.buildStream(streamClass, elffile);
            stream.silent(false);
            var detector = ClassBuilders.buildDetector(detectorClass, config);
            var bundle = detector.detectSegments(stream);
            return bundle;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void printBundle(SegmentBundle bundle) {

        for (var bs : bundle.getSegments()) {
            bs.printSegment();
            System.out.print("\n");
        }
    }

    public static void toJson(SegmentBundle bundle) {
        bundle.toJSON();
    }

    public static void printBundle(SegmentBundle bundle, Predicate<BinarySegment> predicate) {

        for (BinarySegment bs : bundle.getSegments(predicate)) {
            bs.printSegment();
            System.out.print("\n");
        }
    }
}
