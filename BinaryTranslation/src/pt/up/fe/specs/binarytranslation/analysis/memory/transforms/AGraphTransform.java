package pt.up.fe.specs.binarytranslation.analysis.memory.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;

public abstract class AGraphTransform {
    protected Graph<AddressVertex, DefaultEdge> graph;
    
    public AGraphTransform(Graph<AddressVertex, DefaultEdge> graph) {
        this.graph = graph;
    }
    
    public Graph<AddressVertex, DefaultEdge> applyToGraph() {
        applyTransform(graph);
        return graph;
    }
    
    public Graph<AddressVertex, DefaultEdge> applyToCopy() {
        //TODO
        return graph;
    }
    
    protected abstract Graph<AddressVertex, DefaultEdge> applyTransform(Graph<AddressVertex, DefaultEdge> g);
}
