package pt.up.fe.specs.binarytranslation.profiling;

public class InstructionHistogram extends AHistogramProfile {

    public InstructionHistogram() {
        super(data -> data.getName());
    }
}
