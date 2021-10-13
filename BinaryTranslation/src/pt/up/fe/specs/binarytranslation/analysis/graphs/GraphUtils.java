/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package pt.up.fe.specs.binarytranslation.analysis.graphs;

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

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexIsaInfo;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Class with static methods to manipulate memory address graphs
 * 
 * @author Tiago
 *
 */
public class GraphUtils {

    public static Graph<BtfVertex, DefaultEdge> mergeGraphs(List<Graph<BtfVertex, DefaultEdge>> graphs) {
        Graph<BtfVertex, DefaultEdge> merged = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (var graph : graphs) {
            Graphs.addGraph(merged, graph);
        }
        return merged;
    }

    public static BtfVertex findGraphRoot(Graph<BtfVertex, DefaultEdge> graph) {
        var root = BtfVertex.nullVertex;
        for (var v : graph.vertexSet()) {
            if (graph.outDegreeOf(v) == 0)
                root = v;
        }
        return root;
    }

    public static ArrayList<BtfVertex> getParents(Graph<BtfVertex, DefaultEdge> graph, BtfVertex current) {
        var res = new ArrayList<BtfVertex>();
        for (var edge : graph.edgesOf(current)) {
            if (graph.getEdgeSource(edge) != current)
                res.add(graph.getEdgeSource(edge));
        }
        return res;
    }

    public static BtfVertex getBaseAddr(Graph<BtfVertex, DefaultEdge> graph) {
        for (var v : graph.vertexSet()) {
            if (v.getProperty() == BtfVertexProperty.BASE_ADDR)
                return v;
        }
        return BtfVertex.nullVertex;
    }

    public static BtfVertex getOffset(Graph<BtfVertex, DefaultEdge> graph) {
        for (var v : graph.vertexSet()) {
            if (v.getProperty() == BtfVertexProperty.OFFSET)
                return v;
        }
        return BtfVertex.nullVertex;
    }

    public static BtfVertex getMemoryOp(Graph<BtfVertex, DefaultEdge> graph) {
        for (var v : graph.vertexSet()) {
            if (v.getType() == BtfVertexType.MEMORY)
                return v;
        }
        return BtfVertex.nullVertex;
    }

    public static BtfVertex getExpressionStart(Graph<BtfVertex, DefaultEdge> graph) {
        BtfVertex start = null;
        for (var v : graph.vertexSet()) {
            if (v.getType() == BtfVertexType.MEMORY)
                start = v;
        }
        for (var v : getParents(graph, start)) {
            if (v.getIsaInfo() != BtfVertexIsaInfo.RD)
                return v;
        }
        return BtfVertex.nullVertex;
    }

    public static Graph<BtfVertex, DefaultEdge> getExpressionGraph(Graph<BtfVertex, DefaultEdge> graph) {
        Graph<BtfVertex, DefaultEdge> sub = new DefaultDirectedGraph<>(DefaultEdge.class);
        Graphs.addGraph(sub, graph);
        var mem = findAllNodesOfType(sub, BtfVertexType.MEMORY);
        var rd = findAllNodesWithIsaInfo(graph, BtfVertexIsaInfo.RD);
        sub.removeAllVertices(mem);
        sub.removeAllVertices(rd);
        return sub;
    }

    public static String graphToDot(Graph<BtfVertex, DefaultEdge> graph, String title) {
        DOTExporter<BtfVertex, DefaultEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();

            String label = v.getLabel();
            if (v.getIsaInfo() != BtfVertexIsaInfo.NULL)
                label += "\n{" + v.getIsaInfo() + "}";
            if (v.getProperty() != BtfVertexProperty.NULL)
                label += "\n{" + v.getProperty() + "}";

            String extraInfo = "";
//            if (v.getLoadStoreOrder() != -1)
//                extraInfo = " \\[" + v.getLoadStoreOrder() + "\\]";
            extraInfo = " \\[" + v.getPriority() + "\\]";
            
            map.put("label", DefaultAttribute.createAttribute(label + extraInfo));
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

    public static String graphToDot(Graph<BtfVertex, DefaultEdge> graph) {
        return graphToDot(graph, "\"Graph\"");
    }

    public static ArrayList<BtfVertex> findAllNodesOfType(Graph<BtfVertex, DefaultEdge> graph,
            BtfVertexType type) {
        var res = new ArrayList<BtfVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getType() == type)
                res.add(v);
        }
        return res;
    }

    public static ArrayList<BtfVertex> findAllNodesWithProperty(Graph<BtfVertex, DefaultEdge> graph,
            BtfVertexProperty property) {
        var res = new ArrayList<BtfVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getProperty() == property)
                res.add(v);
        }
        return res;
    }

    public static ArrayList<BtfVertex> findAllNodesWithIsaInfo(Graph<BtfVertex, DefaultEdge> graph,
            BtfVertexIsaInfo isaInfo) {
        var res = new ArrayList<BtfVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getIsaInfo() == isaInfo)
                res.add(v);
        }
        return res;
    }

    public static ArrayList<BtfVertex> findAllPredecessors(Graph<BtfVertex, DefaultEdge> graph,
            BtfVertex v) {
        var reversed = new EdgeReversedGraph<BtfVertex, DefaultEdge>(graph);
        return findAllSuccessors(reversed, v);
    }

    public static ArrayList<BtfVertex> findAllSuccessors(Graph<BtfVertex, DefaultEdge> graph, BtfVertex v) {
        var iter = new BreadthFirstIterator<BtfVertex, DefaultEdge>(graph, v);
        var res = new ArrayList<BtfVertex>();

        while (iter.hasNext()) {
            res.add(iter.next());
        }
        return res;
    }

    public static List<BtfVertex> getVerticesWithType(Graph<BtfVertex, DefaultEdge> graph,
            BtfVertexType type) {
        var ret = new ArrayList<BtfVertex>();
        for (var v : graph.vertexSet()) {
            if (v.getType() == type)
                ret.add(v);
        }
        return ret;
    }

    public static String pathBetweenTwoVertices(Graph<BtfVertex, DefaultEdge> graph, BtfVertex source,
            BtfVertex sink) {
        return pathBetweenTwoVertices(graph, source, sink, 0);
    }

    public static String pathBetweenTwoVertices(Graph<BtfVertex, DefaultEdge> graph, BtfVertex source,
            BtfVertex sink, int limit) {
        var dijkstra = new DijkstraShortestPath<BtfVertex, DefaultEdge>(graph);
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

    public static String generateGraphURL(Graph<BtfVertex, DefaultEdge> graph) {
        return GraphUtils.generateGraphURL(graph, "\"Graph\"");
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

    public static String generateGraphURL(Graph<BtfVertex, DefaultEdge> graph, String name) {
        var str = GraphUtils.graphToDot(graph, name);
        return generateGraphURL(str);
    }
}
