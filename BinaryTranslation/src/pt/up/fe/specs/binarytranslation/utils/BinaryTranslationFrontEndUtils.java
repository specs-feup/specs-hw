package pt.up.fe.specs.binarytranslation.utils;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;

/**
 * Helper class for the front-end of the web based demonstrator. Receives elf filename, a type of stream class, and type
 * of detector class. Returns a @{GraphBundle}.
 * 
 * @author nuno
 *
 */
public class BinaryTranslationFrontEndUtils {

    public static GraphBundle doBackend(ELFProvider elf, Class<?> streamClass, Class<?> detectorClass) {

        //
        var istream = elf.toStaticStream();
        var builder = new DetectorConfigurationBuilder();
        builder.withSkipToAddr(istream.getApp().getKernelStart());
        builder.withPrematureStopAddr(istream.getApp().getKernelStop());
        builder.withStartAddr(istream.getApp().getKernelStart());
        builder.withStopAddr(istream.getApp().getKernelStop());

        SegmentDetector detector = null;
        if (detectorClass == FrequentStaticSequenceDetector.class) {
            detector = new FrequentStaticSequenceDetector(builder.withMaxWindow(3).build());

        } else if (detectorClass == StaticBasicBlockDetector.class) {
            detector = new StaticBasicBlockDetector(builder.withMaxWindow(6).build());
        }

        // get segment bundle
        var bundle = detector.detectSegments(istream);

        // transform into graph bundle
        var graphs = GraphBundle.newInstance(bundle);

        return graphs;
    }
}
