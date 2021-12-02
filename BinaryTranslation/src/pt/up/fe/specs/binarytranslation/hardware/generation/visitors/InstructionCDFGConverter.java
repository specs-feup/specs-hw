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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareNodeExpressionMap;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.ImmediateOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.RangedSelection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.IdentifierReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfElseStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.AInstructionCDFGSubscriptModifier;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript.InstructionCDFGRangeSubscript;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFGConverter {

    public static void convert(InstructionCDFG icdfg, HardwareNode parent) {
        icdfg.getInputs().forEach(i -> InstructionCDFGConverter.visit(icdfg, parent, i));
    }
    
    public static boolean allOperandsResolved(AInstructionCDFGSubgraph dfg, AInstructionCDFGNode node, Map<AInstructionCDFGNode, HardwareExpression> signal_map) {
        return !dfg.incomingEdgesOf(node).stream().anyMatch(e -> !signal_map.containsKey(dfg.getEdgeSource(e)));
    }
    
    public static AInstructionCDFGNode nextNodeisDataNode(AInstructionCDFGSubgraph dfg, AInstructionCDFGNode node) {       
        return dfg.getVerticesAfter(node).stream().findFirst().orElse(null);
    }
    
    public static void visit(InstructionCDFG icdfg, HardwareNode parent, GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> node) {
        
        if(node instanceof InstructionCDFGControlFlowIf) {
            InstructionCDFGConverter.visitControlFlowIfNode(icdfg, parent, (InstructionCDFGControlFlowIf) node);
        }else if(node instanceof InstructionCDFGControlFlowIfElse) {
            InstructionCDFGConverter.visitControlFlowIfElseNode(icdfg, parent, (InstructionCDFGControlFlowIfElse) node);   
        }else if(node instanceof InstructionCDFGDataFlowSubgraph) {
            InstructionCDFGConverter.visitDataFlowSubgraph(icdfg, parent, (InstructionCDFGDataFlowSubgraph) node);
        }else if(node instanceof InstructionCDFGControlFlowMerge) {
        }

    }
    
    public static Map<AInstructionCDFGNode, HardwareExpression> generateSignalMap(AInstructionCDFGSubgraph dfg){
        
        Map<AInstructionCDFGNode, HardwareExpression> signal_map = new HashMap<>();
        
        dfg.getInputs().forEach(i -> signal_map.put(i, (i instanceof InstructionCDFGLiteralNode) ?  new ImmediateOperator(((InstructionCDFGLiteralNode)i).getValue(), 32) : new IdentifierReference(i.getUID())));
        dfg.getOutputs().forEach(o -> signal_map.put(o, new IdentifierReference(o.getUID())));
        
        return signal_map;
    }
    
    public static Set<AInstructionCDFGNode> getValidCandidates(AInstructionCDFGSubgraph dfg, Set<AInstructionCDFGNode> candidates, Map<AInstructionCDFGNode, HardwareExpression> signalMap){
        
        Set<AInstructionCDFGNode> valid_candidates =  candidates.stream().filter(v -> InstructionCDFGConverter.allOperandsResolved(dfg, v, signalMap)).collect(Collectors.toSet());
        
        valid_candidates.forEach(v -> candidates.remove(v));
        
        return valid_candidates;
    }
    
    public static Set<AInstructionCDFGNode> getInitialCandidates(AInstructionCDFGSubgraph dfg){
        return dfg.getVerticesAfter(dfg.getInputs());
    }
    
    public static void getNextCandidates(AInstructionCDFGSubgraph dfg, Set<AInstructionCDFGNode> currentCandidates, Set<AInstructionCDFGNode> currentValidCandidates){
        
        currentValidCandidates.forEach(candidate -> {
            dfg.getVerticesAfter(candidate).forEach(v -> {
                
                if(!dfg.hasOutput(v)){
                    currentCandidates.add(v);
                }else {
                    currentCandidates.addAll(dfg.getVerticesAfter(v));
                }
                
            });
        });
             
    }
    
    public static HardwareExpression addSignal(AInstructionCDFGNode operand, List<AInstructionCDFGModifier> modifierList, Map<AInstructionCDFGNode, HardwareExpression> signalMap) {
        
        if(!modifierList.isEmpty()) {
            
            for(AInstructionCDFGModifier modifier : modifierList) {

                if(modifier instanceof InstructionCDFGRangeSubscript) {
                    
                    RangedSelection rangeSelection = new RangedSelection(
                            (IdentifierReference) signalMap.get(operand), 
                            ((InstructionCDFGRangeSubscript)modifier).getLowerBound().intValue(),
                            ((InstructionCDFGRangeSubscript)modifier).getUpperBound().intValue()
                            );
                    
                    return rangeSelection;
                }
            }
        }
        
        return signalMap.get(operand);

    }
    
    public static List<HardwareExpression> getExpressionSignals(AInstructionCDFGSubgraph dfg, AInstructionCDFGNode expression, Map<AInstructionCDFGNode, HardwareExpression> signalMap) throws IllegalArgumentException{
        
        List<HardwareExpression> signals = new ArrayList<>();
        
        switch(dfg.getOperandsOf(expression).size()) {
            case 1:
                dfg.getOperandsOf(expression).forEach(op -> signals.add(signalMap.get(op)));
                break;
            case 2:
                signals.add(InstructionCDFGConverter.addSignal(dfg.getLeftOperand(expression), dfg.getLeftOperandEdge(expression).getModifiers(), signalMap));
                signals.add(InstructionCDFGConverter.addSignal(dfg.getRightOperand(expression), dfg.getRightOperandEdge(expression).getModifiers(), signalMap));
                break;
            default:    throw new IllegalArgumentException();
        }
        
        return signals;  
    }
    
    public static void visitDataFlowSubgraph(InstructionCDFG icdfg, HardwareNode parent, AInstructionCDFGSubgraph dfg) {
        
        Map<AInstructionCDFGNode, HardwareExpression> signal_map = InstructionCDFGConverter.generateSignalMap(dfg);  
        Set<AInstructionCDFGNode> nodes_being_resolved = InstructionCDFGConverter.getInitialCandidates(dfg);

        while(!nodes_being_resolved.isEmpty()) {
            
            Set<AInstructionCDFGNode> validCandidates = InstructionCDFGConverter.getValidCandidates(dfg, nodes_being_resolved, signal_map);
            
            validCandidates.forEach(v -> {

                IdentifierReference expr_var = (IdentifierReference) signal_map.getOrDefault(InstructionCDFGConverter.nextNodeisDataNode(dfg, v), new IdentifierReference(v.getUID()));
                signal_map.putIfAbsent(v, expr_var);
                
                HardwareExpression expr = HardwareNodeExpressionMap.generate(v.getClass(), InstructionCDFGConverter.getExpressionSignals(dfg, v, signal_map));   // Generates a new hardware expression for the vertex
                
                parent.addChild(new ProceduralBlockingStatement(expr_var, expr));// Adds it to the dfg parent node
            });
        
            InstructionCDFGConverter.getNextCandidates(dfg, nodes_being_resolved, validCandidates);
        }
        
        icdfg.getVerticesAfter(dfg).stream().filter(next -> !(next instanceof InstructionCDFGControlFlowMerge)).forEach(next -> InstructionCDFGConverter.visit(icdfg, parent, next));

    }
    
    public static HardwareNode visitControlFlowConditionalSubgraph(InstructionCDFG icdfg, AInstructionCDFGSubgraph dfg) {
        
        Map<AInstructionCDFGNode, HardwareExpression> signal_map = InstructionCDFGConverter.generateSignalMap(dfg);  
        Set<AInstructionCDFGNode> nodes_being_resolved = InstructionCDFGConverter.getInitialCandidates(dfg);

        while(!nodes_being_resolved.isEmpty()) {
            
            Set<AInstructionCDFGNode> validCandidates = InstructionCDFGConverter.getValidCandidates(dfg, nodes_being_resolved, signal_map);
            
            validCandidates.forEach(v -> signal_map.put(v, HardwareNodeExpressionMap.generate(v.getClass(), InstructionCDFGConverter.getExpressionSignals(dfg, v, signal_map))));
        
            InstructionCDFGConverter.getNextCandidates(dfg, nodes_being_resolved, validCandidates);
            
        }
        
        return signal_map.get(dfg.getVerticesBefore((AInstructionCDFGNode) dfg.getOutputs().toArray()[0]).toArray()[0]);
    }

    public static void visitControlFlowMergeNode(InstructionCDFG icdfg, HardwareNode parent, InstructionCDFGControlFlowMerge node) {
        icdfg.getVerticesAfter(node).forEach(v -> InstructionCDFGConverter.visit(icdfg, parent, v));  
    }
    
    public static void visitControlFlowIfNode(InstructionCDFG icdfg, HardwareNode parent, InstructionCDFGControlFlowIf node) {

        IfStatement conditional = new IfStatement((HardwareExpression) InstructionCDFGConverter.visitControlFlowConditionalSubgraph(icdfg, node));
        parent.addChild(conditional);

        InstructionCDFGConverter.visit(icdfg, conditional, icdfg.getTruePath(node));
        InstructionCDFGConverter.visitControlFlowMergeNode(icdfg, parent, node.getMerge());
    }
    
    public static void visitControlFlowIfElseNode(InstructionCDFG icdfg, HardwareNode parent, InstructionCDFGControlFlowIfElse node) {
        
        IfElseStatement conditional = new IfElseStatement((HardwareExpression) InstructionCDFGConverter.visitControlFlowConditionalSubgraph(icdfg, node));
        parent.addChild(conditional);

        InstructionCDFGConverter.visit(icdfg, conditional.getChild(1), icdfg.getTruePath(node));
        InstructionCDFGConverter.visit(icdfg, conditional.getChild(2), icdfg.getFalsePath(node));
        InstructionCDFGConverter.visitControlFlowMergeNode(icdfg, parent, node.getMerge());
    }
    
}
