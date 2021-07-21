package pt.up.specs.cgra.pes;

public interface ProcessingElement {

    /*
     * 
     */
    public int getLatency();

    /*
     * 
     */
    public boolean hasMemory();

    // public getImplementation ??

    // TODO: processingElement builder pattern?
    // peBuilder.withDataType(...).withMemory(memSize).modelLantency(2)... etc?
    // peBuilder.withClass(AdderElement.class).with... ???
}
