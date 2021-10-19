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

public class MemoryPatternReport extends APatternReport {
    private List<String> ids;
    private List<GraphTemplateType> types;
    private List<Integer> occurrences;
    private String segmentID = "?";
    private static int lastID = 1;


    public MemoryPatternReport(String kernelName) {
        name = "\"" + kernelName + "\"";
        graphs = new ArrayList<>();
        ids = new ArrayList<>();
        types = new ArrayList<>();
        occurrences = new ArrayList<>();
        setBasicBlockIDs(new ArrayList<>());
    }

    public void addEntry(Graph<BtfVertex, DefaultEdge> graph, String id, GraphTemplateType type, int occurrence) {
        graphs.add(graph);
        ids.add(id);
        types.add(type);
        occurrences.add(occurrence);
        getBasicBlockIDs().add("BB" + lastID);
    }
    
    @Override
    public String toString() {
        var sb = new StringBuilder();
        
        for (int i = 0; i < graphs.size(); i++) {
            
            var typeStr = types.get(i).toString();
            typeStr = typeStr.replace("TYPE_", "");
            sb.append(name).append(",").append(getBasicBlockIDs().get(i)).append(",").append(ids.get(i)).append(",").append(typeStr)
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
    
    @Override
    public int getLastID() {
        return lastID;
    }
    
    @Override
    public void resetLastID() {
        lastID = 1;
    }

    @Override
    public void incrementLastID() {
        lastID++;
    }
}
