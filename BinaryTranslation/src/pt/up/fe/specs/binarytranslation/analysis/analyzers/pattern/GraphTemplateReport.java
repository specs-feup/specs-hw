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

package pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.templates.GraphTemplateType;

public class GraphTemplateReport {
    private List<Graph<BtfVertex, DefaultEdge>> graphs;
    private List<String> ids;
    private List<GraphTemplateType> types;
    private List<Integer> occurrences;
    private List<String> basicBlockIDs;
    private String name;
    private String segmentID = "?";
    private static int lastID = 1;

    public GraphTemplateReport(String kernelName) {
        name = "\"" + kernelName + "\"";
        graphs = new ArrayList<>();
        ids = new ArrayList<>();
        types = new ArrayList<>();
        occurrences = new ArrayList<>();
        basicBlockIDs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void addEntry(Graph<BtfVertex, DefaultEdge> graph, String id, GraphTemplateType type, int occurrence) {
        graphs.add(graph);
        ids.add(id);
        types.add(type);
        occurrences.add(occurrence);
        basicBlockIDs.add("BB" + lastID);
    }
    
    @Override
    public String toString() {
        var sb = new StringBuilder();
        
        for (int i = 0; i < graphs.size(); i++) {
            
            var typeStr = types.get(i).toString();
            typeStr = typeStr.replace("TYPE_", "");
            sb.append(name).append(",").append(basicBlockIDs.get(i)).append(",").append(ids.get(i)).append(",").append(typeStr)
                    .append(",").append(occurrences.get(i)).append(",").append(GraphUtils.generateGraphURL(graphs.get(i)))
                    .append("\n");
        }
        return sb.toString();
    }
    
    public String getCompositeGraph() {
        var composite = new DefaultDirectedGraph<BtfVertex, DefaultEdge>(DefaultEdge.class);
        for (var g : graphs)
            Graphs.addGraph(composite, g);
        return GraphUtils.generateGraphURL(composite, name + "-" + segmentID);
    }

    public List<Graph<BtfVertex, DefaultEdge>> getGraphs() {
        return graphs;
    }

    public static int getLastID() {
        return lastID;
    }
    
    public static void setLastID(int id) {
        lastID = id;
    }

    public static void incrementLastID() {
        lastID++;
    }
}
