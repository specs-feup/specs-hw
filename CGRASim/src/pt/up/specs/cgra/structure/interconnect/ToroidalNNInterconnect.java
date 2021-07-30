package pt.up.specs.cgra.structure.interconnect;

import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public class ToroidalNNInterconnect extends NearestNeighbour {

    /*
     * 
     */
    public ToroidalNNInterconnect(SpecsCGRA myparent) {
        super(myparent);
    }

    /*
     * Toroidal IC allows for edge PEs to communicate with the edge 
     * on the other extreme (i.e., wrap around)
     * 
     * ToroidalNN only allows up/down/left/right connections (not diagonals)
     */
    @Override
    public boolean connectionValid(ProcessingElementPort from, ProcessingElementPort to) {

        // check if basic nearest neighbour
        if (super.connectionValid(from, to))
            return true;

        // if not normal neighbour, check if wrap around
        var fromPE = from.getPE();
        var toPE = from.getPE();
        var distX = Math.abs(fromPE.getX() - toPE.getX());
        var distY = Math.abs(fromPE.getY() - toPE.getY());

        var xmax = this.getCGRA().getMesh().getX();
        var ymax = this.getCGRA().getMesh().getY();

        // if same row, opposing sides
        if (fromPE.getX() == toPE.getX() && (distX == xmax - 1)) {
            return true;
        }

        // if same column, opposing sides
        else if (fromPE.getY() == toPE.getY() && (distY == ymax - 1)) {
            return true;
        }

        // not nn toroidal
        else {
            return false;
        }
    }
}
