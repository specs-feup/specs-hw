package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.List;

public interface BinarySegment {

    enum SegmentType {
        BASIC_BLOCK,
        MEGA_BLOCK
    };

    SegmentType getSegmentType();

    /*
     * Number of instructions in the segment
     */
    int getSegmentLength();

    /*
     * Total execution latency of segment 
     */
    int getTotalLatency();

    /*
     * Retrieve list of numbers representing
     * registers which are inputs into the segment 
     */
    List<Integer> getLiveIns();

    /*
     * Retrieve list of numbers representing
     * registers which are output of the segment 
     */
    List<Integer> getLiveOuts();
}
