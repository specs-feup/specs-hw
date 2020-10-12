package pt.up.fe.specs.binarytranslation.profiling.data;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class InstructionHistogram extends AHistogramProfile {

    public InstructionHistogram(InstructionStream istream) {
        super(istream, data -> data.getName());
    }
}
