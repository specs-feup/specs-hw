package pt.up.fe.specs.binarytranslation.profiling;

public class InstructionTypeHistogram extends AHistogramProfile {

    public InstructionTypeHistogram() {
        super(data -> data.getData().getGenericTypes().get(0).toString());
    }
}
