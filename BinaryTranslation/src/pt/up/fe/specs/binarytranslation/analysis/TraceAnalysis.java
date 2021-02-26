package pt.up.fe.specs.binarytranslation.analysis;

import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;

public class TraceAnalysis {

    private DetailedRegisterInstructionProducer provider;

    public TraceAnalysis(DetailedRegisterInstructionProducer provider) {
        this.provider = provider;
    }

    public boolean analyze() {
        MemoryProfiler mem = new MemoryProfiler(this.provider);
        mem.profile();
        return true;
    }
}
