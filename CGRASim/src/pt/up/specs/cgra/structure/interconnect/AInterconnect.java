package pt.up.specs.cgra.structure.interconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public abstract class AInterconnect implements Interconnect {

    // TODO: each interconnect class should only hold a set of permitted connection rules
    // the AInterconnect should implement all methods?

    // protected final RULES = ??? some kind of map of possible connections?

    private final SpecsCGRA myparent;
    private Context currentContext;
    private Map<ProcessingElementPort, List<ProcessingElementPort>> connections;

    public AInterconnect(SpecsCGRA myparent) {
        this.myparent = myparent;
        this.currentContext = null;
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
    public boolean applyContext(Context ctx) {
        var connections = ctx.getConnections();
        for (var driver : connections.keySet()) {
            for (var sink : connections.get(driver))
                if (!this.setConnection(driver, sink))
                    return false;
            /*
             * TODO: create exception classes to handle these errors
             */
        }
        this.currentContext = ctx;
        return true;
    }

    @Override
    public Context getContext() {
        return this.currentContext;
    }

    @Override
    public SpecsCGRA getCGRA() {
        return this.myparent;
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
