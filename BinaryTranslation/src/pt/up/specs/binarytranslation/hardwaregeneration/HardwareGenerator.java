package pt.up.specs.binarytranslation.hardwaregeneration;

import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;

public interface HardwareGenerator {

    /*
     * Create a type of hardware instance from binary segments
     */
    HardwareInstance generateHardware(List<BinarySegment> segments);
}
