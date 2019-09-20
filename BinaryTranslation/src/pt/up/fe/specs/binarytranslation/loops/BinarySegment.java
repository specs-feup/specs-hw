package pt.up.fe.specs.binarytranslation.loops;

public interface BinarySegment {

    enum segmentType {
        BasicBlock,
        MegaBlock
    };

    int getSegmentLength();

    segmentType getSegmentType();
}
