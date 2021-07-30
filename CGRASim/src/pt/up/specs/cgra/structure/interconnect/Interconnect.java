package pt.up.specs.cgra.structure.interconnect;

import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public interface Interconnect {

    /*
     * 
     */
    public SpecsCGRA getCGRA();

    /**
     * Propagates data to @ProcessingElement inputs based on interconnect settings
     */
    public boolean propagate();

    /*
     * Set an individual connection in the internconnect
     */
    public boolean setConnection(ProcessingElementPort from, ProcessingElementPort to);

    /*
     * Apply an entire connection context to the interconnect
     */
    public boolean applyContext(Context ctx);

    /*
     * get current interconnection context
     */
    public Context getContext();

    /*
     * 
     */
    public ProcessingElementPort findDriver(ProcessingElementPort to);

    /*
     * 
     */
    public boolean connectionValid(ProcessingElementPort from, ProcessingElementPort to);
}
