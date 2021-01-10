package pt.up.fe.specs.binarytranslation.test.detection;

import java.util.function.Predicate;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.utils.ClassBuilders;

public class SegmentDetectTestUtils {

    public static SegmentBundle detect(ELFProvider elf, Class<?> streamClass, Class<?> detectorClass) {
        return SegmentDetectTestUtils.detect(elf, streamClass, detectorClass,
                DetectorConfigurationBuilder.defaultConfig());
    }

    public static SegmentBundle detect(ELFProvider elf, Class<?> streamClass, Class<?> detectorClass,
            DetectorConfiguration config) {

        var stream = ClassBuilders.buildStream(streamClass, elf);
        var detector = ClassBuilders.buildDetector(detectorClass, config);
        var bundle = detector.detectSegments(stream);
        return bundle;
    }

    public static void printBundle(SegmentBundle bundle) {

        for (BinarySegment bs : bundle.getSegments()) {
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
