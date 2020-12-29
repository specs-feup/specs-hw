package pt.up.fe.specs.binarytranslation.profiling;

public abstract class AInstructionStreamProfiler implements InstructionStreamProfiler {

    // TODO: how to repalce istream here so that InstructionStreamProfiler
    // is compatble with subscribe -> run paradigm?

    protected Number startAddr, stopAddr, profileTime;

    public AInstructionStreamProfiler() {
        this.startAddr = -1;
        this.stopAddr = -1;
    }

    @Override
    public void setStartAddr(Number startAddr) {
        this.startAddr = startAddr;
    }

    @Override
    public void setStopAddr(Number stopAddr) {
        this.stopAddr = stopAddr;
    }

    @Override
    public Number getProfileTime() {
        return this.profileTime;
    }
}
