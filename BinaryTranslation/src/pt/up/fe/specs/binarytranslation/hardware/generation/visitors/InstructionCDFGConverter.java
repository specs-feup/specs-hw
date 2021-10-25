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

package pt.up.fe.specs.binarytranslation.hardware.generation.visitors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareNodeExpressionMap;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.controlanddataflowgraph.ControlFlowNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGControlFlowConditionSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

//http://digitaljs.tilk.eu/#

public class InstructionCDFGConverter {    
    
    @SuppressWarnings("unchecked")
    public static HardwareNode convertInstruction(HardwareNode parent, InstructionCDFG icdfg) {
       
        Set<GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>> nodes_being_resolved = new HashSet<>();
        
        icdfg.getInputs().forEach(g -> {
            icdfg.getVerticesAfter(g).forEach(g_after -> {
                nodes_being_resolved.add(g_after);
            });
        });
        
        while(!nodes_being_resolved.isEmpty()) {
            
        }
        
        return parent;
    }
    
    public static boolean allOperandsResolved(InstructionCDFGDataFlowSubgraph dfg, AInstructionCDFGNode node, HashMap<AInstructionCDFGNode, VariableReference> signal_map) {
        
        for(AInstructionCDFGEdge e : dfg.incomingEdgesOf(node)) {
            if(!signal_map.containsKey(dfg.getEdgeSource(e))) {
                return false;
            }
         }
        
        return true;
    }
    
    public static Collection<VariableReference> visitDataFlowGraph(HardwareNode prev, InstructionCDFGDataFlowSubgraph dfg) {

        HashMap<AInstructionCDFGNode, VariableReference> signal_map = new HashMap<>();
        
        List<AInstructionCDFGNode> nodes_being_resolved = new ArrayList<>();
        
        dfg.getInputs().forEach(i -> {
            signal_map.put(i, new VariableReference(i.getUID()));
            
            dfg.getVerticesAfter(i).forEach(v -> {
               
                if(!nodes_being_resolved.contains(v)) {
                    nodes_being_resolved.add(v);
                }
                
            });
            
        });

        dfg.getOutputs().forEach(o -> signal_map.put(o, new VariableReference(o.getUID())));
        
        
        while(!nodes_being_resolved.isEmpty()) {

        nodes_being_resolved.stream().filter(v -> InstructionCDFGConverter.allOperandsResolved(dfg, v, signal_map)).toList().forEach(v -> {

            nodes_being_resolved.remove(v); // Remove from list of vertices to be resolved
            
            
            VariableReference expr_var =  new VariableReference(v.getUID()); // Creates a new VariableReference for the output of the vertex

            signal_map.put(v,expr_var); // Adds the variable reference of the vertex

            HardwareExpression expr = HardwareNodeExpressionMap.generate(v.getClass());   // Generates a new hardware expression for the vertex
            
            
            
            
            prev.addChild(new ProceduralBlockingStatement(expr_var, expr));// Adds it to the dfg parent node
            
            dfg.getOperandsOf(v).forEach(o -> {expr.addChild(signal_map.get(o));}); // Adds the operands of the new hardware expression
            
            dfg.getVerticesAfter(v).forEach(v_after -> {    // Gets the new vertices to be resolved
                
                if(!dfg.hasOutput(v_after)){
                
                    if(!nodes_being_resolved.contains(v_after)) {
                        nodes_being_resolved.add(v_after);
                    }
                }else {

                    dfg.outgoingEdgesOf(v_after).forEach(g -> {
                        if(!nodes_being_resolved.contains(dfg.getEdgeTarget(g))) {
                            nodes_being_resolved.add(dfg.getEdgeTarget(g));
                        }
                    });
                   
                }
            });

        });
        
        }

        return signal_map.values();
    }
    
    public HardwareNode visitControlFlowDecisionNode(HardwareNode prev,InstructionCDFGControlFlowConditionSubgraph condition_dfg, ControlFlowNode<AInstructionCDFGNode, AInstructionCDFGEdge> cfn) {
        return null;
    }
 
    
    
}
