package pt.up.fe.specs.binarytranslation.profiling;

public abstract class AInstructionStreamProfiler implements InstructionStreamProfiler {

    // TODO: how to repalce istream here so that InstructionStreamProfiler
    // is compatble with subscribe -> run paradigm?

    protected Number startAddr, stopAddr;

    public AInstructionStreamProfiler() {
        this.startAddr = -1;
        this.stopAddr = -1;
    }

    public void setStartAddr(Number startAddr) {
        this.startAddr = startAddr;
    }

    public void setStopAddr(Number stopAddr) {
        this.stopAddr = stopAddr;
    }
}
