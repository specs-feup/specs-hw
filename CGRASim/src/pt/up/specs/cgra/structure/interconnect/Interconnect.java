package pt.up.specs.cgra.structure.interconnect;

import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public interface Interconnect {

    /**
     * Propagates data to @ProcessingElement inputs based on interconnect settings
     */
    public boolean propagate();

    /*
     * 
     */
    public boolean setConnection(ProcessingElementPort from, ProcessingElementPort to);

    /*
     * todo: queries for valid connection
     */
    public boolean connectionValid(ProcessingElementPort from, ProcessingElementPort to);
}