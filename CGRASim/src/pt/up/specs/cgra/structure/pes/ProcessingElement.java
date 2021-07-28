package pt.up.specs.cgra.structure.pes;

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.mesh.Mesh;

public interface ProcessingElement {

    /*
     * 
     */
    default public boolean setParent(Mesh mesh) {
        return false;
    }

    /*
     * 
     */
    default public Mesh getParent() {
        return null;
    }

    /*
     * 
     */
    default public boolean setX(int x) {
        return false;
    }

    /*
     * 
     */
    default public boolean setY(int y) {
        return false;
    }

    /*
     * 
     */
    default public SpecsCGRA getCGRA() {
        return null;
    }

    /*
     * 
     */
    default public int getX() {
        return -1;
    }

    /*
     * 
     */
    default public int getY() {
        return -1;
    }

    /*
     * Copy constructor
     */
    default public ProcessingElement copy() {
        return null;
    }

    /*
     * 
     */
    default public int getLatency() {
        return 1;
    }

    /*
     * 
     */
    default public boolean hasMemory() {
        return false;
    }

    /*
     * 
     */
    default public int getMemorySize() {
        return 0;
    }

    /*
     * 
     */
    default public boolean isReady() {
        return false;
    }

    /*
     * 
     */
    default public boolean isExecuting() {
        return false;
    }

    /*
     * 
     */
    default public int getExecuteCount() {
        return 0;
    }

    /*
     * opIndex:
     * 0: lhs
     * 1: rhs
     */
    // public boolean setOperand(int opIndex, PEData op);

    /*
     * Sets result register for the next operation to execute (this setting is/should be persistent)
     */
    default public boolean setResultRegister(int regIndex) {
        return false;
    }

    /*
     * Implements ONE execution tick (can be considered as a clock cycle)
     * this does not guarantee that the return is valid, only when
     * isReady returns true can the result be considered valid
     */
    default public PEData execute() {
        return null;
    }

    /*
     * 
     */
    default public String statusString() {
        return "No status";
    }

    // public getImplementation ??

    // TODO: processingElement builder pattern?
    // peBuilder.withDataType(...).withMemory(memSize).modelLantency(2)... etc?
    // peBuilder.withClass(AdderElement.class).with... ???
}
