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

import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraphDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.AInstructionCDFGEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand.InstructionCDFGLeftOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.operand.InstructionCDFGRightOperandEdge;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.control.AInstructionCDFGControlFlowSubgraph;

public class InstructionCDFGFlowSubGraphDOTExporter extends GeneralFlowGraphDOTExporter<AInstructionCDFGNode, AInstructionCDFGEdge>{

    @Override
    protected String exportEdges(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> g) {
        
        this.setEdgeAttributeProvider(edge -> edge.getDOTAttributeMap());
        
        String connector = this.computeConnector(g);
        StringBuilder edgeBuilder = new StringBuilder();
        
        for (AInstructionCDFGEdge e : g.edgeSet()) {
            
            edgeBuilder.append(INDENT_BASE + INDENT_INNER);
            edgeBuilder.append(this.getVertexID(g.getEdgeSource(e)));
            edgeBuilder.append(":s" + connector);
            edgeBuilder.append(this.getVertexID(g.getEdgeTarget(e)));
            edgeBuilder.append(":" +  ((e instanceof InstructionCDFGRightOperandEdge) ? "e" : ((e instanceof InstructionCDFGLeftOperandEdge) ? "w" : "n")));
            
            getEdgeAttributes(e).ifPresent(m -> edgeBuilder.append(renderAttributes(m)));
    
            edgeBuilder.append(";\n");
        }
        
        return edgeBuilder.toString();
    }
    
    @Override
    protected String exportVertexes(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> g) {
        
        this.vertexIdProvider = new InstructionCDFGDOTVertexIdProvider();
        
        this.setVertexAttributeProvider(vertex -> vertex.getDOTAttributeMap());
        
        StringBuilder vertexesBuilder = new StringBuilder();
        
        g.vertexSet().forEach(vertex -> {

            vertexesBuilder.append(INDENT_BASE + INDENT_INNER + this.getVertexID(vertex));
            getVertexAttributes(vertex).ifPresent(m -> vertexesBuilder.append(this.renderAttributes(m)));
            vertexesBuilder.append(";\n");
                
        });
        
       return vertexesBuilder.toString();
    }
    
    @Override
    protected String exportHeader(GeneralFlowGraph<AInstructionCDFGNode, AInstructionCDFGEdge> g, String name){
        
        StringBuilder headerBuilder = new StringBuilder();
        
        headerBuilder.append(INDENT_BASE + SUBGRAPH_KEYWORD + " cluster_");
        headerBuilder.append(this.computeGraphId(name) + " {\n");
        headerBuilder.append(INDENT_BASE + INDENT_INNER + "label=\""+ this.graph_name +"\"\n");
        
        if(g instanceof AInstructionCDFGControlFlowSubgraph) {
            headerBuilder.append(INDENT_BASE + INDENT_INNER + "style=\"striped\"\n");
        }
        
        return headerBuilder.toString();
    }
    
}
