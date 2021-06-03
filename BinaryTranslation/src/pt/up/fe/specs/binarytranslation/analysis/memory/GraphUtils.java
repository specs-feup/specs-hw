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

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexIsaInfo;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Class with static methods to manipulate memory address graphs
 * 
 * @author Tiago
 *
 */
public class GraphUtils {

    public static Graph<AddressVertex, DefaultEdge> mergeGraphs(List<Graph<AddressVertex, DefaultEdge>> graphs) {
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

    public static AddressVertex getBaseAddr(Graph<AddressVertex, DefaultEdge> graph) {
        for (var v : graph.vertexSet()) {
            if (v.getProperty() == AddressVertexProperty.BASE_ADDR)
                return v;
        }
        return AddressVertex.nullVertex;
    }

    public static AddressVertex getOffset(Graph<AddressVertex, DefaultEdge> graph) {
        for (var v : graph.vertexSet()) {
            if (v.getProperty() == AddressVertexProperty.OFFSET)
                return v;
        }
        return AddressVertex.nullVertex;
    }

    public static AddressVertex getMemoryOp(Graph<AddressVertex, DefaultEdge> graph) {
        for (var v : graph.vertexSet()) {
            if (v.getType() == AddressVertexType.MEMORY)
                return v;
        }
        return AddressVertex.nullVertex;
    }

    public static AddressVertex getExpressionStart(Graph<AddressVertex, DefaultEdge> graph) {
        AddressVertex start = null;
        for (var v : graph.vertexSet()) {
            if (v.getType() == AddressVertexType.MEMORY)
                start = v;
        }
        for (var v : getParents(graph, start)) {
            if (v.getIsaInfo() != AddressVertexIsaInfo.RD)
                return v;
        }
        return AddressVertex.nullVertex;
    }

    public static Graph<AddressVertex, DefaultEdge> getExpressionGraph(Graph<AddressVertex, DefaultEdge> graph) {
        Graph<AddressVertex, DefaultEdge> sub = new DefaultDirectedGraph<>(DefaultEdge.class);
        Graphs.addGraph(sub, graph);
        var mem = findAllNodesOfType(sub, AddressVertexType.MEMORY);
        var rd = findAllNodesWithIsaInfo(graph, AddressVertexIsaInfo.RD);
        sub.removeAllVertices(mem);
        sub.removeAllVertices(rd);
        return sub;
    }

    public static String graphToDot(Graph<AddressVertex, DefaultEdge> graph, String title) {
        DOTExporter<AddressVertex, DefaultEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();

            String label = v.getLabel();
            if (v.getIsaInfo() != AddressVertexIsaInfo.NULL)
                label += "\n{" + v.getIsaInfo() + "}";
            if (v.getProperty() != AddressVertexProperty.NULL)
                label += "\n{" + v.getProperty() + "}";

            map.put("label", DefaultAttribute.createAttribute(label));
            map.put("type", DefaultAttribute.createAttribute(v.getType().toString()));
            map.put("color", DefaultAttribute.createAttribute(v.getColor()));
            map.put("penwidth", DefaultAttribute.createAttribute("2.5"));
            return map;
        });

        exporter.setEdgeAttributeProvider((e) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();

            if (graph.getEdgeSource(e).getColor().equals("red") && graph.getEdgeTarget(e).getColor().equals("red"))
                map.put("color", DefaultAttribute.createAttribute("red"));

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

    public static String graphToDot(Graph<AddressVertex, DefaultEdge> graph) {
        return graphToDot(graph, "\"Graph\"");
    }

    public static ArrayList<AddressVertex> findAllNodesOfType(Graph<AddressVertex, DefaultEdge> graph,
            AddressVertexType type) {
        var res = new ArrayList<AddressVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getType() == type)
                res.add(v);
        }
        return res;
    }

    public static ArrayList<AddressVertex> findAllNodesWithProperty(Graph<AddressVertex, DefaultEdge> graph,
            AddressVertexProperty property) {
        var res = new ArrayList<AddressVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getProperty() == property)
                res.add(v);
        }
        return res;
    }

    public static ArrayList<AddressVertex> findAllNodesWithIsaInfo(Graph<AddressVertex, DefaultEdge> graph,
            AddressVertexIsaInfo isaInfo) {
        var res = new ArrayList<AddressVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getIsaInfo() == isaInfo)
                res.add(v);
        }
        return res;
    }

    public static ArrayList<AddressVertex> findAllPredecessors(Graph<AddressVertex, DefaultEdge> graph,
            AddressVertex v) {
        var reversed = new EdgeReversedGraph<AddressVertex, DefaultEdge>(graph);
        return findAllSuccessors(reversed, v);
    }

    public static ArrayList<AddressVertex> findAllSuccessors(Graph<AddressVertex, DefaultEdge> graph, AddressVertex v) {
        var iter = new BreadthFirstIterator<AddressVertex, DefaultEdge>(graph, v);
        var res = new ArrayList<AddressVertex>();

        while (iter.hasNext()) {
            res.add(iter.next());
        }
        return res;
    }

    public static List<AddressVertex> getVerticesWithType(Graph<AddressVertex, DefaultEdge> graph,
            AddressVertexType type) {
        var ret = new ArrayList<AddressVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getType() == type)
                ret.add(v);
        }
        return ret;
    }

    public static String pathBetweenTwoVertices(Graph<AddressVertex, DefaultEdge> graph, AddressVertex source,
            AddressVertex sink) {
        return pathBetweenTwoVertices(graph, source, sink, 0);
    }

    public static String pathBetweenTwoVertices(Graph<AddressVertex, DefaultEdge> graph, AddressVertex source,
            AddressVertex sink, int limit) {
        var dijkstra = new DijkstraShortestPath<AddressVertex, DefaultEdge>(graph);
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

    public static String generateGraphURL(Graph<AddressVertex, DefaultEdge> graph) {
        var str = GraphUtils.graphToDot(graph);
        return generateGraphURL(str);
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
}
