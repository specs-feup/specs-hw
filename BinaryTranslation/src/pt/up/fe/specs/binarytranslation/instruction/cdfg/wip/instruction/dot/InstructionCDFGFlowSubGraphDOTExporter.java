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

import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraphDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.ControlFlowNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.edge.operand.*;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;

public class InstructionCDFGFlowSubGraphDOTExporter extends GeneralFlowGraphDOTExporter<AInstructionCDFGNode, AInstructionCDFGEdge>{

    @Override
    protected String exportEdges(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> g) {
        
        this.setEdgeAttributeProvider((e) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            
            map.put("label", e.getDOTLabel()); 
            map.put("arrowhead", e.getDOTArrowHead());
            map.put("arrowtail", e.getDOTArrowTail());
            map.put("dir", DefaultAttribute.createAttribute("both"));
            return map;
        });
        
        String connector = this.computeConnector(g);
        StringBuilder edgeBuilder = new StringBuilder();
        
        for (AInstructionCDFGEdge e : g.edgeSet()) {
            
            edgeBuilder.append(INDENT_BASE + INDENT_INNER);
            edgeBuilder.append(this.getVertexID(g.getEdgeSource(e)));
            if(e instanceof AInstructionCDFGOperandEdge) {
                edgeBuilder.append(":s");
            }else {
                edgeBuilder.append(":s");
            }
            
            edgeBuilder.append(connector);
            edgeBuilder.append(this.getVertexID(g.getEdgeTarget(e)));
            if(e instanceof InstructionCDFGRightOperandEdge) {
                edgeBuilder.append(":e");
            }else if (e instanceof InstructionCDFGLeftOperandEdge) {
                edgeBuilder.append(":w");
            }else {
                edgeBuilder.append(":n");
            }
            getEdgeAttributes(e).ifPresent(m -> { edgeBuilder.append(renderAttributes(m));});
    
            edgeBuilder.append(";\n");
        }
        
        return edgeBuilder.toString();
    }
    
    @Override
    protected String exportVertexes(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> g) {
        
        this.vertexIdProvider = new InstructionCDFGDOTVertexIdProvider();
        
        
        this.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
 
            map.put("shape", v.getDOTShape());
            map.put("label", v.getDOTLabel());

            return map;
        });
        
        
        StringBuilder vertexesBuilder = new StringBuilder();
        
        for (AInstructionCDFGNode vertex : g.vertexSet()) {
         
                vertexesBuilder.append(INDENT_BASE);
                vertexesBuilder.append(INDENT_INNER);
                vertexesBuilder.append(this.getVertexID(vertex));
        
                getVertexAttributes(vertex).ifPresent(m -> {vertexesBuilder.append(this.renderAttributes(m));});
        
                vertexesBuilder.append(";\n");
            }
        
       return vertexesBuilder.toString();
    }
    
    @Override
    protected String exportHeader(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> g, String name)
    {
        StringBuilder headerBuilder = new StringBuilder();
        
        headerBuilder.append(INDENT_BASE);

        headerBuilder.append(SUBGRAPH_KEYWORD);
        headerBuilder.append(" cluster_").append(this.computeGraphId(name)).append(" {\n");
        
        headerBuilder.append(INDENT_BASE + INDENT_INNER + "label=\""+ this.graph_name +"\"\n");
        
        if(g instanceof ControlFlowNode) {
            headerBuilder.append(INDENT_BASE + INDENT_INNER + "color=\"white\"\n");
        }
        
        return headerBuilder.toString();
    }
    
}
