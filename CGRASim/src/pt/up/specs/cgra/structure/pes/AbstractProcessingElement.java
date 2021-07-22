package pt.up.specs.cgra.structure.pes;

import java.util.ArrayList;
import java.util.List;

import pt.up.specs.cgra.dataypes.PEData;

public abstract class AbstractProcessingElement implements ProcessingElement {

    /*
     * 
     */
    // private String peID;
    private int xPos, yPos;
    private int latency = 1;
    private boolean hasMemory = false;
    private boolean ready = true;
    private boolean executing = false;
    private int executeCount = 0;

    /*
     * register file
     */
    private List<PEData> registerFile;
    private int memorySize = 0;
    private int writeIdx = 0;

    /*
     * initialized by children
     */
    protected List<ProcessingElementPort> ports;

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
        } else {
            this.hasMemory = false;
        }
    }

    protected AbstractProcessingElement(int latency) {
        this(latency, 0);
    }

    protected AbstractProcessingElement() {
        this(1);
    }

    @Override
    public boolean setX(int x) {
        if (this.xPos != -1)
            return false;
        else {
            this.xPos = x;
            return true;
        }
    }

    @Override
    public boolean setY(int y) {
        if (this.yPos != -1)
            return false;
        else {
            this.yPos = y;
            return true;
        }
    }

    @Override
    public int getX() {
        return this.xPos;
    }

    @Override
    public int getY() {
        return this.yPos;
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
    public int getMemorySize() {
        return memorySize;
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

    /* @Override
    public boolean setOperand(int opIndex, PEData op) {
        if (opIndex < this.ports.size()) {
            this.operands.set(opIndex, op);
            return true;
        } else {
            return false;
        }
    }*/

    /*
     * Implemented by children
     */
    protected abstract PEData _execute();

    /*
     * Use by children
     */
    protected PEData getOperand(int idx) {
        return this.ports.get(idx).getPayload();
    }

    @Override
    public PEData execute() {

        // TODO: implement latency! requires a counter which sets ready = false and executing = true

        var result = _execute();
        this.executeCount++;
        this.ports.get(this.ports.size() - 1).setPayload(result);
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
