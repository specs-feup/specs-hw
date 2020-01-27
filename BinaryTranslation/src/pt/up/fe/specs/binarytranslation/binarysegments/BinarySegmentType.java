package pt.up.fe.specs.binarytranslation.binarysegments;

public enum BinarySegmentType {

    STATIC_FREQUENT_SEQUENCE,
    STATIC_BASIC_BLOCK,
    TRACE_FREQUENT_SEQUENCE,
    TRACE_BASIC_BLOCK,
    MEGA_BLOCK;

    public Boolean isStatic() {
        return (this == STATIC_FREQUENT_SEQUENCE) || (this == STATIC_BASIC_BLOCK);
    }

    public Boolean isTrace() {
        return (this == TRACE_FREQUENT_SEQUENCE) || (this == MEGA_BLOCK) || (this == TRACE_BASIC_BLOCK);
    }

    public Boolean isAcyclical() {
        return (this == STATIC_FREQUENT_SEQUENCE) || (this == TRACE_FREQUENT_SEQUENCE);
    }

    public Boolean isCyclical() {
        return (this == STATIC_BASIC_BLOCK) || (this == MEGA_BLOCK) || (this == TRACE_BASIC_BLOCK);
    }
}
