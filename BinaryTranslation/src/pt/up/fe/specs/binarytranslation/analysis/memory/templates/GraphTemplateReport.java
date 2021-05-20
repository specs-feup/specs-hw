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

public class GraphTemplateReport {
    private List<String> graphs;
    private List<GraphTemplateType> types;
    private List<Integer> occurrences;
    private String name;

    public GraphTemplateReport(String kernelName) {
        name = kernelName;
        graphs = new ArrayList<>();
        types = new ArrayList<>();
        occurrences = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addEntry(String graph, GraphTemplateType type, int occurrence) {
        graphs.add(graph);
        types.add(type);
        occurrences.add(occurrence);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Graph types report for ").append(name).append(":\n"); 
        
        for (int i = 0; i < graphs.size(); i++) {
            sb.append(types.get(i)).append(" detected ").append(occurrences.get(i)).append(" times, graph = ")
                    .append(graphs.get(i)).append("\n");
        }
        return sb.toString();
    }
}
