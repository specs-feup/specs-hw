package pt.up.fe.specs.binarytranslation.test.detection;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.function.Predicate;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.SegmentBundle;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.SegmentDetector;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;

public class SegmentDetectTestUtils {

    public static SegmentBundle detect(String filename, Class<?> streamClass, Class<?> detectorClass) {

        File fd = SpecsIo.resourceCopy(filename);
        fd.deleteOnExit();

        Constructor<?> consStream, consDetector;
        try {
            consStream = streamClass.getConstructor(File.class);
            consDetector = detectorClass.getConstructor(InstructionStream.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        SegmentBundle bundle = null;
        try (InstructionStream el = (InstructionStream) consStream.newInstance(fd)) {

            var bbd = (SegmentDetector) consDetector.newInstance(el);
            bundle = bbd.detectSegments();

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

    public static void printBundle(SegmentBundle bundle, Predicate<BinarySegment> predicate) {

        for (BinarySegment bs : bundle.getSegments(predicate)) {
            bs.printSegment();
            System.out.print("\n");
        }
    }
}
