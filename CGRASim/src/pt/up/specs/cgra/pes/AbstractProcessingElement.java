package pt.up.specs.cgra.pes;

public class AbstractProcessingElement implements ProcessingElement {

    final int latency = 0;
    final boolean hasMemory = false;

    // TODO: each processing element will need a map of operations which can be validly mapped to it
    // so that the scheduler holding the CGRA object can receive success or failure states during
    // scheduling
    // the map should be in the childmost class (maybe?)

    protected AbstractProcessingElement() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getLatency() {
        return latency;
    }

    @Override
    public boolean hasMemory() {
        return hasMemory;
    }
}
