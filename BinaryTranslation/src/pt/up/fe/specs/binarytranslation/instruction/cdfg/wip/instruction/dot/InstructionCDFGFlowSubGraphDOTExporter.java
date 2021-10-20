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

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.general.GeneralFlowGraphDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.general.graph.ControlFlowNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.wip.instruction.node.AInstructionCDFGNode;

public class InstructionCDFGFlowSubGraphDOTExporter extends GeneralFlowGraphDOTExporter<AInstructionCDFGNode, DefaultEdge>{

    @Override
    protected String exportVertexes(GeneralFlowGraph<AInstructionCDFGNode, DefaultEdge> g) {
        
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
    protected String exportHeader(GeneralFlowGraph<AInstructionCDFGNode, DefaultEdge> g, String name)
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
