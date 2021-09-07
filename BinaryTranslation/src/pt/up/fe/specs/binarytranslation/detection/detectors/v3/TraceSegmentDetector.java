package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;

/**
 * 
 * @author nuno
 *
 */
public interface TraceSegmentDetector {

    ArrayList<StreamUnitPattern> detectSegments();
}
