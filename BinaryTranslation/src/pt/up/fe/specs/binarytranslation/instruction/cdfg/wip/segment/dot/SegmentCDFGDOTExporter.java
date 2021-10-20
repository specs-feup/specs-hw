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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.segment.dot;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraphDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.dot.InstructionCDFGDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;

public class SegmentCDFGDOTExporter extends GeneralFlowGraphDOTExporter<InstructionCDFG, AInstructionCDFGEdge>{

    private InstructionCDFGDOTExporter icdfg_exporter;

    private int instruction_uid;
    
    public SegmentCDFGDOTExporter() {
        this.icdfg_exporter = new InstructionCDFGDOTExporter();
    }
/*
    @Override
    protected String exportEdges(GeneralFlowGraph<InstructionCDFG, AInstructionCDFGEdge> g) {
        
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
            
            InstructionCDFG source = g.getEdgeSource(edge);
            InstructionCDFG target = g.getEdgeTarget(edge); 
            
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
     */
    @SuppressWarnings("unchecked")
    @Override
    protected String exportVertexes(GeneralFlowGraph<InstructionCDFG, AInstructionCDFGEdge> g) {
     
        StringBuilder vertexSubgraphsBuilder = new StringBuilder();
        
        this.icdfg_exporter.INDENT_BASE = this.INDENT_INNER;
        
        g.vertexSet().forEach((vertex) -> {
            
            GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> subgraph = (GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge>)vertex;
            
            if(!subgraph.vertexSet().isEmpty()) {
                vertexSubgraphsBuilder.append(this.icdfg_exporter.exportGraph(subgraph, "i")); 
            }
      
        });
        
        return vertexSubgraphsBuilder.toString();
    }
   
}
