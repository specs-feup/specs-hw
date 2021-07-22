package pt.up.specs.cgra.structure.interconnect;

import java.util.ArrayList;
import java.util.HashMap;
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

    public AInterconnect() {
        this.connections = new HashMap<ProcessingElementPort, List<ProcessingElementPort>>();
    }

    /**
     * For every connection, copy the output payload into the input ports of each @ProcessingElement
     */
    @Override
    public boolean propagate() {

        for (var drive : this.connections.keySet()) {
            var drivenList = this.connections.get(drive);
            for (var drivenPort : drivenList)
                drivenPort.setPayload(drive.getPayload().copy()); // copy the data element
        }

        return true;
    }

    @Override
    public boolean setConnection(ProcessingElementPort from, ProcessingElementPort to) {

        if (!connectionValid(from, to))
            return false;

        // get list for this driving port, i.e., "from"
        if (this.connections.containsKey(from)) {
            var drivenList = this.connections.get(from);
            if (!drivenList.contains(to))
                drivenList.add(to);

        } else {
            var newList = new ArrayList<ProcessingElementPort>();
            newList.add(to);
            this.connections.put(from, newList);
        }

        return true;
    }

    @Override
    public ProcessingElementPort findDriver(ProcessingElementPort to) {

        ProcessingElementPort driver = null;
        for (var drive : this.connections.keySet()) {
            var drivenList = this.connections.get(drive);
            if (drivenList.contains(to)) {
                driver = drive;
                break;
            }
        }
        return driver;

    }
}
