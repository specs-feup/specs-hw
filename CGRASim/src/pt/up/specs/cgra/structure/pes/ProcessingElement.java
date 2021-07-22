package pt.up.specs.cgra.structure.pes;

import pt.up.specs.cgra.dataypes.PEData;

public interface ProcessingElement {

    /*
     * Copy constructor
     */
    public ProcessingElement copy();

    /*
     * 
     */
    public int getLatency();

    /*
     * 
     */
    public boolean hasMemory();

    /*
     * 
     */
    public int getMemorySize();

    /*
     * 
     */
    public boolean isReady();

    /*
     * 
     */
    public boolean isExecuting();

    /*
     * 
     */
    public int getExecuteCount();

    /*
     * opIndex:
     * 0: lhs
     * 1: rhs
     */
    public boolean setOperand(int opIndex, PEData op);

    /*
     * Sets result register for the next operation to execute (this setting is/should be persistent)
     */
    public boolean setResultRegister(int regIndex);

    /*
     * Implements ONE execution tick (can be considered as a clock cycle)
     * this does not guarantee that the return is valid, only when
     * isReady returns true can the result be considered valid
     */
    public PEData execute();

    // public getImplementation ??

    // TODO: processingElement builder pattern?
    // peBuilder.withDataType(...).withMemory(memSize).modelLantency(2)... etc?
    // peBuilder.withClass(AdderElement.class).with... ???
}
