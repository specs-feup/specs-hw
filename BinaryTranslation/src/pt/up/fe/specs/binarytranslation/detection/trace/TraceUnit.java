package pt.up.fe.specs.binarytranslation.detection.trace;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceUnit {
    Instruction start, end;

    public TraceUnit(Instruction start, Instruction end) {
        this.start = start;
        this.end = end;
    }

    public Instruction getStart() {
        return start;
    }

    public Instruction getEnd() {
        return end;
    }
}
