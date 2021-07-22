package pt.up.specs.cgra.mesh;

import java.util.ArrayList;
import java.util.List;

import pt.up.specs.cgra.interconnect.Interconnect;
import pt.up.specs.cgra.pes.NullPE;
import pt.up.specs.cgra.pes.ProcessingElement;

public class GenericSpecsCGRA implements SpecsCGRA {

    // TODO: extend this class with CGRAs with global memories
    // and memory ports

    // string name?
    protected Interconnect interconnect;
    protected List<List<ProcessingElement>> mesh;

    /*
     * for use by the builder
     */
    private GenericSpecsCGRA() {

    }

    private GenericSpecsCGRA(List<List<ProcessingElement>> mesh, Interconnect interconnect) {
        this.mesh = mesh;
        this.interconnect = interconnect;
    }

    @Override
    public ProcessingElement getProcessingElement(int x, int y) {
        return this.mesh.get(x).get(y);
    }

    @Override
    public Interconnect getInterconnect() {
        return this.interconnect;
    }

    @Override
    public boolean execute() {
        for (var line : mesh)
            for (var pe : line)
                pe.execute();

        return true; // eventually use this return to indicate stalling or something
    }

    // TODO: use a graphical representation later
    @Override
    public void visualize() {
        var sbld = new StringBuilder();
        var str = "------------------";
        for (var line : mesh) {
            sbld.append(str.repeat(line.size()) + "\n");
            for (var pe : line) {
                sbld.append("|  " + pe.toString() + "  |");
            }
            sbld.append("\n");
        }
        sbld.append(str.repeat(this.mesh.get(0).size()));
        System.out.println(sbld.toString());
    }

    /*
     * 
     */
    public static class Builder extends GenericSpecsCGRA {

        protected int meshX, meshY;

        /*
         * mesh size is mandatory before any "with..." calls
         */
        public Builder(int x, int y) {
            this.meshX = x;
            this.meshY = y;
            this.mesh = new ArrayList<List<ProcessingElement>>(x);
            for (int i = 0; i < x; i++) {
                this.mesh.add(new ArrayList<ProcessingElement>(y));
                for (int j = 0; j < y; j++) {
                    this.mesh.get(i).add(new NullPE());
                }
            }
        }

        /*
         * "pe" must be copied for each grid position by deep copy
         */
        public Builder withHomogeneousPE(ProcessingElement pe) {
            for (int i = 0; i < this.meshX; i++)
                for (int j = 0; j < this.meshY; j++)
                    this.mesh.get(i).set(j, pe.copy());
            return this;
        }

        public Builder withProcessingElement(ProcessingElement pe, int x, int y) {
            this.mesh.get(x).set(y, pe);
            return this;
        }

        public GenericSpecsCGRA build() {
            return new GenericSpecsCGRA(this.mesh, this.interconnect);
        }
    }
}
