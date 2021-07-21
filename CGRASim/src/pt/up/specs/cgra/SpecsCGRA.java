package pt.up.specs.cgra;

import pt.up.specs.cgra.pes.ProcessingElement;

public interface SpecsCGRA {

    public ProcessingElement getProcessingElement(int x, int y);

    public boolean setConnection(ProcessingElement from, ProcessingElement to);

    // some kind of method that recieves an emitter class
    // which lowers this functional spec into chisel3 or hdl
    // along with whatever other artefecats are necessary
    // public IMplementation(?) toImplementation(Implementer hdlImplementer);
}
