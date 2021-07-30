package pt.up.specs.cgra.structure.mesh;

import java.util.List;

import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

/**
 * Helper class to hold the List<List<ProcessingElement>> mesh, reduces some verbosity
 * 
 * @author nuno
 *
 */
public class Mesh {

    private final SpecsCGRA myparent;
    private final int x, y;
    private final List<List<ProcessingElement>> mesh;

    public Mesh(List<List<ProcessingElement>> mesh, SpecsCGRA myparent) {
        this.myparent = myparent;
        this.mesh = mesh;
        this.x = mesh.size();
        this.y = mesh.get(0).size();
        for (int i = 0; i < this.x; i++)
            for (int j = 0; j < this.y; j++) {
                var pe = this.mesh.get(i).get(j);
                pe.setX(i);
                pe.setY(j);
                pe.setMesh(this);
            }
    }

    /*
     * 
     */
    public SpecsCGRA getCGRA() {
        return this.myparent;
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

    public void execute() {
        for (var line : this.mesh)
            for (var pe : line)
                pe.execute();
    }

    public String visualize() {
        var sbld = new StringBuilder();
        var str = "------------------";
        for (var line : this.mesh) {
            sbld.append(str.repeat(line.size()) + "\n");
            for (var pe : line) {
                sbld.append("|  " + pe.toString() + "  |");
            }
            sbld.append("\n");
        }
        sbld.append(str.repeat(this.x));
        return sbld.toString();
    }
}
