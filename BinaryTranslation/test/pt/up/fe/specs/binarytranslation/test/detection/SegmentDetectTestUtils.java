package pt.up.fe.specs.binarytranslation.test.detection;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.SegmentDetector;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;

public class SegmentDetectTestUtils {

    public static void detect(String filename, Class<?> streamClass, Class<?> detectorClass) {

        File fd = SpecsIo.resourceCopy(filename);
        fd.deleteOnExit();

        Constructor<?> consStream, consDetector;
        try {
            consStream = streamClass.getConstructor(File.class);
            consDetector = detectorClass.getConstructor(InstructionStream.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        try (InstructionStream el = (InstructionStream) consStream.newInstance(fd)) {

            var bbd = (SegmentDetector) consDetector.newInstance(el);
            var bundle = bbd.detectSegments();
            List<BinarySegment> bblist = bundle.getSegments();

            for (BinarySegment bs : bblist) {
                bs.printSegment();
                System.out.print("\n");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        return;
    }
}
