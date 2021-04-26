package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceInstruction extends ATraceUnit {

    private final Instruction inst;

    public TraceInstruction(Instruction inst) {
        super(TraceUnitType.TraceInstruction, inst.getAddress() + 4);
        this.inst = inst;
    }

    @Override
    public int hashCode() {
        return this.inst.getAddress().intValue(); // .hashCode();
    }

    public Instruction getActual() {
        return inst;
    }

    @Override
    public String toString() {
        return this.inst.toString();
    }

    @Override
    public Instruction getStart() {
        return this.inst;
    }

    @Override
    public Instruction getEnd() {
        return this.inst;
    }

    /*
     * True if any instruction in this TraceUnit
     * includes the target of the "other"
     */
    public boolean includesTarget(TraceUnit other) {
        var otherTargetAddr = other.getTargetAddr();
        return this.inst.getAddress().longValue() == otherTargetAddr.longValue();
    }
}
