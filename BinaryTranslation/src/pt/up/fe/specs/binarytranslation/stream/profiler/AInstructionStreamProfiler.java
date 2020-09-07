package pt.up.fe.specs.binarytranslation.stream.profiler;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class AInstructionStreamProfiler implements InstructionStreamProfiler {

    // private Predicate<Instruction> predicate;

    /*public AInstructionStreamProfiler(Predicate<Instruction> predicate) {
        this.predicate = predicate;
    }*/

    protected InstructionStream istream;

    public AInstructionStreamProfiler(InstructionStream istream) {
        this.istream = istream;
    }
}
