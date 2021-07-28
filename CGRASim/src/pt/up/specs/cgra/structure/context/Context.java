package pt.up.specs.cgra.structure.context;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public class Context {

    /*
     * contexts are final, and are applied to the interconnect by iterating
     * over the entire map of source ports to destination ports
     */
    private int contextID;
    private final Map<ProcessingElementPort, List<ProcessingElementPort>> connections;

    public Context(int contextID, Map<ProcessingElementPort, List<ProcessingElementPort>> connections) {
        this.contextID = contextID;
        this.connections = connections;
    }

    public int getContextID() {
        return contextID;
    }

    public Map<ProcessingElementPort, List<ProcessingElementPort>> getConnections() {
        return connections;
    }

    public Optional<List<ProcessingElementPort>> getDestinationsOf(ProcessingElementPort source) {
        return Optional.of(this.connections.get(source));
    }
}
