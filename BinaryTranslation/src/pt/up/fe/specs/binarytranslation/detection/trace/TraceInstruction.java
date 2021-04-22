package pt.up.fe.specs.binarytranslation.detection.trace;

import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class TraceInstruction extends ATraceUnit {

    public TraceInstruction(Instruction inst) {
        super(Arrays.asList(inst));
    }

    @Override
    public int getHash() {
        return this.getStart().hashCode();
    }
}
