package pt.up.specs.cgra.mesh;

import pt.up.specs.cgra.interconnect.Interconnect;
import pt.up.specs.cgra.pes.ProcessingElement;

public interface SpecsCGRA {

    /*
     * 
     */
    public ProcessingElement getProcessingElement(int x, int y);

    /*
     * Interconnect at
     */
    public Interconnect getInterconnect();

    /*
     * Executes a single simulation tick (can be considered a clock cycle)
     */
    public boolean execute();

    /*
     * Generate a visual representation of the current CGRA status
     * (Use JFreeChart or similar?)
     */
    public void visualize();

    // some kind of method that recieves an emitter class
    // which lowers this functional spec into chisel3 or hdl
    // along with whatever other artefecats are necessary
    // public IMplementation(?) toImplementation(Implementer hdlImplementer);
}
