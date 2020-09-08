package pt.up.fe.specs.binarytranslation.stream.profiler;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class InstructionHistogram extends AHistogramProfile {

    public InstructionHistogram(InstructionStream istream) {
        super(istream, data -> data.getName());
    }
}
