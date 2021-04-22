package pt.up.fe.specs.binarytranslation.detection.trace;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public interface TraceUnit {

    public int getHash();

    public Instruction getStart();

    public Instruction getEnd();
}
