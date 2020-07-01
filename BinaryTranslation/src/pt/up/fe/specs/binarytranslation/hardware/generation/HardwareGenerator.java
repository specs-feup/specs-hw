package pt.up.fe.specs.binarytranslation.hardware.generation;

import java.util.List;

import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graphs.GraphBundle;
import pt.up.fe.specs.binarytranslation.hardware.HardwareInstance;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

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

    /*
     * 
     */
    default public HardwareInstance generateHarware(Instruction inst) {
        return null;
    }
}
