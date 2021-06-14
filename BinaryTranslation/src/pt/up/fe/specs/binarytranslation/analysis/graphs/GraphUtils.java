package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.awt.Desktop;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.traverse.BreadthFirstIterator;

import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexIsaInfo;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexType;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Class with static methods to manipulate memory address graphs
 * 
 * @author Tiago
 *
 */
public class GraphUtils {

    public static Graph<DataFlowVertex, DefaultEdge> mergeGraphs(List<Graph<DataFlowVertex, DefaultEdge>> graphs) {
        Graph<DataFlowVertex, DefaultEdge> merged = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (var graph : graphs) {
            Graphs.addGraph(merged, graph);
        }
        return merged;
    }

    public static DataFlowVertex findGraphRoot(Graph<DataFlowVertex, DefaultEdge> graph) {
        var root = DataFlowVertex.nullVertex;
        for (var v : graph.vertexSet()) {
            if (graph.outDegreeOf(v) == 0)
                root = v;
        }
        return root;
    }

    public static ArrayList<DataFlowVertex> getParents(Graph<DataFlowVertex, DefaultEdge> graph, DataFlowVertex current) {
        var res = new ArrayList<DataFlowVertex>();
        for (var edge : graph.edgesOf(current)) {
            if (graph.getEdgeSource(edge) != current)
                res.add(graph.getEdgeSource(edge));
        }
        return res;
    }

    public static DataFlowVertex getBaseAddr(Graph<DataFlowVertex, DefaultEdge> graph) {
        for (var v : graph.vertexSet()) {
            if (v.getProperty() == DataFlowVertexProperty.BASE_ADDR)
                return v;
        }
        return DataFlowVertex.nullVertex;
    }

    public static DataFlowVertex getOffset(Graph<DataFlowVertex, DefaultEdge> graph) {
        for (var v : graph.vertexSet()) {
            if (v.getProperty() == DataFlowVertexProperty.OFFSET)
                return v;
        }
        return DataFlowVertex.nullVertex;
    }

    public static DataFlowVertex getMemoryOp(Graph<DataFlowVertex, DefaultEdge> graph) {
        for (var v : graph.vertexSet()) {
            if (v.getType() == DataFlowVertexType.MEMORY)
                return v;
        }
        return DataFlowVertex.nullVertex;
    }

    public static DataFlowVertex getExpressionStart(Graph<DataFlowVertex, DefaultEdge> graph) {
        DataFlowVertex start = null;
        for (var v : graph.vertexSet()) {
            if (v.getType() == DataFlowVertexType.MEMORY)
                start = v;
        }
        for (var v : getParents(graph, start)) {
            if (v.getIsaInfo() != DataFlowVertexIsaInfo.RD)
                return v;
        }
        return DataFlowVertex.nullVertex;
    }

    public static Graph<DataFlowVertex, DefaultEdge> getExpressionGraph(Graph<DataFlowVertex, DefaultEdge> graph) {
        Graph<DataFlowVertex, DefaultEdge> sub = new DefaultDirectedGraph<>(DefaultEdge.class);
        Graphs.addGraph(sub, graph);
        var mem = findAllNodesOfType(sub, DataFlowVertexType.MEMORY);
        var rd = findAllNodesWithIsaInfo(graph, DataFlowVertexIsaInfo.RD);
        sub.removeAllVertices(mem);
        sub.removeAllVertices(rd);
        return sub;
    }

