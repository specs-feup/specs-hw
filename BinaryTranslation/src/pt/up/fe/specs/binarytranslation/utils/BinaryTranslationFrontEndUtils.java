package pt.up.fe.specs.binarytranslation.utils;

import java.io.File;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.test.detection.SegmentDetectTestUtils;

/**
 * Helper class for the front-end of the web based demonstrator. Receives elf filename, a type of stream class, and type
 * of detector class. Returns a @{GraphBundle}.
 * 
 * @author nuno
 *
 */
public class BinaryTranslationFrontEndUtils {

    public static GraphBundle doBackend(ELFProvider elf, Class<?> streamClass, Class<?> detectorClass) {

        File fd = null;
        if (elf instanceof ZippedELFProvider) {
            var zpd = (ZippedELFProvider) elf;
            fd = zpd.asTxtDump().write(); // write dump to disk from resource (in jar)
        } else {
            fd = elf.getFile();
        }

        // get segment bundle
        var bundle = SegmentDetectTestUtils.detect(fd, streamClass, detectorClass);

        // transform into graph bundle
        var graphs = GraphBundle.newInstance(bundle);

        return graphs;
    }
}
