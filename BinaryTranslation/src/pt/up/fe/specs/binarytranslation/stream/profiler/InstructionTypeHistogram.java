package pt.up.fe.specs.binarytranslation.stream.profiler;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class InstructionTypeHistogram extends AHistogramProfile {

    public InstructionTypeHistogram(InstructionStream istream) {
        super(istream, data -> data.getData().getGenericTypes().get(0).toString());
    }
}
