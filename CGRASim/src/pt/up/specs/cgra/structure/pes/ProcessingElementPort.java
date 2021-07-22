package pt.up.specs.cgra.structure.pes;

import pt.up.specs.cgra.dataypes.PEData;

public class ProcessingElementPort {

    public enum PEPortDirection {
        input,
        output
    }

    // private String portID;
    private PEPortDirection dir;
    private ProcessingElement PE;
    private PEData payload;

    public ProcessingElementPort(ProcessingElement myParent, PEPortDirection dir, PEData payload) {
        this.PE = myParent;
        this.dir = dir;
        this.payload = payload;
    }

    public ProcessingElement getPE() {
        return PE;
    }

    public PEData getPayload() {
        return payload;
    }

    public PEPortDirection getDir() {
        return dir;
    }
}
