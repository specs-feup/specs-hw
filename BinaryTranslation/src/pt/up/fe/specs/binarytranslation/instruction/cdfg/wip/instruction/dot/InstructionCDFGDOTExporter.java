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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.dot;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.ExportException;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraphDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.ControlFlowNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.DataFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;

public class InstructionCDFGDOTExporter extends GeneralFlowGraphDOTExporter<GeneralFlowGraph<AInstructionCDFGNode, DefaultEdge>, AInstructionCDFGEdge>{

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
    protected String exportEdges(GeneralFlowGraph<GeneralFlowGraph<AInstructionCDFGNode, DefaultEdge>, AInstructionCDFGEdge> g) {
        
        String connector = this.computeConnector(g);
        StringBuilder edgeBuilder = new StringBuilder();
        
        this.setEdgeAttributeProvider((e) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            
            map.put("label", e.getDOTLabel()); 
            map.put("arrowhead", e.getDOTArrowHead());
        
            return map;
        });
        
        g.edgeSet().forEach((edge) -> {
            
            edgeBuilder.append(INDENT_BASE + INDENT_INNER);
            
            GeneralFlowGraph<AInstructionCDFGNode,DefaultEdge> source = g.getEdgeSource(edge);
            GeneralFlowGraph<AInstructionCDFGNode,DefaultEdge> target = g.getEdgeTarget(edge); 
            
            if(source != null && target != null){
           
                if(source instanceof DataFlowGraph) {
                    edgeBuilder.append(this.getVertexID(source.getFirstOutput()));
                }else if(source instanceof ControlFlowNode){
                    edgeBuilder.append(this.getVertexID(((ControlFlowNode<AInstructionCDFGNode>)source).getVertex()));
                }
                
                edgeBuilder.append(connector);
                
                if(target instanceof DataFlowGraph) {
                    edgeBuilder.append(this.getVertexID(target.getFirstInput()));
                }else if (target instanceof ControlFlowNode){
                    edgeBuilder.append(this.getVertexID(((ControlFlowNode<AInstructionCDFGNode>)target).getVertex()));
                }
        
                getEdgeAttributes(edge).ifPresent(m -> { edgeBuilder.append(renderAttributes(m));});
        
                edgeBuilder.append(";\n");
            }
        });
        
        return edgeBuilder.toString();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected String exportVertexes(GeneralFlowGraph<GeneralFlowGraph<AInstructionCDFGNode, DefaultEdge>, AInstructionCDFGEdge> g) {
     
        StringBuilder vertexSubgraphsBuilder = new StringBuilder();

        g.vertexSet().forEach((vertex) -> {
            
            GeneralFlowGraph<AInstructionCDFGNode, DefaultEdge> subgraph = (GeneralFlowGraph<AInstructionCDFGNode, DefaultEdge>)vertex;
            
            if(!subgraph.vertexSet().isEmpty()) {
                vertexSubgraphsBuilder.append(this.subgraph_dot_exporter.exportGraph(subgraph, (subgraph instanceof DataFlowGraph) ? "dfg" + String.valueOf(this.dfg_uid++) : "cfn" + String.valueOf(this.cfn_uid++))); 
            }
      
        });
        
        return vertexSubgraphsBuilder.toString();
    }
    
}
