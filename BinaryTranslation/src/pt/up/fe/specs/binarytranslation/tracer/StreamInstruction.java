package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class StreamInstruction extends AStreamUnit {

    private final Instruction inst;

    public StreamInstruction(Instruction inst) {
        super(StreamUnitType.StreamInstruction,
                StreamInstruction.getBranchTarget(inst));
        this.inst = inst;
    }

    protected static Long getBranchTarget(Instruction inst) {
        if (inst.isJump())
            return (Long) inst.getBranchTarget();
        else
            return inst.getAddress() + 4;
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

    @Override
    public boolean containsAddr(Long addr) {
        return this.inst.getAddress().longValue() == addr.longValue();
    }

    /*
     * True if any instruction in this TraceUnit
     * includes the target of the "other"
     */
    @Override
    public boolean includesTarget(StreamUnit other) {
        var otherTargetAddr = other.getTargetAddr();
        return this.inst.getAddress().longValue() == otherTargetAddr.longValue();
    }
}
