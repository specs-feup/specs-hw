package pt.up.specs.cgra.pes;

import java.util.ArrayList;
import java.util.List;

import pt.up.specs.cgra.dataypes.PEData;

public abstract class AbstractProcessingElement implements ProcessingElement {

    /*
     * 
     */
    int latency = 1;
    boolean hasMemory = false;
    boolean ready = true;
    boolean executing = false;
    int executeCount = 0;

    /*
     * register file
     */
    private List<PEData> registerFile;
    int memorySize = 0;
    int writeIdx = 0;

    // TODO: each processing element will need a map of operations which can be validly mapped to it
    // so that the scheduler holding the CGRA object can receive success or failure states during
    // scheduling
    // the map should be in the childmost class (maybe?)

    protected AbstractProcessingElement(int latency, int memorySize) {
        this.latency = latency;
        this.memorySize = memorySize;
        if (this.memorySize > 0) {
            this.hasMemory = true;
            this.registerFile = new ArrayList<PEData>(memorySize);
        }
    }

    protected AbstractProcessingElement(int latency) {
        this.latency = latency;
        this.hasMemory = false;
    }

    protected AbstractProcessingElement() {
        this(1);
    }

    @Override
    public int getLatency() {
        return latency;
    }

    @Override
    public boolean hasMemory() {
        return hasMemory;
    }

    @Override
    public boolean isExecuting() {
        return this.executing;
    }

    @Override
    public boolean isReady() {
        return this.ready;
    }

    @Override
    public int getExecuteCount() {
        return this.executeCount;
    }

    @Override
    public boolean setResultRegister(int regIndex) {
        if (regIndex < this.memorySize) {
            this.writeIdx = regIndex;
            return true;

        } else {
            return false;
        }
    }

    @Override
    public boolean setOperand(int opIndex, PEData op) {
        if (opIndex < this.operands.size()) {
            this.operands.set(opIndex, op);
            return true;
        } else {
            return false;
        }
    }

    /*
     * initialized by children
     */
    protected List<PEData> operands;

    /*
     * Implemented by children
     */
    protected abstract PEData _execute();

    /*
     * Use by children
     */
    protected PEData getOperand(int idx) {
        return this.operands.get(idx);
    }

    @Override
    public PEData execute() {

        // TODO: implement latency! requires a counter which sets ready = false and executing = true

        var result = _execute();
        this.executeCount++;
        if (this.writeIdx != -1)
            this.registerFile.set(this.writeIdx, result);
        return result;
    }

    /*
     * 
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
