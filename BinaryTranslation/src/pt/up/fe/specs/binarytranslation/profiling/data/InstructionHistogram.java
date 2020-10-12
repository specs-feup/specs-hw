package pt.up.fe.specs.binarytranslation.profiling.data;

public class InstructionHistogram extends AHistogramProfile {

    public InstructionHistogram() {
        super(data -> data.getName());
    }
}
