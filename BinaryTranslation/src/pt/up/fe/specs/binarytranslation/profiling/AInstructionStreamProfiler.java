package pt.up.fe.specs.binarytranslation.profiling;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public abstract class AInstructionStreamProfiler implements InstructionStreamProfiler {

    // TODO: how to repalce istream here so that InstructionStreamProfiler is compatble with subscribe -> run paradigm?

    protected InstructionStream istream;

    public AInstructionStreamProfiler(InstructionStream istream) {
        this.istream = istream;
    }
}
