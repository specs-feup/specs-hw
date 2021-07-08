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

package pt.up.fe.specs.binarytranslation.analysis.graphs.pseudocode;

import java.util.Map;

import org.jgrapht.Graphs;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;

public class PseudoInstructionEvaluator {
    private PseudoInstructionGraph graph;

    public PseudoInstructionEvaluator(PseudoInstructionGraph graph) {
        this.graph = graph;
    }
    
    public Number eval(Map<String, Number> inputMap) {
        var output = graph.getOutputs().get(0);
        return eval(inputMap, Graphs.predecessorListOf(graph, output).get(0));
    }
    
    private Number eval(Map<String, Number> inputMap, BtfVertex currV) {
        if (currV.getType() == BtfVertexType.OPERATION) {
            var op1 = Graphs.predecessorListOf(graph, currV).get(0);
            var op2 = Graphs.predecessorListOf(graph, currV).get(1);
            
            if (currV.getLabel().equals("+")) {
                return eval(inputMap, op1).longValue() + eval(inputMap, op2).longValue();
            }
            if (currV.getLabel().equals("-")) {
                return eval(inputMap, op1).longValue() - eval(inputMap, op2).longValue();
            }
            if (currV.getLabel().equals("*")) {
                return eval(inputMap, op1).longValue() * eval(inputMap, op2).longValue();
            }
        }
        if (currV.getType() == BtfVertexType.REGISTER) {
            return inputMap.get(currV.getLabel());
        }
        if (currV.getType() == BtfVertexType.IMMEDIATE) {
            return Integer.decode(currV.getLabel());
        }
        return 0;
    }
}
