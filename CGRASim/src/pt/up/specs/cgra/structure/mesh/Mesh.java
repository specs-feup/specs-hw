package pt.up.specs.cgra.structure.mesh;

import java.util.List;

import pt.up.specs.cgra.structure.pes.ProcessingElement;

/**
 * Helper class to hold the List<List<ProcessingElement>> mesh, reduces some verbosity
 * 
 * @author nuno
 *
 */
public class Mesh {

    private final int x, y;
    private final List<List<ProcessingElement>> mesh;

    public Mesh(List<List<ProcessingElement>> mesh) {
        this.mesh = mesh;
        this.x = mesh.size();
        this.y = mesh.get(0).size();
    }

    public List<List<ProcessingElement>> getMesh() {
        return mesh;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ProcessingElement getProcessingElement(int x, int y) {
        return this.mesh.get(x).get(y);
    }

}
