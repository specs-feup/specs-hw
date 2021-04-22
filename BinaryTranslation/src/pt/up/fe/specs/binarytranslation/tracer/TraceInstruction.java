package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceInstruction extends ATraceUnit {

    private final Instruction inst;

    public TraceInstruction(Instruction inst) {
        super(TraceUnitType.TraceInstruction);
        this.inst = inst;
    }

    @Override
    public int getHash() {
        return this.inst.getAddress().hashCode();
    }

    public Instruction getActual() {
        return inst;
    }

    @Override
    public String toString() {
        return this.inst.toString();
    }
}
