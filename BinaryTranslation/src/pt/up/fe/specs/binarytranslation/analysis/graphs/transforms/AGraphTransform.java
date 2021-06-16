package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;

public abstract class AGraphTransform {
    protected Graph<BtfVertex, DefaultEdge> graph;
    
    public AGraphTransform(Graph<BtfVertex, DefaultEdge> graph) {
        this.graph = graph;
    }
    
    public Graph<BtfVertex, DefaultEdge> applyToGraph() {
        applyTransform(graph);
        return graph;
    }
    
    public Graph<BtfVertex, DefaultEdge> applyToCopy() {
        Graph<BtfVertex, DefaultEdge> copy = new DefaultDirectedGraph<>(DefaultEdge.class);
        Graphs.addGraph(copy, graph);
        applyTransform(copy);
        return copy;
    }
    
    protected abstract Graph<BtfVertex, DefaultEdge> applyTransform(Graph<BtfVertex, DefaultEdge> g);
}
