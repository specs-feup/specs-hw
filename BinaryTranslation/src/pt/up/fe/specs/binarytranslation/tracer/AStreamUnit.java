package pt.up.fe.specs.binarytranslation.tracer;

import java.util.List;

public abstract class AStreamUnit implements StreamUnit {

    private final Long targetAddr;
    private final StreamUnitType type;

    /*
     * Copy Constructor 
     */
    protected AStreamUnit(AStreamUnit other) {
        this.targetAddr = other.getTargetAddr();
        this.type = other.getType();
    }

    protected AStreamUnit(StreamUnitType type, Long targetAddr) {
        this.type = type;
        this.targetAddr = targetAddr;
    }

    protected static Long getBranchTarget(List<StreamInstruction> tilist) {
        for (var inst : tilist)
            if (inst.getActual().isJump())
                return (Long) inst.getActual().getBranchTarget();

        return 0L;
    }

    @Override
    public StreamUnitType getType() {
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
    @Override
    public boolean follows(StreamUnit other) {
        var otherEndAddr = other.getEnd().getAddress();
        var thisStartAddr = this.getStart().getAddress();
        return thisStartAddr.longValue() == (otherEndAddr.longValue() + 4);
    }

    /*
     * True if addresses of two units follow
     * i.e. if "other" comes after "this"
     */
    @Override
    public boolean precedes(StreamUnit other) {
        var otherStartAddr = other.getStart().getAddress();
        var thisEndAddr = this.getEnd().getAddress();
        return otherStartAddr.longValue() == (thisEndAddr.longValue() + 4);
    }

    /*
     * True if "this" jumps to "other"
     */
    @Override
    public boolean jumpsTo(StreamUnit other) {
        var otherStartAddr = other.getStart().getAddress();
        return otherStartAddr.longValue() == (this.targetAddr.longValue());
    }

    /*
     * True if "other" jumps to "this"
     */
    @Override
    public boolean targetOf(StreamUnit other) {
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
