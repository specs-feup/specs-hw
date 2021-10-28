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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.dot;

import java.util.*;

import org.jgrapht.nio.*;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.dataflowgraph.DataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraphDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGTrueEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.InstructionCDFGControlMergeNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.InstructionCDFGDecisionNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AControlFlowNode;

public class InstructionCDFGDOTExporter extends GeneralFlowGraphDOTExporter<GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>, AInstructionCDFGEdge>{

    private InstructionCDFGFlowSubGraphDOTExporter subgraph_dot_exporter;
    
    private int dfg_uid = 0;
    private int cfn_uid = 0;
    
    private final Map<AInstructionCDFGNode, String> validatedSubgraphIds;
    
    public InstructionCDFGDOTExporter() {
        
        this.subgraph_dot_exporter = new InstructionCDFGFlowSubGraphDOTExporter();
        this.validatedSubgraphIds = new HashMap<>();
        
        this.subgraph_dot_exporter.INDENT_BASE = this.INDENT_INNER;
    }
    
    protected String getVertexID(AInstructionCDFGNode vertex){
        
        String vertexId = validatedSubgraphIds.get(vertex);
           
        if (vertexId == null) {
    
            vertexId = vertex.getUID();
            
            if (!isValidID(vertexId)) {
                throw new ExportException("Generated id '" + vertexId + "'for vertex '" + vertex + "' is not valid with respect to the .dot language");
            }
            
            validatedSubgraphIds.put(vertex, vertexId);
        }
        
        return vertexId;
    }
    
    @Override
    protected String exportEdges(GeneralFlowGraph<GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>, AInstructionCDFGEdge> g) {
        
        String connector = this.computeConnector(g);
        StringBuilder edgeBuilder = new StringBuilder();
        
        this.setEdgeAttributeProvider((e) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            
            map.put("label", e.getDOTLabel()); 
            map.put("arrowhead", e.getDOTArrowHead());
            map.put("arrowtail", e.getDOTArrowTail());
            map.put("dir", DefaultAttribute.createAttribute("both"));
        
            return map;
        });
        
        g.edgeSet().forEach((edge) -> {
            
            edgeBuilder.append(INDENT_BASE + INDENT_INNER);
            
            GeneralFlowGraph<AInstructionCDFGNode,AInstructionCDFGEdge> source = g.getEdgeSource(edge);
            GeneralFlowGraph<AInstructionCDFGNode,AInstructionCDFGEdge> target = g.getEdgeTarget(edge); 
            
            if(source != null && target != null){
           
                if(source instanceof DataFlowGraph) {
                    edgeBuilder.append(this.getVertexID((AInstructionCDFGNode) source.getOutputs().toArray()[0]).toString() + ":s");
                }else if(source instanceof AControlFlowNode){
                    AInstructionCDFGNode control = ((AControlFlowNode)source).getVertex();
                    edgeBuilder.append(this.getVertexID(control));
                    
                    if(control instanceof InstructionCDFGDecisionNode) {    
                        edgeBuilder.append((edge instanceof InstructionCDFGTrueEdge) ? ":se" : ((edge instanceof InstructionCDFGFalseEdge) ? ":sw" : ""));
                    }else if(control instanceof InstructionCDFGControlMergeNode) {
                        edgeBuilder.append(":s");
                    }
                }
                
                edgeBuilder.append(connector);
                
                if(target instanceof DataFlowGraph) {
                    edgeBuilder.append(this.getVertexID((AInstructionCDFGNode) target.getInputs().toArray()[0]).toString() + ":n");    
                }else if (target instanceof AControlFlowNode){
                    AInstructionCDFGNode control = ((AControlFlowNode)target).getVertex();
                    edgeBuilder.append(this.getVertexID(control));
                    
                    edgeBuilder.append(((control instanceof InstructionCDFGControlMergeNode) || (control instanceof InstructionCDFGDecisionNode)) ? ":n" : "");
    
                }
        
                getEdgeAttributes(edge).ifPresent(m -> { edgeBuilder.append(renderAttributes(m));});
        
                edgeBuilder.append(";\n");
            }
        });
        
        return edgeBuilder.toString();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected String exportVertexes(GeneralFlowGraph<GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>, AInstructionCDFGEdge> g) {
     
        StringBuilder vertexSubgraphsBuilder = new StringBuilder();

        g.vertexSet().forEach((vertex) -> {
            
            GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> subgraph = (GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>)vertex;
            
            if(!subgraph.vertexSet().isEmpty()) {
                vertexSubgraphsBuilder.append(this.subgraph_dot_exporter.exportGraph(subgraph, (subgraph instanceof DataFlowGraph) ? "dfg" + String.valueOf(this.dfg_uid++) : "cfn" + String.valueOf(this.cfn_uid++))); 
            }
      
        });
        
        return vertexSubgraphsBuilder.toString();
    }
    
}