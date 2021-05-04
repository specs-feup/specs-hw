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
        return false;
    }
}
