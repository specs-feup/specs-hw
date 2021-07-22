package pt.up.specs.cgra.structure;

import pt.up.specs.cgra.structure.interconnect.Interconnect;
import pt.up.specs.cgra.structure.mesh.Mesh;

public interface SpecsCGRA {

    /*
     * 
     */
    public Mesh getMesh();

    /*
     * Interconnect
     */
    public Interconnect getInterconnect();

    /*
     * Executes a single simulation tick (can be considered a clock cycle)
     */
    public boolean execute();

    /*
     * switch between one of X (max) available contexts
     */
    // public boolean switchContext();

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
