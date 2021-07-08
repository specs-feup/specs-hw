package pt.up.fe.specs.binarytranslation.tracer;

import org.jgrapht.graph.DefaultWeightedEdge;

public class StreamUnitEdge extends DefaultWeightedEdge {

    /**
     * 
     */
    private static final long serialVersionUID = 1579359536265198438L;

    @Override
    public String toString() {
        return String.valueOf(super.getWeight());
    }

    @Override
    protected double getWeight() {
        return super.getWeight();
    }
}
