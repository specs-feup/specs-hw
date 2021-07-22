package pt.up.specs.cgra.structure.interconnect;

import java.util.List;
import java.util.Map;

import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public abstract class AInterconnect implements Interconnect {

    // TODO: each interconnect class should only hold a set of permitted connection rules
    // the AInterconnect should implement all methods?

    // protected final RULES = ??? some kind of map of possible connections?

    // reference to the mesh?
    // private final Mesh mesh;
    private Map<ProcessingElementPort, List<ProcessingElementPort>> connections;

    /**
     * For every connection, copy the output payload into the input ports of each @ProcessingElement
     */
    @Override
    public boolean propagate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean setConnection(ProcessingElementPort from, ProcessingElementPort to) {

        if (!connectionValid(from, to))
            return false;

        to.setPayload(from.getPayload());
        return true;
    }
}
