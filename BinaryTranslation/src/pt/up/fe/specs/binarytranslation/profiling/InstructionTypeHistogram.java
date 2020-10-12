package pt.up.fe.specs.binarytranslation.profiling;

import pt.up.fe.specs.binarytranslation.profiling.data.AHistogramProfile;

public class InstructionTypeHistogram extends AHistogramProfile {

    public InstructionTypeHistogram() {
        super(data -> data.getData().getGenericTypes().get(0).toString());
    }
}
