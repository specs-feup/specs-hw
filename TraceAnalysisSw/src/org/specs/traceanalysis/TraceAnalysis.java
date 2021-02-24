package org.specs.traceanalysis;

import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;

public class TraceAnalysis {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    private DetailedRegisterInstructionProducer provider;
    
    public TraceAnalysis(DetailedRegisterInstructionProducer provider) {
        this.setProvider(provider);
    }

    public DetailedRegisterInstructionProducer getProvider() {
        return provider;
    }

    public void setProvider(DetailedRegisterInstructionProducer provider) {
        this.provider = provider;
    }

    public boolean analyze() {
        MemoryProfiler mem = new MemoryProfiler(provider);
        mem.profile();
        return true;
    }
}
