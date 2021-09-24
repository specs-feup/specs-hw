/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.analysis.analyzers.scheduling;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;

public class PriorityCalculator {
    private Graph<BtfVertex, DefaultEdge> graph;
    private int longestLatency = 0;

    public PriorityCalculator(Graph<BtfVertex, DefaultEdge> graph) {
        this.graph = graph;
    }
    
    public int calculatePriority(BtfVertex v) {
        dfs(v, 0);
        var res = longestLatency;
        longestLatency = 0;
        return res;
    }
    
    public void dfs(BtfVertex v, int p) {
        if (graph.outDegreeOf(v) == 0) {
            p += v.getLatency();
            if (p > longestLatency) {
                longestLatency = p;
            }
        }
        else {
            p += v.getLatency();
            
            for (var u : Graphs.successorListOf(graph, v)) {
                dfs(u, p);
            }
        }
    }
   
}
