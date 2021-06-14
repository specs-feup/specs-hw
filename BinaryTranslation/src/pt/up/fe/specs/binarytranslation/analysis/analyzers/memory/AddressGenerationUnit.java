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

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformShiftsToMult;

public class AddressGenerationUnit {
    private Graph<BtfVertex, DefaultEdge> graph;
    private List<String> inputs;
    private Map<String, Integer> strides;
    private Map<String, Integer> currIndexes;

    public AddressGenerationUnit(Graph<BtfVertex, DefaultEdge> graph, Map<String, Integer> strides) {
        this.graph = GraphUtils.getExpressionGraph(graph);
        var t1 = new TransformHexToDecimal(this.graph);
        t1.applyToGraph();
        var t2 = new TransformShiftsToMult(this.graph);
        t2.applyToGraph();
        var t3 = new TransformRemoveTemporaryVertices(this.graph);
        t3.applyToGraph();
        
        this.strides = strides;
        this.inputs = findAllInputs();
        this.currIndexes = new HashMap<>();
        for (var key : strides.keySet()) {
            currIndexes.put(key, 0);
        }
    }

    private List<String> findAllInputs() {
        var regs = new ArrayList<String>();
        for (var reg : GraphUtils.findAllNodesOfType(graph, BtfVertexType.REGISTER)) {
            if (!strides.keySet().contains(reg.getLabel()))
                regs.add(reg.getLabel());
        }
        return regs;
    }

    public Graph<BtfVertex, DefaultEdge> getGraph() {
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
        var start = GraphUtils.findGraphRoot(graph);
        return next(inputMap, start);
    }
    
    private long next(Map<String, Integer> inputMap, BtfVertex currVertex) {
        if (currVertex == BtfVertex.nullVertex)
            return Long.MAX_VALUE;

        var parents = GraphUtils.getParents(graph, currVertex);
        var left = parents.size() > 0 ? parents.get(0) : BtfVertex.nullVertex;
        long leftVal = next(inputMap, left);
        var right = parents.size() > 1 ? parents.get(1) : BtfVertex.nullVertex;
        long rightVal = next(inputMap, right);
        
        if (leftVal == Long.MAX_VALUE)
            leftVal = rightVal;
        if (rightVal == Long.MAX_VALUE)
            rightVal = leftVal;
        
        if (currVertex.getType() == BtfVertexType.IMMEDIATE) {
            return Long.valueOf(currVertex.getLabel());
        }
        if (currVertex.getType() == BtfVertexType.OPERATION) {
            return doOperation(leftVal, rightVal, currVertex.getLabel());
        }
        if (currVertex.getType() == BtfVertexType.REGISTER) {
            var reg = currVertex.getLabel();
            if (strides.containsKey(reg)) {
                var stride = strides.get(reg);
                var curr = currIndexes.get(reg);
                currIndexes.put(reg, curr + stride);
                return curr;
            }
            else if (inputMap.containsKey(reg)) {
                return inputMap.get(reg);
            }
        }
        return Long.MAX_VALUE;
    }

    private long doOperation(long leftVal, long rightVal, String op) {
        if (op.equals("*")) {
            return leftVal * rightVal;
        }
        if (op.equals("-")) {
            return leftVal - rightVal;
        }
        if (op.equals("+")) {
            return leftVal + rightVal;
        }
        return Long.MAX_VALUE;
    }
}
