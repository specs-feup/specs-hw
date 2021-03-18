package pt.up.fe.specs.binarytranslation.analysis;

import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public abstract class ATraceAnalyzer {
    protected ATraceInstructionStream stream;

    public ATraceAnalyzer(ATraceInstructionStream stream) {
        this.stream = stream;
    }
    
    
}
