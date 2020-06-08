package pt.up.fe.specs.binarytranslation.hardware;

import java.util.List;

import pt.up.fe.specs.binarytranslation.graphs.*;

public interface HardwareGenerator {

    /*
     * Create a type of hardware instance from one binary segment
     */
    default public HardwareInstance generateHardware(BinarySegmentGraph graph) {
        return null;
    }

    /*
     * Create a type of hardware instance from binary segments
     */
    default public HardwareInstance generateHardware(List<BinarySegmentGraph> graphs) {
        return null;
    }

    /*
     * Create a type of hardware instance from a bundle of graphs 
     */
    default public HardwareInstance generateHarware(GraphBundle gbundle) {
        return null;
    }
}
