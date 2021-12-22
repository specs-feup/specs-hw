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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.ExportException;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraphDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGFalseEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.conditional.InstructionCDFGTrueEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.InstructionCDFGControlConditionalNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.control.InstructionCDFGControlMergeNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.conditional.AInstructionCDFGControlFlowConditionalSubgraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.merge.InstructionCDFGControlFlowMerge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.data.InstructionCDFGDataFlowSubgraph;

public class InstructionCDFGDOTExporter extends GeneralFlowGraphDOTExporter<AInstructionCDFGSubgraph, AInstructionCDFGEdge>{

    private InstructionCDFGFlowSubGraphDOTExporter subgraph_dot_exporter;
    
    private int dfg_uid = 0;
    private int cfn_uid = 0;
    
    private final Map<AInstructionCDFGNode, Integer> cdfgUIDMap;
    
    public InstructionCDFGDOTExporter() {
        this.subgraph_dot_exporter = new InstructionCDFGFlowSubGraphDOTExporter();
        this.cdfgUIDMap = new HashMap<>();
        this.subgraph_dot_exporter.INDENT_BASE = this.INDENT_INNER;
    }

    @Override
    protected String exportEdges(GeneralFlowGraph<AInstructionCDFGSubgraph, AInstructionCDFGEdge> g) {
        
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
           
                if(source instanceof AInstructionCDFGControlFlowSubgraph) {
                    AInstructionCDFGNode control = ((AInstructionCDFGControlFlowSubgraph) source).getControlVertex();
                    edgeBuilder.append("v" + this.cdfgUIDMap.get(control));
                    
                    if(control instanceof InstructionCDFGControlConditionalNode) {    
                        edgeBuilder.append((edge instanceof InstructionCDFGTrueEdge) ? ":se" : ((edge instanceof InstructionCDFGFalseEdge) ? ":sw" : ""));
                    }else if(control instanceof InstructionCDFGControlMergeNode) {
                        edgeBuilder.append(":s");
                    }
                }else if(source instanceof InstructionCDFGDataFlowSubgraph){
                    edgeBuilder.append("v" + this.cdfgUIDMap.get((AInstructionCDFGNode) source.getOutputs().toArray()[0]).toString() + ":s");
                }
                
                edgeBuilder.append(connector);
                
                if (target instanceof InstructionCDFGControlFlowMerge){
                    AInstructionCDFGNode control = ((AInstructionCDFGControlFlowSubgraph)target).getControlVertex();
                    edgeBuilder.append("v" + this.cdfgUIDMap.get(control));
                    
                    edgeBuilder.append(((control instanceof InstructionCDFGControlMergeNode) || (control instanceof InstructionCDFGControlConditionalNode)) ? ":n" : "");
                }else if((target instanceof InstructionCDFGDataFlowSubgraph) || (target instanceof AInstructionCDFGControlFlowConditionalSubgraph)){
                    edgeBuilder.append("v" + this.cdfgUIDMap.get((AInstructionCDFGNode) target.getInputs().toArray()[0]).toString() + ":n");    
                } 
        
                getEdgeAttributes(edge).ifPresent(m -> edgeBuilder.append(renderAttributes(m)));
        
                edgeBuilder.append(";\n");
            }
        });
        
        return edgeBuilder.toString();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected String exportVertexes(GeneralFlowGraph<AInstructionCDFGSubgraph, AInstructionCDFGEdge> g) {
     
        StringBuilder vertexSubgraphsBuilder = new StringBuilder();

        g.vertexSet().forEach(vertex -> vertexSubgraphsBuilder.append(this.subgraph_dot_exporter.exportGraph(vertex, (vertex instanceof AInstructionCDFGControlFlowSubgraph) ? "cfn" + String.valueOf(this.cfn_uid++) : "dfg" + String.valueOf(this.dfg_uid++))));
        
        this.cdfgUIDMap.putAll(this.subgraph_dot_exporter.getVertexIDMap());
        
        return vertexSubgraphsBuilder.toString();
    }
    
}
