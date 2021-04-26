package pt.up.fe.specs.binarytranslation.tracer;

public abstract class ATraceUnit implements TraceUnit {

    protected Long targetAddr = 0L;
    private final TraceUnitType type;

    public ATraceUnit(TraceUnitType type) {
        this.type = type;
    }

    @Override
    public TraceUnitType getType() {
        return type;
    }

    @Override
    public Long getTargetAddr() {
        return targetAddr;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    /*
     * True if addresses of two units follow
     * i.e. if "this" comes after "other" 
     */
    public boolean follows(TraceUnit other) {
        var otherEndAddr = other.getEnd().getAddress();
        var thisStartAddr = this.getStart().getAddress();
        return thisStartAddr.longValue() == (otherEndAddr.longValue() + 4);
    }

    /*
     * True if addresses of two units follow
     * i.e. if "other" comes after "this"
     */
    public boolean precedes(TraceUnit other) {
        var otherStartAddr = other.getStart().getAddress();
        var thisEndAddr = this.getEnd().getAddress();
        return otherStartAddr.longValue() == (thisEndAddr.longValue() + 4);
    }

    /*
     * True if "this" jumps to "other"
     */
    public boolean jumpsTo(TraceUnit other) {
        var otherStartAddr = other.getStart().getAddress();
        return otherStartAddr.longValue() == (this.targetAddr.longValue());
    }

    /*
     * True if "other" jumps to "this"
     */
    public boolean targetOf(TraceUnit other) {
        var otherTargetAddr = other.getTargetAddr();
        var thisStartAddr = this.getStart().getAddress();
        return thisStartAddr.longValue() == (otherTargetAddr.longValue());
    }

    // TODO: if the detector wishes to determine equality based on
    // relative operands (i.e. symbolification), then it can
    // query the TraceUnit for a full list, and do whatever
    // transformations it requires
    //
    // for trace detectors, its just a matter of
    // directly calling the TraceUnit hash, which is
    // just addr based
}
