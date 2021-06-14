package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex;

public abstract class AGraphTransform {
    protected Graph<DataFlowVertex, DefaultEdge> graph;
    
    public AGraphTransform(Graph<DataFlowVertex, DefaultEdge> graph) {
        this.graph = graph;
    }
    
    public Graph<DataFlowVertex, DefaultEdge> applyToGraph() {
        applyTransform(graph);
        return graph;
    }
    
    public Graph<DataFlowVertex, DefaultEdge> applyToCopy() {
        //TODO
        return graph;
    }
    
    protected abstract Graph<DataFlowVertex, DefaultEdge> applyTransform(Graph<DataFlowVertex, DefaultEdge> g);
}
