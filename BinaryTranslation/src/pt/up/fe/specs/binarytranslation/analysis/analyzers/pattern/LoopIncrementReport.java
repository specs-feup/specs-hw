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
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.BasicBlockDataFlowGraph;

public class LoopIncrementReport extends APatternReport {
    private List<Graph<BtfVertex, DefaultEdge>> doubleGraphs = new ArrayList<>();

    public List<Graph<BtfVertex, DefaultEdge>> getDoubleGraphs() {
        return graphs;
    }
    
    public void addEntry(BasicBlockDataFlowGraph g1, BasicBlockDataFlowGraph g2) {
        graphs.add(g1);
        doubleGraphs.add(g2);
        getBasicBlockIDs().add("BB" + lastID);
        incrementLastID();
    }
    
    @Override
    public String toString() {
        var sb = new StringBuilder();
        
        for (int i = 0; i < getBasicBlockIDs().size(); i++) {
            var g1 = getGraphs().get(i);
            var g2 = getDoubleGraphs().get(i);
            var bbid = getBasicBlockIDs().get(i);
            sb.append(this.name + "," + bbid + "," + g1 + "," + g2 + "\n");
        }
        return sb.toString();
    }
}
