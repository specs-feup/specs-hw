package pt.up.specs.cgra.pes;

import pt.up.specs.cgra.dataypes.ProcessingElementDataType;

public interface ProcessingElement {

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
    public boolean setOperand(int opIndex, ProcessingElementDataType op);

    /*
     * Sets result register for the next operation to execute (this setting is/should be persistent)
     */
    public boolean setResultRegister(int regIndex);

    /*
     * Implements ONE execution tick (can be considered as a clock cycle)
     * this does not guarantee that the return is valid, only when
     * isReady returns true can the result be considered valid
     */
    public ProcessingElementDataType execute();

    /*
     * Unary operations must implement this (binary operations can override it with the binary version where opB = 0) ?
    
    public ProcessingElementDataType execute(ProcessingElementDataType opA);
    
    
     * Binary operations must implement this (two external operands)
    
    public ProcessingElementDataType execute(ProcessingElementDataType opA, ProcessingElementDataType opB);
    
    
     * Binary operations must implement this (rhs operand from internal register file index)
     * If PE doesnt have memory, then this method throws exception?
    
    public ProcessingElementDataType execute(ProcessingElementDataType opA, int rfIndex);
    */

    // public getImplementation ??

    // TODO: processingElement builder pattern?
    // peBuilder.withDataType(...).withMemory(memSize).modelLantency(2)... etc?
    // peBuilder.withClass(AdderElement.class).with... ???
}
