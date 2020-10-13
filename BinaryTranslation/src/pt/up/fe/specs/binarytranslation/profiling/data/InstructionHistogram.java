package pt.up.fe.specs.binarytranslation.profiling.data;

import pt.up.fe.specs.binarytranslation.profiling.AHistogramProfile;

public class InstructionHistogram extends AHistogramProfile {

    public InstructionHistogram() {
        super(data -> data.getName());
    }
}
