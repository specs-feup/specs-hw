package pt.up.specs.cgra.structure.interconnect;

import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public class NearestNeighbour extends AInterconnect {

    public NearestNeighbour(SpecsCGRA myparent) {
        super(myparent);
    }

    @Override
    public boolean connectionValid(ProcessingElementPort from, ProcessingElementPort to) {
        var fromPE = from.getPE();
        var toPE = from.getPE();
        var distX = Math.abs(fromPE.getX() - toPE.getX());
        var distY = Math.abs(fromPE.getY() - toPE.getY());

        // diagonal (of any length)
        if (distX > 0 && distY > 0)
            return false;

        else if (distX == 1 && distY == 0)
            return true;

        else if (distX == 0 && distY == 1)
            return true;

        // anything else
        else
            return false;
    }
}
