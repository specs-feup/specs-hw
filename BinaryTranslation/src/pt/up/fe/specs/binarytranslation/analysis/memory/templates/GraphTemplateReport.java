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

package pt.up.fe.specs.binarytranslation.analysis.memory.templates;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;

public class GraphTemplateReport {
    private List<String> graphs;
    private List<String> ids;
    private List<GraphTemplateType> types;
    private List<Integer> occurrences;
    private String name;

    public GraphTemplateReport(String kernelName) {
        name = "\"" + kernelName + "\"";
        graphs = new ArrayList<>();
        ids = new ArrayList<>();
        types = new ArrayList<>();
        occurrences = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addEntry(String graph, String id, GraphTemplateType type, int occurrence) {
        graphs.add(graph);
        ids.add(id);
        types.add(type);
        occurrences.add(occurrence);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Graph types report for ").append(name).append(":\n");
        for (var g : graphs) {
            sb.append(g).append("\n");
        }
        sb.append("\n");
        
        for (int i = 0; i < graphs.size(); i++) {
            
            sb.append("name,").append(ids.get(i)).append(",").append(types.get(i))
                    .append(",").append(occurrences.get(i))
                    .append("\n");
        }
        return sb.toString();
    }
}
