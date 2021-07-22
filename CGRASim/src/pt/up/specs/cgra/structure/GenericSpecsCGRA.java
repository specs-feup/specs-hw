package pt.up.specs.cgra.structure;

import java.util.ArrayList;
import java.util.List;

import pt.up.specs.cgra.structure.interconnect.Interconnect;
import pt.up.specs.cgra.structure.interconnect.NearestNeighbour;
import pt.up.specs.cgra.structure.mesh.Mesh;
import pt.up.specs.cgra.structure.pes.NullPE;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

public class GenericSpecsCGRA implements SpecsCGRA {

    // TODO: extend this class with CGRAs with global memories
    // and memory ports

    // TODO: list of contexts is required to switch connections
    // the switch operation can model the latency upon call

    // string name?
    protected final Mesh mesh;
    protected final Interconnect interconnect;

    private GenericSpecsCGRA(Mesh mesh, Interconnect interconnect) {
        this.mesh = mesh;
        this.interconnect = interconnect;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public Interconnect getInterconnect() {
        return this.interconnect;
    }

    @Override
    public boolean execute() {

        // propagate data from interconnect settings
        this.interconnect.propagate();

        // execute compute
        for (var line : this.mesh.getMesh())
            for (var pe : line)
                pe.execute();

        return true; // eventually use this return to indicate stalling or something
    }

    // TODO: use a graphical representation later
    @Override
    public void visualize() {
        var sbld = new StringBuilder();
        var str = "------------------";
        for (var line : this.mesh.getMesh()) {
            sbld.append(str.repeat(line.size()) + "\n");
            for (var pe : line) {
                sbld.append("|  " + pe.toString() + "  |");
            }
            sbld.append("\n");
        }
        sbld.append(str.repeat(this.mesh.getX()));
        System.out.println(sbld.toString());
    }

    /**
     * Builder class for @GenericSpecsCGRA
     */
    public static class Builder extends GenericSpecsCGRA {

        private int meshX, meshY;
        private final List<List<ProcessingElement>> mesh;
        private Interconnect intc = null;

        /*
         * mesh size is mandatory before any "with..." calls
         */
        public Builder(int x, int y) {
            super(null, null);
            this.meshX = x;
            this.meshY = y;
            this.mesh = new ArrayList<List<ProcessingElement>>(x);
            for (int i = 0; i < x; i++) {
                this.mesh.add(new ArrayList<ProcessingElement>(y));
                for (int j = 0; j < y; j++) {
                    this.mesh.get(i).add(new NullPE());
                }
            }
            this.intc = new NearestNeighbour(this.meshX, this.meshY); // default
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

        public Builder withNearestNeighbourInterconnect() {
            this.intc = new NearestNeighbour(this.meshX, this.meshY);
            return this;
        }

        public GenericSpecsCGRA build() {
            return new GenericSpecsCGRA(new Mesh(this.mesh), this.intc);
        }
    }
}
