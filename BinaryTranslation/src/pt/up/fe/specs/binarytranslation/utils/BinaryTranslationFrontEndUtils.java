package pt.up.fe.specs.binarytranslation.utils;

import org.specs.BinaryTranslation.ELFProvider;

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

        // get segment bundle
        var bundle = SegmentDetectTestUtils.detect(elf, streamClass, detectorClass);

        // transform into graph bundle
        var graphs = GraphBundle.newInstance(bundle);

        return graphs;
    }
}
