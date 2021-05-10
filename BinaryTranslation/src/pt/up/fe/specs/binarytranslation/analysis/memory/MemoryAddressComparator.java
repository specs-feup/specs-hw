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

package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class MemoryAddressComparator {

    private Map<String, List<String>> prologueDeps;
    private ArrayList<Graph<AddressVertex, DefaultEdge>> graphs;
    private HashMap<String, Integer> indVars;

    public MemoryAddressComparator(Map<String, List<String>> prologueDeps, ArrayList<Graph<AddressVertex, DefaultEdge>> graphs, HashMap<String, Integer> indVars) {
        this.prologueDeps = prologueDeps;
        this.graphs = graphs;
        this.indVars = indVars;
    }
    
    public boolean compare(Graph<AddressVertex, DefaultEdge> load, Graph<AddressVertex, DefaultEdge> store) {
        Graph<AddressVertex, DefaultEdge> shortest, longest;
        if (load.vertexSet().size() > store.vertexSet().size()) {
            shortest = store;
            longest = load;
        }
        else {
            shortest = load;
            longest = store;
        }
        
        var reducedLong = treeSubtract(longest, shortest);
        var reducedShort = treeSubtract(longest, shortest);
        
        resetGraph(load);
        resetGraph(store);
        return false;
    }
    

    private Graph<AddressVertex, DefaultEdge> treeSubtract(Graph<AddressVertex, DefaultEdge> main,
            Graph<AddressVertex, DefaultEdge> sub) {
        var mainStart = GraphUtils.getExpressionStart(main);
        var subStart = GraphUtils.getExpressionStart(sub);
        
        //Create main-clone
        Graph<AddressVertex, DefaultEdge> mainClone = new DefaultDirectedGraph<>(DefaultEdge.class);
        Graphs.addGraph(mainClone, main);
        
        //Go through each node of sub, and remove from main-clone
        //Use post-order traversal!!!
        postOrderTraversal(mainClone, sub, subStart);
        
        return mainClone;
    }
    
    private void postOrderTraversal(Graph<AddressVertex, DefaultEdge> mainClone,
            Graph<AddressVertex, DefaultEdge> sub, AddressVertex currVertex) {
        if (currVertex == AddressVertex.nullVertex)
            return;
        
        var parents = GraphUtils.getParents(sub, currVertex);
        var left = parents.size() > 0 ? parents.get(0) : AddressVertex.nullVertex;
        postOrderTraversal(mainClone, sub, left);
        var right = parents.size() > 1 ? parents.get(1) : AddressVertex.nullVertex;
        postOrderTraversal(mainClone, sub, right);
        
        //Do the comparison to mainClone, and remove if it exists
        //...
        //print for now
        System.out.println(currVertex.toString());
    }

    private void resetGraph(Graph<AddressVertex, DefaultEdge> g) {
        for (var v : g.vertexSet())
            v.setKeep(false);
    }
}