    public static String graphToDot(Graph<DataFlowVertex, DefaultEdge> graph, String title) {
        DOTExporter<DataFlowVertex, DefaultEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();

            String label = v.getLabel();
            if (v.getIsaInfo() != DataFlowVertexIsaInfo.NULL)
                label += "\n{" + v.getIsaInfo() + "}";
            if (v.getProperty() != DataFlowVertexProperty.NULL)
                label += "\n{" + v.getProperty() + "}";

            map.put("label", DefaultAttribute.createAttribute(label));
            map.put("type", DefaultAttribute.createAttribute(v.getType().toString()));
            map.put("color", DefaultAttribute.createAttribute(v.getColor()));
            map.put("penwidth", DefaultAttribute.createAttribute("2.5"));
            return map;
        });

        exporter.setEdgeAttributeProvider((e) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            var source = graph.getEdgeSource(e);
            var target = graph.getEdgeTarget(e);
            
            if (source.getColor().equals("red") && target.getColor().equals("red")) {
                map.put("color", DefaultAttribute.createAttribute("red"));
            }

            map.put("penwidth", DefaultAttribute.createAttribute("1.5"));
            return map;
        });

        Supplier<Map<String, Attribute>> sup = () -> Map.of("label", DefaultAttribute.createAttribute(title),
                "labelloc", DefaultAttribute.createAttribute("t"));
        exporter.setGraphAttributeProvider(sup);
        Writer writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        return writer.toString();
    }

    public static String graphToDot(Graph<DataFlowVertex, DefaultEdge> graph) {
        return graphToDot(graph, "\"Graph\"");
    }

    public static ArrayList<DataFlowVertex> findAllNodesOfType(Graph<DataFlowVertex, DefaultEdge> graph,
            DataFlowVertexType type) {
        var res = new ArrayList<DataFlowVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getType() == type)
                res.add(v);
        }
        return res;
    }

    public static ArrayList<DataFlowVertex> findAllNodesWithProperty(Graph<DataFlowVertex, DefaultEdge> graph,
            DataFlowVertexProperty property) {
        var res = new ArrayList<DataFlowVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getProperty() == property)
                res.add(v);
        }
        return res;
    }

    public static ArrayList<DataFlowVertex> findAllNodesWithIsaInfo(Graph<DataFlowVertex, DefaultEdge> graph,
            DataFlowVertexIsaInfo isaInfo) {
        var res = new ArrayList<DataFlowVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getIsaInfo() == isaInfo)
                res.add(v);
        }
        return res;
    }

    public static ArrayList<DataFlowVertex> findAllPredecessors(Graph<DataFlowVertex, DefaultEdge> graph,
            DataFlowVertex v) {
        var reversed = new EdgeReversedGraph<DataFlowVertex, DefaultEdge>(graph);
        return findAllSuccessors(reversed, v);
    }

    public static ArrayList<DataFlowVertex> findAllSuccessors(Graph<DataFlowVertex, DefaultEdge> graph, DataFlowVertex v) {
        var iter = new BreadthFirstIterator<DataFlowVertex, DefaultEdge>(graph, v);
        var res = new ArrayList<DataFlowVertex>();

        while (iter.hasNext()) {
            res.add(iter.next());
        }
        return res;
    }

    public static List<DataFlowVertex> getVerticesWithType(Graph<DataFlowVertex, DefaultEdge> graph,
            DataFlowVertexType type) {
        var ret = new ArrayList<DataFlowVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getType() == type)
                ret.add(v);
        }
        return ret;
    }

    public static String pathBetweenTwoVertices(Graph<DataFlowVertex, DefaultEdge> graph, DataFlowVertex source,
            DataFlowVertex sink) {
        return pathBetweenTwoVertices(graph, source, sink, 0);
    }

    public static String pathBetweenTwoVertices(Graph<DataFlowVertex, DefaultEdge> graph, DataFlowVertex source,
            DataFlowVertex sink, int limit) {
        var dijkstra = new DijkstraShortestPath<DataFlowVertex, DefaultEdge>(graph);
        var path = dijkstra.getPath(source, sink);
        if (path == null) {
            return "No path";
        }
        var pathList = path.getVertexList();

        var strPath = new ArrayList<String>();
        for (var v : pathList)
            strPath.add(v.getLabel());

        if (limit == 0 || limit > strPath.size())
            return String.join("->", strPath);
        else
            return String.join("->", strPath.subList(0, limit));
    }

    public static String generateGraphURL(String dotGraph) {
        String base = "https://dreampuf.github.io/GraphvizOnline/#";
        try {
            URI uri;
            uri = new URI(
                    base,
                    dotGraph,
                    null);
            String url = uri.toASCIIString();
            url = url.replace("#:", "#");
            return url;
        } catch (URISyntaxException e) {
            SpecsLogs.warn("Error message:\n", e);
        }
        return base;
    }

    public static String generateGraphURL(Graph<DataFlowVertex, DefaultEdge> graph) {
        return GraphUtils.generateGraphURL(graph, "Graph");
    }

    public static void openGraphInBrowser(String dotGraph) {
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            // specify the protocol along with the URL
            URI oURL = new URI(
                    "https://dreampuf.github.io/GraphvizOnline/#",
                    dotGraph,
                    null);
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String generateGraphURL(Graph<DataFlowVertex, DefaultEdge> graph, String name) {
        var str = GraphUtils.graphToDot(graph, name);
        return generateGraphURL(str);
    }
}
