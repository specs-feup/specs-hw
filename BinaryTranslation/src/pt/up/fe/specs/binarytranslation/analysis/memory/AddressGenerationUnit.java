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

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformShiftsToMult;

public class AddressGenerationUnit {
    private Graph<AddressVertex, DefaultEdge> graph;
    private List<String> inputs;
    private Map<String, Integer> strides;
    private Map<String, Integer> currIndexes;

    public AddressGenerationUnit(Graph<AddressVertex, DefaultEdge> graph, Map<String, Integer> strides) {
        this.graph = GraphUtils.getExpressionGraph(graph);
        var t1 = new TransformHexToDecimal(this.graph);
        t1.applyToGraph();
        var t2 = new TransformShiftsToMult(this.graph);
        t2.applyToGraph();
        var t3 = new TransformRemoveTemporaryVertices(this.graph);
        t3.applyToGraph();
        
        this.inputs = findAllInputs();
        this.strides = strides;
        this.currIndexes = new HashMap<>();
        for (var key : strides.keySet()) {
            currIndexes.put(key, 0);
        }
    }

    private List<String> findAllInputs() {
        var regs = new ArrayList<String>();
        for (var reg : GraphUtils.findAllNodesOfType(graph, AddressVertexType.REGISTER)) {
            if (!strides.keySet().contains(reg.getLabel()))
                regs.add(reg.getLabel());
        }
        return regs;
    }

    public Graph<AddressVertex, DefaultEdge> getGraph() {
        return graph;
    }
    
    public Map<String, Integer> getInputMap() {
        var map = new HashMap<String, Integer>();
        for (var key : inputs) {
            map.put(key, 0);
        }
        return map;
    }
    
    public long next(Map<String, Integer> inputMap) {
        return 0;
    }
}
