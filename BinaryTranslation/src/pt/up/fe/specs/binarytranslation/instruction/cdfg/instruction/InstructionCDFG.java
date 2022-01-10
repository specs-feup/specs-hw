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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jgrapht.Graphs;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.controlanddataflowgraph.ControlAndDataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.InstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.AInstructionCDFGConditionalEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGTrueEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.AInstructionCDFGControlNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGGeneratedVariable;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.data.InstructionCDFGLiteralNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIf;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.InstructionCDFGControlFlowIfElse;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFG extends ControlAndDataFlowGraph<AInstructionCDFGSubgraph, AInstructionCDFGEdge>{

    private Instruction instruction;
    
    private Map<AInstructionCDFGNode, AInstructionCDFGSubgraph> dataInputs;
    private Map<AInstructionCDFGNode, AInstructionCDFGSubgraph> dataOutputs;
    
    private static Predicate<AInstructionCDFGNode> notGeneratedVariableNode = v -> !(v instanceof InstructionCDFGGeneratedVariable);
    private static Predicate<AInstructionCDFGNode> notLiteralNode = v -> !(v instanceof InstructionCDFGLiteralNode);
    private static Predicate<AInstructionCDFGNode> notControlNode = v -> !(v instanceof AInstructionCDFGControlNode);
    
    public InstructionCDFG(Instruction instruction) {
        super(InstructionCDFGDataFlowSubgraph.class, AInstructionCDFGControlFlowSubgraph.class, AInstructionCDFGEdge.class);
        
        this.instruction = instruction;
        
        this.dataInputs = new HashMap<>();
        this.dataOutputs = new HashMap<>();
    }
    
    public Instruction getInstruction() {
        return this.instruction;
    }
    
    public void refresh() {
        this.generateInputs();
        this.generateOutputs();
        this.generateDataInputs();
        this.generateDataOutputs();
    }
    
    public boolean isControlFlowConditionNode(AInstructionCDFGSubgraph node) {
        return this.getVerticesAfter(node).stream().anyMatch(next -> next instanceof AInstructionCDFGControlFlowSubgraph);
    }
    
    public void generateDataInputs() {
        this.vertexSet().forEach(g -> g.getInputs().stream()
                .filter(InstructionCDFG.notLiteralNode.and(InstructionCDFG.notGeneratedVariableNode))
                .forEach(i -> this.dataInputs.put(i, g))
                );
    }
    
    public Set<AInstructionCDFGNode> getDataInputs(){
        return this.dataInputs.keySet();
    }
    
    public Map<AInstructionCDFGNode, AInstructionCDFGSubgraph> getDataInputsMap(){
        return this.dataInputs;
    }
    
    public Set<String> getDataInputsNames(){
        
        Set<String> names = new HashSet<>();
        
        this.getDataInputs().forEach(name -> names.add(name.getUID()));
        
        return names;
    }
    
    public Set<String> getDataInputsReferences(){
        Set<String> names = new HashSet<>();
        
        this.getDataInputs().forEach(name -> names.add(name.getReference()));
        
        return names;
    }
    
    public void generateDataOutputs() {    
        this.vertexSet().forEach(g -> g.getOutputs().stream()
                .filter(InstructionCDFG.notGeneratedVariableNode.and(InstructionCDFG.notControlNode))
                .forEach(o -> this.dataOutputs.put(o, g))
            );
    }
    
    public Set<AInstructionCDFGNode> getDataOutputs(){
        return this.dataOutputs.keySet();
    }
    
    public Map<AInstructionCDFGNode, AInstructionCDFGSubgraph> getDataOutputsMap(){
        return this.dataOutputs;
    }
    
    
    public Set<String> getDataOutputsNames(){
        
        Set<String> names = new HashSet<>();    
        
        this.getDataOutputs().forEach(name -> names.add(name.getUID()));
        
        return names;
    }
  
    public Set<String> getDataOutputsReferences(){
        Set<String> names = new HashSet<>();
        
        this.getDataOutputs().forEach(name -> names.add(name.getReference()));
        
        return names;
    }
    
    @Override
    public AInstructionCDFGEdge addEdge(AInstructionCDFGSubgraph sourceVertex, AInstructionCDFGSubgraph targetVertex) {
        AInstructionCDFGEdge edge = new InstructionCDFGEdge();
        this.addEdge(sourceVertex, targetVertex, edge);
        return edge;
    }
    
    @Override
    public void addControlEdgesTo(AInstructionCDFGSubgraph decision,AInstructionCDFGSubgraph path_true,AInstructionCDFGSubgraph path_false) {
        this.addEdge(decision, path_true, new InstructionCDFGTrueEdge());
        this.addEdge(decision, path_false, new InstructionCDFGFalseEdge());
    }

    private AInstructionCDFGSubgraph getConditionalPath(Class<? extends AInstructionCDFGConditionalEdge> type, AInstructionCDFGControlFlowConditionalSubgraph vertex) {
        
        for(AInstructionCDFGEdge e : this.outgoingEdgesOf(vertex)) {     
            if(type.isInstance(e)) {
                return this.getEdgeTarget(e);
            }
        }
        
        return null;
    }
    
    public AInstructionCDFGSubgraph getTruePath(AInstructionCDFGControlFlowConditionalSubgraph vertex){
        return this.getConditionalPath(InstructionCDFGTrueEdge.class, vertex);
    }
    
    public AInstructionCDFGSubgraph getFalsePath(AInstructionCDFGControlFlowConditionalSubgraph vertex){
        return this.getConditionalPath(InstructionCDFGFalseEdge.class, vertex);
    }

    @Override
    public String toString() {
        return this.instruction.getName();
    }
    
    public void splitSubgraph(AInstructionCDFGSubgraph subgraph, Set<AInstructionCDFGNode> vertices) {
        
    }
    
    @Override
    public void replaceVertex(AInstructionCDFGSubgraph current, AInstructionCDFGSubgraph replacement) {
        
        this.getInputs().remove(current);
        this.getOutputs().remove(current);
        
        this.assertVertexExist(current);
        
        this.addVertex(replacement);
        
        this.getVerticesBefore(current).forEach(before -> {
            this.addEdge(before, replacement, this.getEdge(before, current).duplicate());
        });
        
        this.getVerticesAfter(current).forEach(after -> {
            this.addEdge(replacement, after, this.getEdge(current, after).duplicate());
        });
        
        
        this.removeVertex(current);
    }
    
    public void copyVerticesOfSubgraph(AInstructionCDFGSubgraph source, AInstructionCDFGSubgraph target) {
        
        target.addVertices(source.vertexSet());

        Graphs.addAllEdges(target, source, source.edgeSet());
        
        
        target.generateInputs();
        target.generateOutputs();
        
    }
   
    
    public InstructionCDFGControlFlowIfElse convertIfToIfElse(InstructionCDFGControlFlowIf ifSubgraph) {

        InstructionCDFGControlFlowIfElse newIfElse = new InstructionCDFGControlFlowIfElse(ifSubgraph.getMerge());
        
        newIfElse.setInputUIDMap(ifSubgraph.getInputUIDMap());
        
        ifSubgraph.vertexSet().forEach(vertex -> {
            newIfElse.addVertex(vertex);
            if(ifSubgraph.outgoingEdgesOf(vertex).isEmpty()) {
                newIfElse.setControlVertex((AInstructionCDFGControlNode) vertex);
            }
        });
        
        ifSubgraph.edgeSet().forEach(edge -> newIfElse.addEdge(ifSubgraph.getEdgeSource(edge), ifSubgraph.getEdgeTarget(edge), edge.duplicate()));

        this.addVertex(newIfElse);
        
        this.incomingEdgesOf(ifSubgraph).forEach(incomingEdge -> this.addEdge(this.getEdgeSource(incomingEdge), newIfElse, incomingEdge.duplicate()));
        
        InstructionCDFGDataFlowSubgraph newDFG = new InstructionCDFGDataFlowSubgraph();
        
        newDFG.setInputUIDMap(ifSubgraph.getInputUIDMap());
        newDFG.generateOutputUIDMap();
        
        this.addVertex(newDFG);
        
        this.addEdge(newIfElse, this.getTruePath(ifSubgraph), new InstructionCDFGTrueEdge());
        this.addEdge(newIfElse, newDFG, new InstructionCDFGFalseEdge());

        this.addEdge(newDFG, ifSubgraph.getMerge(), new InstructionCDFGEdge());
        
        this.removeVertex(ifSubgraph);
        
        return newIfElse;
    }
}
