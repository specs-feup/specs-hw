package pt.up.fe.specs.binarytranslation.hardwaregeneration;

import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;

public interface HardwareGenerator {

    /*
     * Create a type of hardware instance from one binary segment
     */
    public HardwareInstance generateHardware(BinarySegment segment);

    /*
     * Create a type of hardware instance from binary segments
     */
    public HardwareInstance generateHardware(List<BinarySegment> segments);
}
