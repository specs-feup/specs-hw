package pt.up.fe.specs.binarytranslation.test.detection;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;

public class SegmentDetectTestUtils {

    public static SegmentBundle detect(String filename, Class<?> streamClass, Class<?> detectorClass) {

        File fd = SpecsIo.resourceCopy(filename);
        fd.deleteOnExit();

        Constructor<?> consStream, consDetector;
        try {
            consStream = streamClass.getConstructor(File.class);
            consDetector = detectorClass.getConstructor();

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        SegmentBundle bundle = null;
        try (InstructionStream el = (InstructionStream) consStream.newInstance(fd)) {

            var bbd = (SegmentDetector) consDetector.newInstance();
            bundle = bbd.detectSegments(el);

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

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
