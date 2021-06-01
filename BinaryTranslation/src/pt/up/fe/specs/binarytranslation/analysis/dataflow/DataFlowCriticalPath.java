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

package pt.up.fe.specs.binarytranslation.analysis.dataflow;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;

public class DataFlowCriticalPath {
    private Graph<AddressVertex, DefaultEdge> graph;

    public DataFlowCriticalPath(Graph<AddressVertex, DefaultEdge> graph) {
        this.graph = graph;
    }

    public Graph<AddressVertex, DefaultEdge> calculatePath() {
        var sources = findSources();
        var sinks = findSinks();
        List<AddressVertex> currPath = new ArrayList<AddressVertex>();

        for (var source : sources) {
            for (var sink : sinks) {
                var dijkstra = new DijkstraShortestPath<AddressVertex, DefaultEdge>(graph);
                var path = dijkstra.getPath(source, sink);
                if (path == null)
                    continue;

                var pathList = path.getVertexList();
                if (pathSize(pathList) > pathSize(currPath))
                    currPath = pathList;
            }
        }
        return pathToGraph(currPath);
    }

    private Graph<AddressVertex, DefaultEdge> pathToGraph(List<AddressVertex> currPath) {
        Graph<AddressVertex, DefaultEdge> path = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (var v : currPath)
            path.addVertex(v);
        for (int i = 1; i < currPath.size(); i++)
            path.addEdge(currPath.get(i - 1), currPath.get(i));

        return path;
    }

    private int pathSize(List<AddressVertex> pathList) {
        var cnt = 0;
        
        for (var v : pathList) {
            if (v.getType() == AddressVertexType.OPERATION)
                cnt++;
        }
        return cnt;
    }

    public List<AddressVertex> findSinks() {
        var sinks = new ArrayList<AddressVertex>();

        for (var v : graph.vertexSet()) {
            if (graph.inDegreeOf(v) != 0 && graph.outDegreeOf(v) == 0)
                sinks.add(v);
        }
        return sinks;
    }

    public List<AddressVertex> findSources() {
        var sources = new ArrayList<AddressVertex>();

        for (var v : graph.vertexSet()) {
            if (graph.inDegreeOf(v) == 0 && graph.outDegreeOf(v) != 0)
                sources.add(v);
        }
        return sources;
    }
}
