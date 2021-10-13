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
 
package pt.up.fe.specs.binarytranslation.analysis.analyzers.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.address.AddressGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class MemoryAddressDetector {
    private List<Instruction> basicBlock;

    public MemoryAddressDetector(BinarySegment bb) {
        this.basicBlock = bb.getInstructions();
    }
    
    public MemoryAddressDetector(List<Instruction> bb) {
        this.basicBlock = bb;
    }

    public ArrayList<Graph<BtfVertex, DefaultEdge>> detectGraphs() {
        var out = new ArrayList<Graph<BtfVertex, DefaultEdge>>();

        for (var i : basicBlock) {
            if (i.isMemory()) {
                var addrGraph = new AddressGraph(basicBlock, i);
                out.add(addrGraph);
            }
        }
        return out;
    }
    
    public static String buildMemoryExpression(Graph<BtfVertex, DefaultEdge> graph) {
        var root = GraphUtils.findGraphRoot(graph);
        return buildMemoryExpression(graph, root);
    }

    public static String buildMemoryExpression(Graph<BtfVertex, DefaultEdge> graph, BtfVertex root) {
        var sb = new StringBuilder();
        
        if (root.getType() == BtfVertexType.LOAD_TARGET) {
            sb.append(root.getLabel()).append(" <- mem[");

            var start = GraphUtils.getParents(graph, GraphUtils.getParents(graph, root).get(0)).get(0);

            sb.append(buildAddressExpression(graph, start, true));
            sb.append("]");
        }
        if (root.getType() == BtfVertexType.MEMORY) {
            sb.append("mem[");

            var addrStart = BtfVertex.nullVertex;
            var dataToStore = BtfVertex.nullVertex;
            var parents = GraphUtils.getParents(graph, root);

            for (var parent : parents) {
                switch (parent.getType()) {
                case OPERATION:
                    addrStart = parent;
                    break;
                case IMMEDIATE:
                case REGISTER:
                    dataToStore = parent;
                    break;
                default:
                    break;
                }
            }

            sb.append(buildAddressExpression(graph, addrStart, true));
            sb.append("] <- ").append(dataToStore.getLabel());
        }
        return sb.toString();
    }
    
    public static String buildAddressFunction(Graph<BtfVertex, DefaultEdge> graph, List<String> args, HashMap<String, Integer> indVars) {
        var root = GraphUtils.findGraphRoot(graph);
        var start = GraphUtils.getParents(graph, GraphUtils.getParents(graph, root).get(0)).get(0);
        var sb = new StringBuilder("f(");
        sb.append(String.join(", ", args));
        sb.append(") = ");
        sb.append(buildAddressExpression(graph, start, true));
        
        List<String> argsIndVars = new ArrayList<>();
        for (var arg : args) {
            if (indVars.containsKey(arg))
                argsIndVars.add(arg);
        }
        if (argsIndVars.size() > 0) {
            sb.append("    (").append(String.join(", ", argsIndVars));
            sb.append(argsIndVars.size() == 1 ? " is an induction var with increment " : " are induction vars with increment");
            List<String> strides = new ArrayList<>();
            for (var arg : argsIndVars) {
                strides.add(indVars.get(arg) + "");
            }
            sb.append(String.join(", ", strides)).append(")");
        }
        
        return sb.toString();
    }

    public static String buildAddressExpression(Graph<BtfVertex, DefaultEdge> graph, BtfVertex current, boolean first) {

        if (current.getType() == BtfVertexType.IMMEDIATE) {
            return current.getLabel();
        }
        if (current.getType() == BtfVertexType.REGISTER) {
            var parents = GraphUtils.getParents(graph, current);
            if (parents.size() == 0)
                return current.getLabel();
            else
                return buildAddressExpression(graph, parents.get(0), false);
        }
        if (current.getType() == BtfVertexType.OPERATION || current.getType() == BtfVertexType.CHECK) {
            var parents = GraphUtils.getParents(graph, current);
            String s1 = "";
            String s2 = "";
            String s3 = "";
            
            if (parents.size() == 2) {
                var op1 = parents.get(0);
                var op2 = parents.get(1);
                
                s1 = buildAddressExpression(graph, op1, false);
                s2 = current.getLabel();
                s3 = buildAddressExpression(graph, op2, false);
            }
            if (parents.size() == 1) {
                var op1 = parents.get(0);
                s1 = buildAddressExpression(graph, op1, false); 
                
                s2 = "?";
                if (current.getLabel().equals("+")) {
                    s2 = "*";
                }
                if (current.getLabel().equals("*")) {
                    s2 = "^";
                }
                s3 = "2";
            }
            
            if (first)
                return s1 + " " + s2 + " " + s3;
            else
                return "(" + s1 + " " + s2 + " " + s3 + ")";
        }
        return "";
    }
}
