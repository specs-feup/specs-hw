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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.ImmediateReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfElseStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.AInstructionCDFGDataNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGGeneratedVariable;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.ControlFlowIfElseNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.ControlFlowIfNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AControlFlowNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.ControlFlowNodeDecision;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

//http://digitaljs.tilk.eu/#

public class InstructionCDFGConverter {    
    
    @SuppressWarnings("unchecked")
    public static HardwareNode convertInstruction(HardwareNode parent, InstructionCDFG icdfg) {
       
        Set<GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>> nodes_being_resolved = new HashSet<>();

        icdfg.getInputs().forEach(g -> nodes_being_resolved.add(g));
        InstructionCDFGDataFlowSubgraph condition_dfg = null;
        while(!nodes_being_resolved.isEmpty()) {
            
            GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> gg = null;
            
            for(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> subgraph : nodes_being_resolved) {
                
                if(subgraph instanceof InstructionCDFGDataFlowSubgraph) {
                    
                    if(icdfg.isControlFlowConditionNode(subgraph)) {             
                        condition_dfg = (InstructionCDFGDataFlowSubgraph) subgraph;        
                    }else {
                        InstructionCDFGConverter.visitDataFlowGraph(parent, (InstructionCDFGDataFlowSubgraph) subgraph);
                    }
                    
                }else {
                    
                    if(subgraph instanceof ControlFlowNodeDecision) {         
                        InstructionCDFGConverter.visitControlFlowDecisionNode(parent, condition_dfg, (AControlFlowNode) subgraph);                     
                    }else {
                        
                    }
                    
                }

                gg = subgraph;
            }
            
            nodes_being_resolved.remove(gg);
            nodes_being_resolved.addAll((Collection<? extends GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>>) icdfg.getVerticesAfter(gg));
        }
        
        return parent;
    }
    
    public static boolean allOperandsResolved(InstructionCDFGDataFlowSubgraph dfg, AInstructionCDFGNode node, HashMap<AInstructionCDFGNode, HardwareExpression> signal_map) {
        
        for(AInstructionCDFGEdge e : dfg.incomingEdgesOf(node)) {
            if(!signal_map.containsKey(dfg.getEdgeSource(e))) {
                return false;
            }
         }
        
        return true;
    }
    
    public static AInstructionCDFGNode nextNodeisDataNode(InstructionCDFGDataFlowSubgraph dfg, AInstructionCDFGNode node) {
        
        for(AInstructionCDFGNode v : dfg.getVerticesAfter(node)) {
            if(v instanceof AInstructionCDFGDataNode) {
                return v;
            }
        }
        
        return null;
    }
    
    public static Collection<HardwareExpression> visitDataFlowGraph(HardwareNode prev, InstructionCDFGDataFlowSubgraph dfg) {

        HashMap<AInstructionCDFGNode, HardwareExpression> signal_map = new HashMap<>();
        
        Set<AInstructionCDFGNode> nodes_being_resolved = new HashSet<>();
        
        dfg.getInputs().forEach(i -> {           
            signal_map.put(i, (i instanceof InstructionCDFGLiteralNode) ?  new ImmediateReference(((InstructionCDFGLiteralNode)i).getValue(), 32) : new VariableReference(i.getUID()));
            nodes_being_resolved.addAll(dfg.getVerticesAfter(i));
        });

        dfg.getOutputs().forEach(o -> signal_map.put(o, new VariableReference(o.getUID())));
        
        while(!nodes_being_resolved.isEmpty()) {
            
        nodes_being_resolved.stream().filter(v -> InstructionCDFGConverter.allOperandsResolved(dfg, v, signal_map)).toList().forEach(v -> {

            nodes_being_resolved.remove(v); // Remove from list of vertices to be resolved
            
            VariableReference expr_var = (VariableReference) signal_map.getOrDefault(InstructionCDFGConverter.nextNodeisDataNode(dfg, v), new VariableReference(v.getUID()));
            signal_map.putIfAbsent(v, expr_var);
            
            List<HardwareExpression> signals = new ArrayList<>();
            
            dfg.getOperandsOf(v).forEach(op -> signals.add(signal_map.get(op)));
            
            HardwareExpression expr = HardwareNodeExpressionMap.generate(v.getClass(), signals);   // Generates a new hardware expression for the vertex

            prev.addChild(new ProceduralBlockingStatement(expr_var, expr));// Adds it to the dfg parent node

            dfg.getVerticesAfter(v).forEach(v_after -> {    // Gets the new vertices to be resolved
                
                if(!dfg.hasOutput(v_after)){
                    nodes_being_resolved.add(v_after);
                }else {
                    nodes_being_resolved.addAll(dfg.getVerticesAfter(v_after));
                }
            }); 

        });
        
        }

        return signal_map.values();
    }
    
    public static HardwareNode visitDataFlowGraph(InstructionCDFGDataFlowSubgraph dfg) {
        
        HashMap<AInstructionCDFGNode, HardwareExpression> signal_map = new HashMap<>();
        
        Set<AInstructionCDFGNode> nodes_being_resolved = new HashSet<>();
        
        HardwareNode output = null;
        
        dfg.getInputs().forEach(i -> {
            signal_map.put(i, (i instanceof InstructionCDFGLiteralNode) ? new ImmediateReference(((InstructionCDFGLiteralNode)i).getValue(), 32) : new VariableReference(i.getUID()));
            nodes_being_resolved.addAll(dfg.getVerticesAfter(i));
        });

        while(!nodes_being_resolved.isEmpty()) {
            
                for(AInstructionCDFGNode v : nodes_being_resolved.stream().filter(v -> InstructionCDFGConverter.allOperandsResolved(dfg, v, signal_map)).toList()) {

                nodes_being_resolved.remove(v); // Remove from list of vertices to be resolved
                
                List<HardwareExpression> signals = new ArrayList<>();
                
                dfg.getOperandsOf(v).forEach(op -> signals.add(signal_map.get(op)));

                signal_map.put(v, HardwareNodeExpressionMap.generate(v.getClass(), signals));// Generates a new hardware expression for the vertex

                for(AInstructionCDFGNode v_after : dfg.getVerticesAfter(v)) {
                    if(v_after instanceof InstructionCDFGGeneratedVariable) {
                        output = signal_map.get(v);
                    }
                }
                
            }
        }
        
        return output;
    }
    
    public static HardwareNode visitControlFlowDecisionNode(HardwareNode prev, InstructionCDFGDataFlowSubgraph condition_dfg, AControlFlowNode cfn) {
        
        HardwareStatement decision = null;
        
        if(cfn instanceof ControlFlowIfNode) {
            decision = new IfStatement((HardwareExpression) InstructionCDFGConverter.visitDataFlowGraph(condition_dfg));
        }else if (cfn instanceof ControlFlowIfElseNode) {
            decision = new IfElseStatement((HardwareExpression) InstructionCDFGConverter.visitDataFlowGraph(condition_dfg));
            InstructionCDFGConverter.visitDataFlowGraph((HardwareNode) ((IfElseStatement)decision).getIfStatements(), condition_dfg);
            InstructionCDFGConverter.visitDataFlowGraph((HardwareNode) ((IfElseStatement)decision).getElseStatements(), condition_dfg);
        }
        
        
        
        
        prev.addChild(decision);
        
        return null;
    }
 
    
    
}
