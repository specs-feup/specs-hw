package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 * Class with static methods to manipulate memory address graphs
 * @author Tiago
 *
 */
public class GraphUtils {

    public static Graph<AddressVertex, DefaultEdge> mergeGraphs(ArrayList<Graph<AddressVertex, DefaultEdge>> graphs) {
        Graph<AddressVertex, DefaultEdge> merged = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (var graph : graphs) {
            Graphs.addGraph(merged, graph);
        }
        return merged;
    }

    public static AddressVertex findGraphRoot(Graph<AddressVertex, DefaultEdge> graph) {
        var root = AddressVertex.nullVertex;
        for (var v : graph.vertexSet()) {
            if (graph.outDegreeOf(v) == 0)
                root = v;
        }
        return root;
    }

    public static ArrayList<AddressVertex> getParents(Graph<AddressVertex, DefaultEdge> graph, AddressVertex current) {
        var res = new ArrayList<AddressVertex>();
        for (var edge : graph.edgesOf(current)) {
            if (graph.getEdgeSource(edge) != current)
                res.add(graph.getEdgeSource(edge));
        }
        return res;
    }

}
