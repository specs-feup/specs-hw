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

package pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;

public class DataFlowCriticalPath {
    private ASegmentDataFlowGraph graph;

    public DataFlowCriticalPath(ASegmentDataFlowGraph graph) {
        this.graph = graph;
    }

    public Graph<BtfVertex, DefaultEdge> calculatePath() {
        var sources = findSources();
        var sinks = findSinks();
        List<BtfVertex> bestPath = new ArrayList<BtfVertex>();

        for (var source : sources) {
            for (var sink : sinks) {                
                var alg = new AllDirectedPaths<BtfVertex, DefaultEdge>(graph);
                for (var path : alg.getAllPaths(source, sink, true, null)) {
                    var currPath = path.getVertexList();
                    if (pathSize(currPath) > pathSize(bestPath))
                        bestPath = currPath;
                }
            }
        }
        return pathToGraph(bestPath);
    }

    private Graph<BtfVertex, DefaultEdge> pathToGraph(List<BtfVertex> currPath) {
        Graph<BtfVertex, DefaultEdge> path = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (var v : currPath)
            path.addVertex(v);
        for (int i = 1; i < currPath.size(); i++)
            path.addEdge(currPath.get(i - 1), currPath.get(i));

        return path;
    }

    private int pathSize(List<BtfVertex> pathList) {
        var cnt = 0;
        
        for (var v : pathList) {
            if (v.getType() == BtfVertexType.OPERATION)
                cnt++;
        }
        return cnt;
    }

    public List<BtfVertex> findSinks() {
        var sinks = new ArrayList<BtfVertex>();

        for (var v : graph.vertexSet()) {
            if (graph.inDegreeOf(v) != 0 && graph.outDegreeOf(v) == 0)
                sinks.add(v);
        }
        return sinks;
    }

    public List<BtfVertex> findSources() {
        var sources = new ArrayList<BtfVertex>();

        for (var v : graph.vertexSet()) {
            if (graph.inDegreeOf(v) == 0 && graph.outDegreeOf(v) != 0)
                sources.add(v);
        }
        return sources;
    }
}
