package pt.up.specs.cgra.structure.interconnect;

import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public class NearestNeighbour extends AInterconnect {

    public NearestNeighbour(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean connectionValid(ProcessingElementPort from, ProcessingElementPort to) {
        // TODO Auto-generated method stub
        return false;
    }
}
